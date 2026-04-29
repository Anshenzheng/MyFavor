import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private apiUrl = '/api/upload';

  constructor(private http: HttpClient) {}

  uploadFile(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<ApiResponse<string>>(this.apiUrl, formData)
      .pipe(map(response => response.data));
  }

  uploadFiles(files: FileList): Observable<string[]> {
    const formData = new FormData();
    Array.from(files).forEach(file => {
      formData.append('files', file);
    });

    return this.http.post<ApiResponse<string[]>>(`${this.apiUrl}/multiple`, formData)
      .pipe(map(response => response.data));
  }
}
