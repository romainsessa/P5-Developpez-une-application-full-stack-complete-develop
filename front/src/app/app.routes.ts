import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [

  {
    path: '',
    loadComponent: () =>
      import('./features/home/home.component')
        .then(m => m.HomeComponent),
    canActivate: [authGuard],
    data: { public: true }
  },

  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component')
        .then(m => m.LoginComponent),
    canActivate: [authGuard],
    data: { public: true }
  },

  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component')
        .then(m => m.RegisterComponent),
    canActivate: [authGuard],
    data: { public: true }
  },

  {
    path: 'profile',
    loadComponent: () =>
      import('./features/users/profile/profile.component')
        .then(m => m.ProfileComponent),
    canActivate: [authGuard]
  },

  {
    path: 'topics',
    loadComponent: () =>
      import('./features/topics/topic-list/topic-list.component')
        .then(m => m.TopicListComponent),
    canActivate: [authGuard]
  },

  {
    path: 'posts/:id',
    loadComponent: () =>
      import('./features/posts/post-detail/post-detail.component')
        .then(m => m.PostDetailComponent),
    canActivate: [authGuard]
  },

  {
    path: 'feed',
    loadComponent: () =>
      import('./features/posts/feed/feed.component')
        .then(m => m.FeedComponent),
    canActivate: [authGuard]
  },

  {
    path: 'create-post',
    loadComponent: () =>
      import('./features/posts/create-post/create-post.component')
        .then(m => m.CreatePostComponent),
    canActivate: [authGuard]
  },

  {
    path: '**',
    redirectTo: ''
  }

];
