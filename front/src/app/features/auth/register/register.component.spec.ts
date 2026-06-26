import { RegisterComponent } from './register.component';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { describe, it, expect, vi } from 'vitest';

describe('RegisterComponent (logic only)', () => {

  function createComponent() {

    const mockAuthService = {
      register: vi.fn()
    };

    const mockUserService = {
      me: vi.fn()
    };

    const mockSessionService = {
      logIn: vi.fn()
    };

    const mockRouter = {
      navigate: vi.fn()
    };

    const component = new RegisterComponent(
      new FormBuilder(),
      mockRouter as any,
      mockAuthService as any,
      mockUserService as any,
      mockSessionService as any
    );

    return {
      component,
      mockAuthService,
      mockUserService,
      mockSessionService,
      mockRouter
    };
  }

  // ✅ création
  it('should create', () => {
    const { component } = createComponent();
    expect(component).toBeTruthy();
  });

  // ✅ form invalide initialement
  it('should be invalid initially', () => {
    const { component } = createComponent();
    expect(component.registerForm.invalid).toBe(true);
  });

  // ✅ form valide avec bonnes valeurs
  it('should be valid when form is correctly filled', () => {
    const { component } = createComponent();

    component.registerForm.setValue({
      username: 'test',
      email: 'test@mail.com',
      password: 'Password123!'
    });

    expect(component.registerForm.valid).toBe(true);
  });

  // ✅ validation password (important coverage)
  it('should be invalid with weak password', () => {
    const { component } = createComponent();

    component.registerForm.setValue({
      username: 'test',
      email: 'test@mail.com',
      password: 'weak'
    });

    expect(component.registerForm.invalid).toBe(true);
  });

  // ✅ succès register complet
  it('should register, fetch user and navigate', () => {
    const {
      component,
      mockAuthService,
      mockUserService,
      mockSessionService,
      mockRouter
    } = createComponent();

    const mockUser = { id: 1, username: 'test' };
    const mockResponse = { token: 'fake-token' };

    mockAuthService.register.mockReturnValue(of(mockResponse));
    mockUserService.me.mockReturnValue(of(mockUser));

    const setItemSpy = vi.spyOn(localStorage.__proto__, 'setItem');

    component.registerForm.setValue({
      username: 'test',
      email: 'test@mail.com',
      password: 'Password123!'
    });

    component.onSubmit();

    expect(mockAuthService.register).toHaveBeenCalled();
    expect(setItemSpy).toHaveBeenCalledWith('token', 'fake-token');
    expect(mockUserService.me).toHaveBeenCalled();
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockUser);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/feed']);
  });

  // ✅ erreur register API
  it('should handle register error', () => {
    const { component, mockAuthService } = createComponent();

    mockAuthService.register.mockReturnValue(
      throwError(() => ({
        error: { message: 'Erreur inscription' }
      }))
    );

    component.registerForm.setValue({
      username: 'test',
      email: 'test@mail.com',
      password: 'Password123!'
    });

    component.onSubmit();

    expect(component.onError).toBe(true);
    expect(component.errorMessage).toBe('Erreur inscription');
  });

  // ✅ erreur sur me()
  it('should handle error when fetching user after register', () => {
    const {
      component,
      mockAuthService,
      mockUserService
    } = createComponent();

    mockAuthService.register.mockReturnValue(of({ token: 'token' }));
    mockUserService.me.mockReturnValue(
      throwError(() => ({}))
    );

    component.registerForm.setValue({
      username: 'test',
      email: 'test@mail.com',
      password: 'Password123!'
    });

    component.onSubmit();

    expect(component.onError).toBe(true);
  });

  // ✅ goBack
  it('should navigate to home on goBack', () => {
    const { component, mockRouter } = createComponent();

    component.goBack();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });

});