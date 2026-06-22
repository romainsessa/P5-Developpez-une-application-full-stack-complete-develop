import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../../shared/models/topic.interface';

@Injectable({
  providedIn: 'root',
})
export class TopicService {
  private pathService = '/api/topic';

  constructor(private httpClient: HttpClient) {
  }

  public getAll(): Observable<Topic[]> {
    return this.httpClient.get<Topic[]>(this.pathService);
  }

  public subscribe(topicId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/${topicId}/subscribe`, null);
  }

  public unsubscribe(topicId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.pathService}/${topicId}/unsubscribe`);
  }
}