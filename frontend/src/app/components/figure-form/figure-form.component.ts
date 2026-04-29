import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FigureService } from '../../services/figure.service';
import { CategoryService } from '../../services/category.service';
import { TagService } from '../../services/tag.service';
import { UploadService } from '../../services/upload.service';
import { ToastService } from '../../services/toast.service';
import { Figure, Category, Tag, FigureCreateRequest, FigureUpdateRequest } from '../../models/figure.model';

@Component({
  selector: 'app-figure-form',
  templateUrl: './figure-form.component.html',
  styleUrls: ['./figure-form.component.css']
})
export class FigureFormComponent implements OnInit {
  figureForm: FormGroup;
  isEdit = false;
  figureId: number | null = null;
  loading = false;
  submitting = false;
  categories: Category[] = [];
  tags: Tag[] = [];
  selectedTags: string[] = [];
  newTag = '';
  uploadedImages: string[] = [];
  primaryImageIndex = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private figureService: FigureService,
    private categoryService: CategoryService,
    private tagService: TagService,
    private uploadService: UploadService,
    private toastService: ToastService
  ) {
    this.figureForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', Validators.maxLength(2000)],
      price: [null],
      purchaseDate: [''],
      categoryId: [null],
      isPublic: [true]
    });
  }

  ngOnInit(): void {
    this.loadCategories();
    this.loadTags();

    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.isEdit = true;
        this.figureId = +id;
        this.loadFigureForEdit(+id);
      }
    });
  }

  get f() {
    return this.figureForm.controls;
  }

  loadCategories(): void {
    this.categoryService.getMyCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('加载分类失败', error);
      }
    });
  }

  loadTags(): void {
    this.tagService.getMyTags().subscribe({
      next: (tags) => {
        this.tags = tags;
      },
      error: (error) => {
        console.error('加载标签失败', error);
      }
    });
  }

  loadFigureForEdit(id: number): void {
    this.loading = true;
    this.figureService.getFigureById(id).subscribe({
      next: (figure) => {
        this.figureForm.patchValue({
          name: figure.name,
          description: figure.description,
          price: figure.price,
          purchaseDate: figure.purchaseDate,
          categoryId: figure.categoryId,
          isPublic: figure.isPublic
        });

        if (figure.images) {
          this.uploadedImages = figure.images;
        }
        if (figure.tagNames) {
          this.selectedTags = figure.tagNames;
        }

        this.loading = false;
      },
      error: (error) => {
        console.error('加载手办详情失败', error);
        this.toastService.error('加载手办详情失败');
        this.loading = false;
        this.router.navigate(['/manage']);
      }
    });
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.uploadService.uploadFiles(input.files).subscribe({
        next: (urls) => {
          this.uploadedImages = [...this.uploadedImages, ...urls];
          this.toastService.success(`上传成功 ${urls.length} 张图片`);
        },
        error: (error) => {
          console.error('上传失败', error);
          this.toastService.error('图片上传失败');
        }
      });
    }
  }

  removeImage(index: number): void {
    this.uploadedImages.splice(index, 1);
    if (this.primaryImageIndex >= this.uploadedImages.length) {
      this.primaryImageIndex = Math.max(0, this.uploadedImages.length - 1);
    }
  }

  setPrimaryImage(index: number): void {
    this.primaryImageIndex = index;
  }

  addTag(): void {
    const tag = this.newTag.trim();
    if (tag && !this.selectedTags.includes(tag)) {
      this.selectedTags.push(tag);
      this.newTag = '';
    }
  }

  addSuggestedTag(tagName: string): void {
    if (!this.selectedTags.includes(tagName)) {
      this.selectedTags.push(tagName);
    }
  }

  removeTag(tag: string): void {
    const index = this.selectedTags.indexOf(tag);
    if (index > -1) {
      this.selectedTags.splice(index, 1);
    }
  }

  onSubmit(): void {
    if (this.figureForm.invalid || this.uploadedImages.length === 0) {
      if (this.uploadedImages.length === 0) {
        this.toastService.error('请至少上传一张图片');
      }
      return;
    }

    this.submitting = true;

    const formValue = this.figureForm.value;
    const primaryImageUrl = this.uploadedImages[this.primaryImageIndex];

    if (this.isEdit && this.figureId) {
      const updateRequest: FigureUpdateRequest = {
        name: formValue.name,
        description: formValue.description,
        price: formValue.price,
        purchaseDate: formValue.purchaseDate,
        categoryId: formValue.categoryId,
        isPublic: formValue.isPublic,
        imageUrls: this.uploadedImages,
        tagNames: this.selectedTags,
        primaryImageUrl: primaryImageUrl
      };

      this.figureService.updateFigure(this.figureId, updateRequest).subscribe({
        next: () => {
          this.toastService.success('更新成功');
          this.router.navigate(['/manage']);
        },
        error: (error) => {
          console.error('更新失败', error);
          this.toastService.error('更新失败');
          this.submitting = false;
        }
      });
    } else {
      const createRequest: FigureCreateRequest = {
        name: formValue.name,
        description: formValue.description,
        price: formValue.price,
        purchaseDate: formValue.purchaseDate,
        categoryId: formValue.categoryId,
        isPublic: formValue.isPublic,
        imageUrls: this.uploadedImages,
        tagNames: this.selectedTags
      };

      this.figureService.createFigure(createRequest).subscribe({
        next: () => {
          this.toastService.success('添加成功');
          this.router.navigate(['/manage']);
        },
        error: (error) => {
          console.error('添加失败', error);
          this.toastService.error('添加失败');
          this.submitting = false;
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/manage']);
  }

  getImageUrl(image: string): string {
    if (image && image.startsWith('http')) {
      return image;
    }
    if (image && image.startsWith('/uploads')) {
      return image;
    }
    return 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=anime%20figure%20placeholder&image_size=square';
  }
}
