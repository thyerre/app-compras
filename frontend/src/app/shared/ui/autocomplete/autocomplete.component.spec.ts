import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { AutocompleteComponent } from './autocomplete.component';

describe('AutocompleteComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AutocompleteComponent, NoopAnimationsModule],
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(AutocompleteComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should write a value correctly', () => {
    const fixture = TestBed.createComponent(AutocompleteComponent);
    const component = fixture.componentInstance;
    component.writeValue('test');
    expect(component.value()).toBe('test');
  });

  it('should filter options based on user input', () => {
    const fixture = TestBed.createComponent(AutocompleteComponent);
    const component = fixture.componentInstance;
    fixture.componentRef.setInput('options', [
      { value: 1, label: 'Alpha' },
      { value: 2, label: 'Beta' },
      { value: 3, label: 'Gamma' },
    ]);
    fixture.detectChanges();

    const event = { target: { value: 'al' } } as unknown as Event;
    component.onInput(event);
    expect(component.filteredOptions().length).toBe(1);
    expect(component.filteredOptions()[0].label).toBe('Alpha');
  });

  it('should set disabled state', () => {
    const fixture = TestBed.createComponent(AutocompleteComponent);
    const component = fixture.componentInstance;
    component.setDisabledState(true);
    expect(component.disabled()).toBe(true);
  });
});
