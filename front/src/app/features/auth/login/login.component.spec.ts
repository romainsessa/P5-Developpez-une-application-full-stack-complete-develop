import { LoginComponent } from './login.component';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { describe, it, expect, vi } from 'vitest';

describe('LoginComponent (logic only)', () => {

  function createComponent() {

    const mockAuthService = {
      login: vi.fn()
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

    const component = new LoginComponent(
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

  it('should create', () => {
    const { component } = createComponent();
    expect(component).toBeTruthy();
  });

  it('should be invalid initially', () => {
    const { component } = createComponent();
    expect(component.loginForm.invalid).toBe(true);
  });

  it('should login and navigate on success', () => {
    const {
      component,
      mockAuthService,
      mockUserService,
      mockSessionService,
      mockRouter
    } = createComponent();

    const mockUser = { id: 1, username: 'test' };

    mockAuthService.login.mockReturnValue(of({}));
    mockUserService.me.mockReturnValue(of(mockUser));

    component.loginForm.setValue({
      identifier: 'test@mail.com',
      password: 'Password123!'
    });

    component.onSubmit();

    expect(mockAuthService.login).toHaveBeenCalled();
    expect(mockUserService.me).toHaveBeenCalled();
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockUser);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/feed']);
  });

  it('should handle login error', () => {
    const { component, mockAuthService } = createComponent();

    mockAuthService.login.mockReturnValue(
      throwError(() => ({
        error: { message: 'Identifiants invalides' }
      }))
    );

    component.loginForm.setValue({
      identifier: 'test@mail.com',
      password: 'wrong'
    });

    component.onSubmit();

    expect(component.onError).toBe(true);
    expect(component.errorMessage).toBe('Identifiants invalides');
  });

  it('should fallback error message', () => {
    const { component, mockAuthService } = createComponent();

    mockAuthService.login.mockReturnValue(
      throwError(() => ({}))
    );

    component.loginForm.setValue({
      identifier: 'test@mail.com',
      password: 'wrong'
    });

    component.onSubmit();

    expect(component.onError).toBe(true);
    expect(component.errorMessage).toBe('Une erreur est survenue');
  });

  it('should navigate to home', () => {
    const { component, mockRouter } = createComponent();

    component.goBack();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });

});