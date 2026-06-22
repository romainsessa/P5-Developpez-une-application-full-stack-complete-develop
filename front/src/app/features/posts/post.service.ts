import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../../shared/models/post.interface';
import { CreatePostRequest } from '../posts/models/createPostRequest';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private pathService = '/api/post';

  constructor(private httpClient: HttpClient) {
  }

  public getAll(): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.pathService);
  }

  public detail(id: string): Observable<Post> {
    return this.httpClient.get<Post>(`${this.pathService}/${id}`);
  }

  public create(post: CreatePostRequest): Observable<number> {
    return this.httpClient.post<number>(`${this.pathService}`, post);
  }

  public addComment(postId: number, content: String): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/${postId}/comments`, { content });
  }

}
