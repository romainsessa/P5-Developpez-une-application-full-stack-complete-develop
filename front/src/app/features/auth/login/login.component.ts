import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { UserService } from '../../users/user.service';
import { SessionService } from '../../../core/services/session.service';
import { LoginRequest } from '../models/loginRequest.interface';
import { User } from '../../../shared/models/user.interface';
import { CommonModule } from '@angular/common';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  public loginForm!: FormGroup;
  public onError = false;
  public errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private sessionService: SessionService
  ) {
    this.loginForm = this.fb.nonNullable.group({
      identifier: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  public onSubmit(): void {
    const loginRequest: LoginRequest = this.loginForm.getRawValue();

    this.authService.login(loginRequest).pipe(
      switchMap(() => {
        return this.userService.me();
      })
    ).subscribe({
      next: (user: User) => {
        this.sessionService.logIn(user);
        this.router.navigate(['/feed']);
      },
      error: (error) => {
        this.onError = true;
        this.errorMessage = error?.error?.message || 'Une erreur est survenue';
      }
    });

  }

  public goBack(): void {
    this.router.navigate(['/']);
  }
}
