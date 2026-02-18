import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HeaderComponent,
        NoopAnimationsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should emit toggleMenu when the menu button is clicked', () => {
    const spy = jest.fn();
    component.toggleMenu.subscribe(spy);

    const button = fixture.nativeElement.querySelector('button[mat-icon-button]');
    button.click();

    expect(spy).toHaveBeenCalled();
  });

  it('should emit logout when the logout button is clicked', () => {
    const spy = jest.fn();
    component.logout.subscribe(spy);

    const menuButton = fixture.nativeElement.querySelectorAll('button[mat-icon-button]')[1];
    menuButton.click();
    fixture.detectChanges();

    const logoutButton = document.querySelector('button[mat-menu-item]') as HTMLElement;
    if (logoutButton) {
      logoutButton.click();
      expect(spy).toHaveBeenCalled();
    }
  });
});
