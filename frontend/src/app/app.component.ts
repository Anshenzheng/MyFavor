import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from './services/auth.service';
import { ToastService, Toast } from './services/toast.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = '手办阁';
  isLoggedIn = false;
  currentUser: any = null;
  toast: Toast | null = null;
  private authSubscription: Subscription | undefined;
  private toastSubscription: Subscription | undefined;
  private toastTimeout: any;

  constructor(
    private authService: AuthService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn;
    this.currentUser = this.authService.currentUserValue;

    this.authSubscription = this.authService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      this.currentUser = user;
    });

    this.toastSubscription = this.toastService.toast$.subscribe(toast => {
      this.toast = toast;
      if (this.toastTimeout) {
        clearTimeout(this.toastTimeout);
      }
      this.toastTimeout = setTimeout(() => {
        this.toast = null;
      }, 3000);
    });
  }

  ngOnDestroy(): void {
    this.authSubscription?.unsubscribe();
    this.toastSubscription?.unsubscribe();
    if (this.toastTimeout) {
      clearTimeout(this.toastTimeout);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  get isHomePage(): boolean {
    return this.router.url === '/' || this.router.url === '';
  }
}
