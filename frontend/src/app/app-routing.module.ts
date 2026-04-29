import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { FigureDetailComponent } from './components/figure-detail/figure-detail.component';
import { FigureManageComponent } from './components/figure-manage/figure-manage.component';
import { FigureFormComponent } from './components/figure-form/figure-form.component';
import { UserFiguresComponent } from './components/user-figures/user-figures.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'figure/:id', component: FigureDetailComponent },
  { path: 'user/:userId', component: UserFiguresComponent },
  { path: 'manage', component: FigureManageComponent, canActivate: [AuthGuard] },
  { path: 'manage/add', component: FigureFormComponent, canActivate: [AuthGuard] },
  { path: 'manage/edit/:id', component: FigureFormComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
