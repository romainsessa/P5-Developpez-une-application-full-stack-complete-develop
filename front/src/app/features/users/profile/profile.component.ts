import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../../shared/models/user.interface';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { TopicService } from '../../topics/topic.service';
import { SessionService } from '../../../core/services/session.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent {
  public userForm!: FormGroup;
  public user: User | undefined;
  public onError = false;
  public errorMessage: string = '';

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private userService: UserService,
    private topicService: TopicService,
    private sessionService: SessionService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.loadMe();
  }

  private loadMe(): void {
    this.userService.me().subscribe({
      next: (user: User) => {
        this.user = user;
        this.sessionService.updateUser(user);

        this.userForm.patchValue({
          username: user.username,
          email: user.email,
          password: ''
        });
      },
      error: (error: any) => {
        console.error(error);
        this.onError = true;
      }
    });
  }

  private initForm(): void {
    this.userForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['']
    });
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      this.userService.update(this.userForm.value).subscribe({
        next: (updatedUser: User) => {
          this.sessionService.updateUser(updatedUser);
          this.onError = false;
          this.errorMessage = '';
        },
        error: (error: any) => {
          console.error(error);
          this.onError = true;
          this.errorMessage = error?.error?.message;
        }
      });
    }
  }

  public unsubscribe(topicId: number): void {
    this.topicService.unsubscribe(topicId).subscribe({
      next: () => {
        this.loadMe();
      }
    });
  }
}
