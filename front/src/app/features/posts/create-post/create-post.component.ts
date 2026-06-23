import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Topic } from '../../../shared/models/topic.interface';
import { Router } from '@angular/router';
import { PostService } from '../post.service';
import { TopicService } from '../../topics/topic.service';
import { CreatePostRequest } from '../models/createPostRequest';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-create-post-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatIconModule],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css',
})
export class CreatePostComponent {
public postForm!: FormGroup;
  public topics: Topic[] = [];
  public onError = false;

  constructor (
    private router: Router,
    private fb: FormBuilder,
    private postService: PostService,
    private topicService: TopicService
  ) {}

  public ngOnInit(): void {
    this.initForm();
    this.loadTopics();
  }

  private initForm(): void {
    this.postForm = this.fb.group({
      topicId: ['', [Validators.required]],
      title: ['', [Validators.required]],
      content: ['', [Validators.required]],
    });
  }

  private loadTopics(): void {
    this.topicService.getAll().subscribe({
      next: (topics) => {
        this.topics = topics;
      },
      error: () => {
        this.onError = true;
      }
    });
  }

  public onSubmit(): void {
    if (this.postForm.valid) {
      const post = this.postForm.value as CreatePostRequest;
      console.log(post);

      this.postService.create(post).subscribe({
        next: (postId: number) => {
          this.router.navigate(['/detail', postId]);
        },
        error: () => {
          this.onError = true;
        }
      });
    }
  }

  public back(): void {
    window.history.back();
  }
}
