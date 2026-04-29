import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import {
  User,
  LoginRequest,
  RegisterRequest,
  JwtResponse,
  ApiResponse
} from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;

  private apiUrl = '/api/auth';

  constructor(private http: HttpClient) {
    const storedUser = localStorage.getItem('user');
    this.currentUserSubject = new BehaviorSubject<User | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  get isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }

  login(request: LoginRequest): Observable<ApiResponse<JwtResponse>> {
    return this.http.post<ApiResponse<JwtResponse>>(`${this.apiUrl}/login`, request).pipe(
      tap(response => {
        if (response.success && response.data) {
          const jwt = response.data;
          localStorage.setItem('token', jwt.token);
          const user: User = {
            id: jwt.id,
            username: jwt.username,
            email: jwt.email
          };
          localStorage.setItem('user', JSON.stringify(user));
          this.currentUserSubject.next(user);
        }
      })
    );
  }

  register(request: RegisterRequest): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/register`, request);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  getCurrentUserId(): number | null {
    return this.currentUserValue?.id || null;
  }
}
