import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { UserService } from '../../users/user.service';
import { SessionService } from '../../../core/services/session.service';
import { AuthResponse } from '../models/authResponse.interface';
import { User } from '../../../shared/models/user.interface';
import { CommonModule } from '@angular/common';
import { RegisterRequest } from '../models/registerRequest.interface';

@Component({
  selector: 'app-register-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  public registerForm!: FormGroup;
  public onError = false;
  public errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private sessionService: SessionService
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(12),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{12,}$/)      
      ]]
    });
  }

  public onSubmit(): void {
    const registerRequest = this.registerForm.value as RegisterRequest;
    this.authService.register(registerRequest).subscribe({
      next: (response: AuthResponse) => {
        localStorage.setItem('token', response.token);
        this.userService.me().subscribe({
          next: (user: User) => {
            this.sessionService.logIn(user);
            this.router.navigate(['/feed']);
          },
          error: () => {
            this.onError = true;
          }
        });
      },
      error: (error) => {
        this.onError = true;
        this.errorMessage = error.error?.message; //TODO n'apparait pas
      }
    });
  }

  public goBack(): void {
    this.router.navigate(['/']);
  }
}
