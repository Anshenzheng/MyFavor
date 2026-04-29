import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FigureService } from '../../services/figure.service';
import { CategoryService } from '../../services/category.service';
import { TagService } from '../../services/tag.service';
import { ToastService } from '../../services/toast.service';
import { Figure, Category, Tag } from '../../models/figure.model';
import { PageResponse } from '../../models/user.model';

@Component({
  selector: 'app-figure-manage',
  templateUrl: './figure-manage.component.html',
  styleUrls: ['./figure-manage.component.css']
})
export class FigureManageComponent implements OnInit {
  figures: Figure[] = [];
  categories: Category[] = [];
  tags: Tag[] = [];
  currentPage = 0;
  pageSize = 12;
  totalPages = 0;
  totalElements = 0;
  loading = false;
  searchKeyword = '';
  selectedCategory: number | null = null;
  showDeleteModal = false;
  figureToDelete: Figure | null = null;

  constructor(
    private router: Router,
    private figureService: FigureService,
    private categoryService: CategoryService,
    private tagService: TagService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loadMyFigures();
    this.loadCategories();
    this.loadTags();
  }

  loadMyFigures(page: number = 0): void {
    this.loading = true;
    this.currentPage = page;

    const observable = this.searchKeyword
      ? this.figureService.searchMyFigures(this.searchKeyword, page, this.pageSize)
      : this.figureService.getMyFigures(page, this.pageSize);

    observable.subscribe({
      next: (response: PageResponse<Figure>) => {
        this.figures = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.currentPage = response.pageNumber;
        this.loading = false;
      },
      error: (error) => {
        console.error('加载手办列表失败', error);
        this.toastService.error('加载手办列表失败');
        this.loading = false;
      }
    });
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

  search(): void {
    this.loadMyFigures(0);
  }

  clearSearch(): void {
    this.searchKeyword = '';
    this.loadMyFigures(0);
  }

  viewFigure(figure: Figure): void {
    this.router.navigate(['/figure', figure.id]);
  }

  editFigure(figure: Figure): void {
    this.router.navigate(['/manage/edit', figure.id]);
  }

  addFigure(): void {
    this.router.navigate(['/manage/add']);
  }

  openDeleteModal(figure: Figure): void {
    this.figureToDelete = figure;
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.figureToDelete = null;
  }

  confirmDelete(): void {
    if (!this.figureToDelete) return;

    this.figureService.deleteFigure(this.figureToDelete.id).subscribe({
      next: () => {
        this.toastService.success('删除成功');
        this.closeDeleteModal();
        this.loadMyFigures(this.currentPage);
      },
      error: (error) => {
        console.error('删除失败', error);
        this.toastService.error('删除失败');
      }
    });
  }

  pageChanged(page: number): void {
    this.loadMyFigures(page);
  }

  get pages(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 5;
    let start = Math.max(0, this.currentPage - Math.floor(maxVisiblePages / 2));
    let end = Math.min(this.totalPages, start + maxVisiblePages);

    if (end - start < maxVisiblePages) {
      start = Math.max(0, end - maxVisiblePages);
    }

    for (let i = start; i < end; i++) {
      pages.push(i);
    }
    return pages;
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
