import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { UserService } from '../../users/user.service';
import { SessionService } from '../../../core/services/session.service';
import { LoginRequest } from '../models/loginRequest.interface';
import { AuthResponse } from '../models/authResponse.interface';
import { User } from '../../../shared/models/user.interface';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatIconModule],
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
    this.loginForm = this.fb.group({
      identifier: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  public onSubmit(): void {
    const loginRequest = this.loginForm.value as LoginRequest;
    this.authService.login(loginRequest).subscribe({
      next: (response: AuthResponse) => {
        localStorage.setItem('token', response.token);
        this.userService.me().subscribe({
          next: (user: User) => {
            this.sessionService.logIn(user);
            this.router.navigate(['/post']);
          },
          error: () => {
            this.onError = true;
          }
        });
      },
      error: (error) => {
        this.onError = true;
        this.errorMessage = error.error?.message;
      }
    });
  }

  public goBack(): void {
    this.router.navigate(['/']);
  }
}
