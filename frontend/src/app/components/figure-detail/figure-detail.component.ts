import { Component, OnInit, HostListener } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FigureService } from '../../services/figure.service';
import { LikeService } from '../../services/like.service';
import { WantService } from '../../services/want.service';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { Figure } from '../../models/figure.model';

@Component({
  selector: 'app-figure-detail',
  templateUrl: './figure-detail.component.html',
  styleUrls: ['./figure-detail.component.css']
})
export class FigureDetailComponent implements OnInit {
  figure: Figure | null = null;
  loading = true;
  isLoggedIn = false;

  // Lightbox
  lightboxOpen = false;
  currentImageIndex = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private figureService: FigureService,
    private likeService: LikeService,
    private wantService: WantService,
    private authService: AuthService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn;
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.loadFigure(+id);
      }
    });
  }

  loadFigure(id: number): void {
    this.loading = true;
    this.figureService.getFigureById(id).subscribe({
      next: (figure) => {
        this.figure = figure;
        this.loading = false;
      },
      error: (error) => {
        console.error('加载手办详情失败', error);
        this.toastService.error('加载手办详情失败');
        this.loading = false;
      }
    });
  }

  openLightbox(index: number = 0): void {
    this.currentImageIndex = index;
    this.lightboxOpen = true;
  }

  closeLightbox(): void {
    this.lightboxOpen = false;
  }

  prevImage(): void {
    if (this.figure && this.figure.images) {
      this.currentImageIndex = (this.currentImageIndex - 1 + this.figure.images.length) % this.figure.images.length;
    }
  }

  nextImage(): void {
    if (this.figure && this.figure.images) {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.figure.images.length;
    }
  }

  @HostListener('document:keydown', ['$event'])
  handleKeydown(event: KeyboardEvent): void {
    if (!this.lightboxOpen) return;

    switch (event.key) {
      case 'Escape':
        this.closeLightbox();
        break;
      case 'ArrowLeft':
        this.prevImage();
        break;
      case 'ArrowRight':
        this.nextImage();
        break;
    }
  }

  toggleLike(): void {
    if (!this.isLoggedIn) {
      this.toastService.info('请先登录后再点赞');
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    if (!this.figure) return;

    this.likeService.toggleLike(this.figure.id).subscribe({
      next: (isLiked) => {
        if (this.figure) {
          this.figure.isLiked = isLiked;
          this.figure.likeCount += isLiked ? 1 : -1;
        }
        this.toastService.success(isLiked ? '点赞成功' : '已取消点赞');
      },
      error: (error) => {
        this.toastService.error('操作失败');
      }
    });
  }

  toggleWant(): void {
    if (!this.isLoggedIn) {
      this.toastService.info('请先登录后再标记');
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    if (!this.figure) return;

    this.wantService.toggleWant(this.figure.id).subscribe({
      next: (isWanted) => {
        if (this.figure) {
          this.figure.isWanted = isWanted;
          this.figure.wantCount += isWanted ? 1 : -1;
        }
        this.toastService.success(isWanted ? '已标记想要' : '已取消标记');
      },
      error: (error) => {
        this.toastService.error('操作失败');
      }
    });
  }

  viewUserFigures(userId: number): void {
    this.router.navigate(['/user', userId]);
  }

  editFigure(): void {
    if (this.figure) {
      this.router.navigate(['/manage/edit', this.figure.id]);
    }
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

  canEdit(): boolean {
    return this.isLoggedIn && this.figure !== null &&
           this.authService.getCurrentUserId() === this.figure.userId;
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }
}
