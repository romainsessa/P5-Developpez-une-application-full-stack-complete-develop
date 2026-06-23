import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../../../shared/models/topic.interface';
import { TopicService } from '../topic.service';
import { SessionService } from '../../../core/services/session.service';
import { UserService } from '../../users/user.service';
import { User } from '../../../shared/models/user.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-topic-list-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './topic-list.component.html',
  styleUrl: './topic-list.component.css',
})
export class TopicListComponent {
  public topics$: Observable<Topic[]>;

  constructor(
    private topicService: TopicService,
    private sessionService: SessionService,
    private userService: UserService
  ) {
    this.topics$ = this.topicService.getAll();
  }

  public subscribe(topicId: number): void {
    const userId = this.sessionService.user?.id;
    console.log('subscribe', topicId, userId);
    if (userId) {
      this.topicService.subscribe(topicId).subscribe({
        next: () => {
          this.fetchSession();
        }
      });
    }
  }

  public fetchSession(): void {
    this.userService.me().subscribe({
      next: (user: User) => {
        this.sessionService.updateUser(user);
      }
    });
  }

  public isSubscribed(topicId: number): boolean {
    const user = this.sessionService.user;
    if (!user || !user.topics) {
      return false;
    }
    return user.topics.some(t => t.id === topicId);
  }
}
