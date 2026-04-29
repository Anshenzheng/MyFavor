import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Category } from '../models/figure.model';
import { ApiResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = '/api/categories';

  constructor(private http: HttpClient) {}

  getCategoriesByUserId(userId: number): Observable<Category[]> {
    return this.http.get<ApiResponse<Category[]>>(`${this.apiUrl}/${userId}`)
      .pipe(map(response => response.data));
  }

  getMyCategories(): Observable<Category[]> {
    return this.http.get<ApiResponse<Category[]>>(this.apiUrl)
      .pipe(map(response => response.data));
  }

  createCategory(name: string): Observable<Category> {
    return this.http.post<ApiResponse<Category>>(this.apiUrl, { name })
      .pipe(map(response => response.data));
  }

  updateCategory(id: number, name: string): Observable<Category> {
    return this.http.put<ApiResponse<Category>>(`${this.apiUrl}/${id}`, { name })
      .pipe(map(response => response.data));
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/${id}`)
      .pipe(map(() => undefined));
  }
}
