import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LikeStatus } from '../models/figure.model';
import { ApiResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class LikeService {
  private apiUrl = '/api/likes';

  constructor(private http: HttpClient) {}

  toggleLike(figureId: number): Observable<boolean> {
    return this.http.post<ApiResponse<boolean>>(`${this.apiUrl}/${figureId}`, {})
      .pipe(map(response => response.data));
  }

  checkLike(figureId: number): Observable<LikeStatus> {
    return this.http.get<ApiResponse<LikeStatus>>(`${this.apiUrl}/${figureId}`)
      .pipe(map(response => response.data));
  }
}
