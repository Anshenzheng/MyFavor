import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FigureService } from '../../services/figure.service';
import { Figure } from '../../models/figure.model';
import { PageResponse } from '../../models/user.model';

@Component({
  selector: 'app-user-figures',
  templateUrl: './user-figures.component.html',
  styleUrls: ['./user-figures.component.css']
})
export class UserFiguresComponent implements OnInit {
  userId: number | null = null;
  figures: Figure[] = [];
  username = '';
  currentPage = 0;
  pageSize = 12;
  totalPages = 0;
  totalElements = 0;
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private figureService: FigureService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const userId = params['userId'];
      if (userId) {
        this.userId = +userId;
        this.loadUserFigures(0);
      }
    });
  }

  loadUserFigures(page: number = 0): void {
    if (!this.userId) return;

    this.loading = true;
    this.currentPage = page;

    this.figureService.getUserFigures(this.userId, page, this.pageSize).subscribe({
      next: (response: PageResponse<Figure>) => {
        this.figures = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.currentPage = response.pageNumber;
        
        if (this.figures.length > 0) {
          this.username = this.figures[0].username;
        }
        
        this.loading = false;
      },
      error: (error) => {
        console.error('加载用户手办失败', error);
        this.loading = false;
      }
    });
  }

  viewFigure(figure: Figure): void {
    this.router.navigate(['/figure', figure.id]);
  }

  pageChanged(page: number): void {
    this.loadUserFigures(page);
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
