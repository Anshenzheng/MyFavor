import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FigureService } from '../../services/figure.service';
import { Figure } from '../../models/figure.model';
import { PageResponse } from '../../models/user.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  popularFigures: Figure[] = [];
  recentFigures: Figure[] = [];
  allFigures: Figure[] = [];
  currentPage = 0;
  pageSize = 12;
  totalPages = 0;
  totalElements = 0;
  loading = false;
  searchKeyword = '';
  isSearching = false;

  constructor(
    private figureService: FigureService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadPopularFigures();
    this.loadAllFigures();
  }

  loadPopularFigures(): void {
    this.figureService.getPopularFigures(0, 8).subscribe({
      next: (response: PageResponse<Figure>) => {
        this.popularFigures = response.content;
      },
      error: (error) => {
        console.error('加载热门手办失败', error);
      }
    });
  }

  loadAllFigures(page: number = 0): void {
    this.loading = true;
    this.currentPage = page;

    const observable = this.isSearching && this.searchKeyword
      ? this.figureService.searchFigures(this.searchKeyword, page, this.pageSize)
      : this.figureService.getPublicFigures(page, this.pageSize);

    observable.subscribe({
      next: (response: PageResponse<Figure>) => {
        this.allFigures = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.currentPage = response.pageNumber;
        this.loading = false;
      },
      error: (error) => {
        console.error('加载手办列表失败', error);
        this.loading = false;
      }
    });
  }

  search(): void {
    if (this.searchKeyword.trim()) {
      this.isSearching = true;
    } else {
      this.isSearching = false;
    }
    this.loadAllFigures(0);
  }

  clearSearch(): void {
    this.searchKeyword = '';
    this.isSearching = false;
    this.loadAllFigures(0);
  }

  viewFigure(figure: Figure): void {
    this.router.navigate(['/figure', figure.id]);
  }

  viewUserFigures(userId: number): void {
    this.router.navigate(['/user', userId]);
  }

  pageChanged(page: number): void {
    this.loadAllFigures(page);
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
