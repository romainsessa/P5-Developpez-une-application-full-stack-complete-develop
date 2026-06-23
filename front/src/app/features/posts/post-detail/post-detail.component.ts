import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../../../shared/models/post.interface';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../post.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-post-detail-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatIconModule],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css',
})
export class PostDetailComponent {
  public post$: Observable<Post> | undefined;
  public commentForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private fb: FormBuilder
  ) { }

  public ngOnInit(): void {
    const sessionId = this.route.snapshot.paramMap.get('id');
    if (sessionId) {
      this.post$ = this.postService.detail(sessionId);
    }
    this.initForm();
  }

  initForm() {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required]]
    });
  }

  public onSubmitComment(postId: number): void {
    if (this.commentForm.valid) {
      const content = this.commentForm.value.content;
      this.postService.addComment(postId, content).subscribe({
        next: () => {
          this.post$ = this.postService.detail(postId.toString());
          this.commentForm.reset();
        }
      });
    }
  }

  public back() {
    window.history.back();
  }
}
