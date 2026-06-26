import { ProfileComponent } from './profile.component';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { describe, it, expect, vi } from 'vitest';

describe('ProfileComponent (logic only)', () => {

  function createComponent() {
    const mockRouter = {
      navigate: vi.fn()
    };

    const mockUserService = {
      me: vi.fn(),
      update: vi.fn()
    };

    const mockTopicService = {
      unsubscribe: vi.fn()
    };

    const mockSessionService = {
      logIn: vi.fn()
    };

    const component = new ProfileComponent(
      mockRouter as any,
      new FormBuilder(),
      mockUserService as any,
      mockTopicService as any,
      mockSessionService as any
    );

    return {
      component,
      mockUserService,
      mockTopicService,
      mockSessionService
    };
  }

  const mockUser = {
    id: 1,
    username: 'test',
    email: 'test@mail.com',
    topics: [
      { id: 1, name: 'Angular', description: '...' }
    ]
  };

  // ✅ création
  it('should create', () => {
    const { component } = createComponent();
    expect(component).toBeTruthy();
  });

  // ✅ ngOnInit → init form + load user
  it('should init form and load user', () => {
    const { component, mockUserService, mockSessionService } = createComponent();

    mockUserService.me.mockReturnValue(of(mockUser));

    component.ngOnInit();

    expect(component.userForm).toBeDefined();
    expect(mockUserService.me).toHaveBeenCalled();
    expect(component.user).toEqual(mockUser);
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockUser);

    expect(component.userForm.value.username).toBe('test');
    expect(component.userForm.value.email).toBe('test@mail.com');
  });

  // ✅ erreur loadMe
  it('should handle error when loading user', () => {
    const { component, mockUserService } = createComponent();

    mockUserService.me.mockReturnValue(
      throwError(() => ({
        error: { message: 'Erreur user' }
      }))
    );

    component.ngOnInit();

    expect(component.onError).toBe(true);
    expect(component.errorMessage).toBe('Erreur user');
  });

  // ✅ submit succès
  it('should update user and reset error state', () => {
    const {
      component,
      mockUserService,
      mockSessionService
    } = createComponent();

    mockUserService.me.mockReturnValue(of(mockUser));
    mockUserService.update.mockReturnValue(of(mockUser));

    component.ngOnInit();

    component.userForm.setValue({
      username: 'updated',
      email: 'updated@mail.com',
      password: ''
    });

    component.onSubmit();

    expect(mockUserService.update).toHaveBeenCalled();
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockUser);
    expect(component.onError).toBe(false);
    expect(component.errorMessage).toBe('');
  });

  // ✅ erreur update
  it('should handle error on update', () => {
    const { component, mockUserService } = createComponent();

    mockUserService.me.mockReturnValue(of(mockUser));
    mockUserService.update.mockReturnValue(
      throwError(() => ({
        error: { message: 'Erreur update' }
      }))
    );

    component.ngOnInit();

    component.userForm.setValue({
      username: 'test',
      email: 'test@mail.com',
      password: ''
    });

    component.onSubmit();

    expect(component.onError).toBe(true);
    expect(component.errorMessage).toBe('Erreur update');
  });

  // ✅ submit bloqué si invalide
  it('should not call update if form invalid', () => {
    const { component, mockUserService } = createComponent();

    mockUserService.me.mockReturnValue(of(mockUser));

    component.ngOnInit();

    // ✅ rendre invalide
    component.userForm.setValue({
      username: '', // required -> invalide
      email: 'test@mail.com',
      password: ''
    });

    component.onSubmit();

    expect(mockUserService.update).not.toHaveBeenCalled();
  });

  // ✅ unsubscribe → reload user
  it('should call unsubscribe and reload user', () => {
    const {
      component,
      mockUserService,
      mockTopicService
    } = createComponent();

    mockUserService.me.mockReturnValue(of(mockUser));
    mockTopicService.unsubscribe.mockReturnValue(of({}));

    component.ngOnInit();

    component.unsubscribe(1);

    expect(mockTopicService.unsubscribe).toHaveBeenCalledWith(1);
    expect(mockUserService.me).toHaveBeenCalledTimes(2); // init + reload
  });

});