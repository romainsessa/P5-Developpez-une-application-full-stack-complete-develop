import { PostDetailComponent } from './post-detail.component';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';
import { describe, it, expect, vi } from 'vitest';

describe('PostDetailComponent (logic only)', () => {

  function createComponent(routeId: string | null = '1') {

    const mockRoute = {
      snapshot: {
        paramMap: {
          get: vi.fn().mockReturnValue(routeId)
        }
      }
    };

    const mockPostService = {
      detail: vi.fn(),
      addComment: vi.fn()
    };

    const component = new PostDetailComponent(
      mockRoute as any,
      mockPostService as any,
      new FormBuilder()
    );

    return {
      component,
      mockRoute,
      mockPostService
    };
  }

  const postMock = {
    id: 1,
    title: 'Test',
    content: 'Content',
    author: 'user',
    topic: 'Angular',
    createdAt: '2024-01-01',
    comments: []
  };

  // ✅ création
  it('should create', () => {
    const { component } = createComponent();
    expect(component).toBeTruthy();
  });

  // ✅ init avec id → appel detail
  it('should load post on init when id exists', () => {
    const { component, mockPostService } = createComponent('1');

    mockPostService.detail.mockReturnValue(of(postMock));

    component.ngOnInit();

    expect(mockPostService.detail).toHaveBeenCalledWith('1');
    expect(component.post$).toBeDefined();
  });

  // ✅ init sans id → pas d'appel
  it('should not call service if no id in route', () => {
    const { component, mockPostService } = createComponent(null);

    component.ngOnInit();

    expect(mockPostService.detail).not.toHaveBeenCalled();
  });

  // ✅ form initialisé
  it('should initialize comment form', () => {
    const { component, mockPostService } = createComponent('1');

    mockPostService.detail.mockReturnValue(of(postMock));

    component.ngOnInit();

    expect(component.commentForm).toBeDefined();
    expect(component.commentForm.invalid).toBe(true);
  });

  // ✅ submit succès
  it('should add comment, reload post and reset form', () => {
    const { component, mockPostService } = createComponent('1');

    mockPostService.detail.mockReturnValue(of(postMock));
    mockPostService.addComment.mockReturnValue(of({}));

    component.ngOnInit();

    component.commentForm.setValue({
      content: 'New comment'
    });

    component.onSubmitComment(1);

    expect(mockPostService.addComment).toHaveBeenCalledWith(1, 'New comment');
    expect(mockPostService.detail).toHaveBeenCalledTimes(2); // init + reload
    expect(component.commentForm.value.content).toBe(null);
  });

  // ✅ submit bloqué si invalide
  it('should not call addComment if form invalid', () => {
    const { component, mockPostService } = createComponent('1');

    mockPostService.detail.mockReturnValue(of(postMock));

    component.ngOnInit();

    component.onSubmitComment(1);

    expect(mockPostService.addComment).not.toHaveBeenCalled();
  });

  // ✅ back navigation
  it('should call window.history.back', () => {
    const { component } = createComponent();

    const spy = vi.spyOn(window.history, 'back');

    component.back();

    expect(spy).toHaveBeenCalled();
  });

});