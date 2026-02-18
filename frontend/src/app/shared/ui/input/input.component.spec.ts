import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { InputComponent } from './input.component';

describe('InputComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InputComponent, NoopAnimationsModule],
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(InputComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should write a value correctly', () => {
    const fixture = TestBed.createComponent(InputComponent);
    const component = fixture.componentInstance;
    component.writeValue('test');
    expect(component.value()).toBe('test');
  });

  it('should handle null value gracefully', () => {
    const fixture = TestBed.createComponent(InputComponent);
    const component = fixture.componentInstance;
    component.writeValue(null as unknown as string);
    expect(component.value()).toBe('');
  });

  it('should set disabled state', () => {
    const fixture = TestBed.createComponent(InputComponent);
    const component = fixture.componentInstance;
    component.setDisabledState(true);
    expect(component.disabled()).toBe(true);
  });
});
