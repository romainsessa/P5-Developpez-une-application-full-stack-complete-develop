import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { SessionService } from '../../../core/services/session.service';
import { UserService } from '../../../features/users/user.service';
import { Observable } from 'rxjs';
import { User } from '../../models/user.interface';

@Component({
  selector: 'app-header-component',
  standalone: true,
  imports: [CommonModule, MatIconModule, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  public active = false;

  constructor(
    private router: Router,
    private sessionService: SessionService,
    private userService: UserService) {
  }

  public ngOnInit(): void {
    this.autoLog();
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate([''])
    this.autoLog();
  }

  public autoLog(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.userService.me().subscribe({
        next: (user: User) => {
          this.sessionService.logIn(user);
        },
        error: () => {
          localStorage.removeItem('token');
          this.sessionService.logOut();
        }
      });
    }
  }

  public showHeader(): boolean {
    return this.router.url !== '/' && this.router.url !== '';
  }
}