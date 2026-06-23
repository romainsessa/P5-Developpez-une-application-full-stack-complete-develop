import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { User } from "../../shared/models/user.interface";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  public isLogged = false;
  public user: User | undefined;

  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);

  constructor() {
    this.isLogged = !!localStorage.getItem('token');
  }

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public logIn(user: User) {
    this.user = user;
    this.isLogged = true;
    this.next();
  }

  public updateUser(user: User): void {
    this.user = user;
    this.next();
  }

  public logOut(): void {
    localStorage.removeItem('token');
    this.user = undefined;
    this.isLogged = false;
    this.next();
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }
}