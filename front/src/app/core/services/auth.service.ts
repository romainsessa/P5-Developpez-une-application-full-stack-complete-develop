import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { AuthResponse } from '../interfaces/authResponse.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private pathService = '/api/auth';

  constructor(private httpClient: HttpClient) { }

  public register(registerRequest: RegisterRequest): Observable<AuthResponse> {
    return this.httpClient.post<AuthResponse>(`${this.pathService}/register`, registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.httpClient.post<AuthResponse>(`${this.pathService}/login`, loginRequest);
  }
  
}
