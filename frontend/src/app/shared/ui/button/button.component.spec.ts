import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ButtonComponent } from './button.component';

describe('ButtonComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ButtonComponent, NoopAnimationsModule],
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(ButtonComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should emit clicked event when button is clicked', () => {
    const fixture = TestBed.createComponent(ButtonComponent);
    const component = fixture.componentInstance;
    const spy = jest.fn();
    component.clicked.subscribe(spy);
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');
    button.click();
    expect(spy).toHaveBeenCalled();
  });
});
