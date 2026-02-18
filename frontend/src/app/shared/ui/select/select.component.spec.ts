import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { SelectComponent } from './select.component';

describe('SelectComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectComponent, NoopAnimationsModule],
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(SelectComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should write a value correctly', () => {
    const fixture = TestBed.createComponent(SelectComponent);
    const component = fixture.componentInstance;
    component.writeValue('test');
    expect(component.value()).toBe('test');
  });

  it('should set disabled state', () => {
    const fixture = TestBed.createComponent(SelectComponent);
    const component = fixture.componentInstance;
    component.setDisabledState(true);
    expect(component.disabled()).toBe(true);
  });
});
