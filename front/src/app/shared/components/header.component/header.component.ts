import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { SessionService } from '../../../core/services/session.service';
import { map, Observable } from 'rxjs';

@Component({
  selector: 'app-header-component',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  public active = false;
  public isLogged$!: Observable<boolean>;
  public showHeader$!: Observable<boolean>;

  constructor(
    private router: Router,
    private sessionService: SessionService
  ) { }

  ngOnInit(): void {
    this.isLogged$ = this.sessionService.$isLogged();
    this.showHeader$ = this.router.events.pipe(
      map(() => this.router.url !== '/' && this.router.url !== '')
    );
  }

  logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['/']);
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

}