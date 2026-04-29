import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  Figure,
  FigureCreateRequest,
  FigureUpdateRequest
} from '../models/figure.model';
import { ApiResponse, PageResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class FigureService {
  private apiUrl = '/api/figures';

  constructor(private http: HttpClient) {}

  getPublicFigures(page: number = 0, size: number = 12): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/public`, { params })
      .pipe(map(response => response.data));
  }

  getMyFigures(page: number = 0, size: number = 12): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/my`, { params })
      .pipe(map(response => response.data));
  }

  getUserFigures(userId: number, page: number = 0, size: number = 12): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/user/${userId}`, { params })
      .pipe(map(response => response.data));
  }

  getPopularFigures(page: number = 0, size: number = 10): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/popular`, { params })
      .pipe(map(response => response.data));
  }

  getMostViewedFigures(page: number = 0, size: number = 10): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/most-viewed`, { params })
      .pipe(map(response => response.data));
  }

  searchFigures(keyword: string, page: number = 0, size: number = 12): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/search`, { params })
      .pipe(map(response => response.data));
  }

  searchMyFigures(keyword: string, page: number = 0, size: number = 12): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/search/my`, { params })
      .pipe(map(response => response.data));
  }

  getFigureById(id: number): Observable<Figure> {
    return this.http.get<ApiResponse<Figure>>(`${this.apiUrl}/${id}`)
      .pipe(map(response => response.data));
  }

  getFiguresByCategory(categoryId: number, page: number = 0, size: number = 12): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/category/${categoryId}`, { params })
      .pipe(map(response => response.data));
  }

  getFiguresByTag(tagId: number, page: number = 0, size: number = 12): Observable<PageResponse<Figure>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PageResponse<Figure>>>(`${this.apiUrl}/tag/${tagId}`, { params })
      .pipe(map(response => response.data));
  }

  createFigure(request: FigureCreateRequest): Observable<Figure> {
    return this.http.post<ApiResponse<Figure>>(this.apiUrl, request)
      .pipe(map(response => response.data));
  }

  updateFigure(id: number, request: FigureUpdateRequest): Observable<Figure> {
    return this.http.put<ApiResponse<Figure>>(`${this.apiUrl}/${id}`, request)
      .pipe(map(response => response.data));
  }

  deleteFigure(id: number): Observable<void> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/${id}`)
      .pipe(map(() => undefined));
  }
}
