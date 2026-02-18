import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent, NoopAnimationsModule],
      providers: [provideHttpClient(), provideRouter([])],
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(LoginComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should have empty initial values for email and password', () => {
    const fixture = TestBed.createComponent(LoginComponent);
    const component = fixture.componentInstance;
    expect(component.email()).toBe('');
    expect(component.senha()).toBe('');
  });

  it('should not call login when fields are empty', () => {
    const fixture = TestBed.createComponent(LoginComponent);
    const component = fixture.componentInstance;
    const loginSpy = jest.spyOn(component.authState, 'login');
    component.onSubmit();
    expect(loginSpy).not.toHaveBeenCalled();
  });

  it('should call login with correct credentials when fields are filled', () => {
    const fixture = TestBed.createComponent(LoginComponent);
    const component = fixture.componentInstance;
    const loginSpy = jest.spyOn(component.authState, 'login').mockImplementation(() => {});
    component.email.set('test@test.com');
    component.senha.set('123456');
    component.onSubmit();
    expect(loginSpy).toHaveBeenCalledWith({
      email: 'test@test.com',
      senha: '123456',
    });
  });
});
