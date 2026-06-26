import { Component } from '@angular/core';
import { BehaviorSubject, combineLatest, map, Observable, startWith } from 'rxjs';
import { Post } from '../../../shared/models/post.interface';
import { PostService } from '../post.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-feed-component',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css',
})
export class FeedComponent {
  public posts$: Observable<Post[]>;
  public sortMode$ = new BehaviorSubject<'date-desc' | 'date-asc'>('date-desc');

  constructor(
    private postService: PostService,
  ) {
    this.posts$ = combineLatest([
      this.postService.getAll(),
      this.sortMode$.pipe(startWith('date-desc'))
    ]).pipe(map(([posts, order]) => {
      return [...posts].sort((a, b) => {
        const dateA = new Date(a.createdAt).getTime();
        const dateB = new Date(b.createdAt).getTime();
        return order === 'date-asc'
          ? dateA - dateB
          : dateB - dateA;
      });
    })
    );
  }

  public onSort(): void {
    const current = this.sortMode$.value;
    this.sortMode$.next(current === 'date-desc' ? 'date-asc' : 'date-desc');
  }
}
