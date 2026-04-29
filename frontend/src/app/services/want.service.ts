import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { WantStatus } from '../models/figure.model';
import { ApiResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class WantService {
  private apiUrl = '/api/wants';

  constructor(private http: HttpClient) {}

  toggleWant(figureId: number): Observable<boolean> {
    return this.http.post<ApiResponse<boolean>>(`${this.apiUrl}/${figureId}`, {})
      .pipe(map(response => response.data));
  }

  checkWant(figureId: number): Observable<WantStatus> {
    return this.http.get<ApiResponse<WantStatus>>(`${this.apiUrl}/${figureId}`)
      .pipe(map(response => response.data));
  }
}
