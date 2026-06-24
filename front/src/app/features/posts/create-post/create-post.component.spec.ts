import { CreatePostComponent } from './create-post.component';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { describe, it, expect, vi } from 'vitest';

describe('CreatePostComponent (logic only)', () => {

  function createComponent() {
    const mockRouter = {
      navigate: vi.fn()
    };

    const mockPostService = {
      create: vi.fn()
    };

    const mockTopicService = {
      getAll: vi.fn()
    };

    const component = new CreatePostComponent(
      mockRouter as any,
      new FormBuilder(),
      mockPostService as any,
      mockTopicService as any
    );

    return {
      component,
      mockRouter,
      mockPostService,
      mockTopicService
    };
  }

  const topicsMock = [
    { id: 1, name: 'Angular' },
    { id: 2, name: 'Java' }
  ];

  // ✅ création
  it('should create', () => {
    const { component } = createComponent();
    expect(component).toBeTruthy();
  });

  // ✅ ngOnInit initialise le formulaire + charge topics
  it('should initialize form and load topics on init', () => {
    const { component, mockTopicService } = createComponent();

    mockTopicService.getAll.mockReturnValue(of(topicsMock));

    component.ngOnInit();

    expect(component.postForm).toBeDefined();
    expect(mockTopicService.getAll).toHaveBeenCalled();
    expect(component.topics).toEqual(topicsMock);
  });

  // ✅ erreur lors du chargement des topics
  it('should handle error when loading topics', () => {
    const { component, mockTopicService } = createComponent();

    mockTopicService.getAll.mockReturnValue(
      throwError(() => ({
        error: { message: 'Erreur topics' }
      }))
    );

    component.ngOnInit();

    expect(component.onError).toBe(true);
    expect(component.errorMessage).toBe('Erreur topics');
  });

  // ✅ form invalide
  it('should be invalid initially', () => {
    const { component, mockTopicService } = createComponent();

    mockTopicService.getAll.mockReturnValue(of([])); // ✅ AJOUT

    component.ngOnInit();

    expect(component.postForm.invalid).toBe(true);
  });


  // ✅ submit succès
  it('should create post and navigate on success', () => {
    const {
      component,
      mockPostService,
      mockRouter,
      mockTopicService
    } = createComponent();

    mockTopicService.getAll.mockReturnValue(of(topicsMock));
    mockPostService.create.mockReturnValue(of(10));

    component.ngOnInit();

    component.postForm.setValue({
      topicId: 1,
      title: 'Test',
      content: 'Content'
    });

    component.onSubmit();

    expect(mockPostService.create).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/detail', 10]);
  });

  // ✅ erreur submit
  it('should handle error on submit', () => {
    const {
      component,
      mockPostService,
      mockTopicService
    } = createComponent();

    mockTopicService.getAll.mockReturnValue(of(topicsMock));
    mockPostService.create.mockReturnValue(
      throwError(() => ({
        error: { message: 'Erreur création' }
      }))
    );

    component.ngOnInit();

    component.postForm.setValue({
      topicId: 1,
      title: 'Test',
      content: 'Content'
    });

    component.onSubmit();

    expect(component.onError).toBe(true);
    expect(component.errorMessage).toBe('Erreur création');
  });

  // ✅ submit bloqué si form invalide
  it('should not call service if form invalid', () => {
    const {
      component,
      mockPostService,
      mockTopicService
    } = createComponent();

    mockTopicService.getAll.mockReturnValue(of(topicsMock));

    component.ngOnInit();

    component.onSubmit();

    expect(mockPostService.create).not.toHaveBeenCalled();
  });

  // ✅ back navigation
  it('should call window.history.back', () => {
    const { component } = createComponent();

    const backSpy = vi.spyOn(window.history, 'back');

    component.back();

    expect(backSpy).toHaveBeenCalled();
  });

});