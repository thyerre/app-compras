import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { LayoutComponent } from './layout.component';

describe('LayoutComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LayoutComponent, NoopAnimationsModule],
      providers: [provideHttpClient(), provideRouter([])],
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(LayoutComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should initialize with an empty menus array', () => {
    const fixture = TestBed.createComponent(LayoutComponent);
    expect(fixture.componentInstance.menus()).toEqual([]);
  });

  it('should initialize with isMobile set to false', () => {
    const fixture = TestBed.createComponent(LayoutComponent);
    expect(fixture.componentInstance.isMobile()).toBe(false);
  });
});
