import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Tag } from '../models/figure.model';
import { ApiResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class TagService {
  private apiUrl = '/api/tags';

  constructor(private http: HttpClient) {}

  getTagsByUserId(userId: number): Observable<Tag[]> {
    return this.http.get<ApiResponse<Tag[]>>(`${this.apiUrl}/${userId}`)
      .pipe(map(response => response.data));
  }

  getMyTags(): Observable<Tag[]> {
    return this.http.get<ApiResponse<Tag[]>>(this.apiUrl)
      .pipe(map(response => response.data));
  }

  createTag(name: string): Observable<Tag> {
    return this.http.post<ApiResponse<Tag>>(this.apiUrl, { name })
      .pipe(map(response => response.data));
  }

  createTags(tagNames: string[]): Observable<Tag[]> {
    return this.http.post<ApiResponse<Tag[]>>(`${this.apiUrl}/batch`, tagNames)
      .pipe(map(response => response.data));
  }

  updateTag(id: number, name: string): Observable<Tag> {
    return this.http.put<ApiResponse<Tag>>(`${this.apiUrl}/${id}`, { name })
      .pipe(map(response => response.data));
  }

  deleteTag(id: number): Observable<void> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/${id}`)
      .pipe(map(() => undefined));
  }
}
