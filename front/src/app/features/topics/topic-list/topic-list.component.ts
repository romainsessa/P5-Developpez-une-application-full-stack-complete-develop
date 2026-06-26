import { Component, OnInit } from '@angular/core';
import { Observable, switchMap, tap } from 'rxjs';
import { Topic } from '../../../shared/models/topic.interface';
import { TopicService } from '../topic.service';
import { SessionService } from '../../../core/services/session.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-topic-list-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './topic-list.component.html',
  styleUrl: './topic-list.component.css',
})
export class TopicListComponent implements OnInit {

  public topics$!: Observable<Topic[]>;
  public subscribedTopicIds = new Set<number>();

  constructor(
    private topicService: TopicService,
    private sessionService: SessionService
  ) {
    this.topics$ = this.topicService.getAll();
  }

  ngOnInit(): void {
    this.updateSubscriptions();
  }

  private updateSubscriptions(): void {
    const user = this.sessionService.user;

    this.subscribedTopicIds = new Set(
      (user?.topics ?? [])
        .map(t => t.id)
        .filter((id): id is number => id !== undefined)
    );
  }

  public isSubscribed(topicId: number): boolean {
    return this.subscribedTopicIds.has(topicId);
  }

  public subscribe(topicId: number): void {
    this.topicService.subscribe(topicId).pipe(
      switchMap(() => this.sessionService.refreshUser$())
    ).subscribe({
      next: () => {
        this.updateSubscriptions();
      }
    });

  }

}