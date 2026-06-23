import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../shared/models/user.interface';
import { UpdateUserRequest } from './models/UpdateUserRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private pathService = 'http://localhost:9000/api/user';

  constructor(private httpClient: HttpClient) { }

  public me(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/me`);
  }

  public update(user: UpdateUserRequest): Observable<User> {
    return this.httpClient.put<User>(`${this.pathService}/me`, user);
  }
}
