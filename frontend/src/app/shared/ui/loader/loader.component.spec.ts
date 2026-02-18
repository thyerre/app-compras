import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LoaderService } from '../../../core/services/loader.service';
import { LoaderComponent } from './loader.component';

describe('LoaderComponent', () => {
  let component: LoaderComponent;
  let fixture: ComponentFixture<LoaderComponent>;
  let loaderService: LoaderService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoaderComponent, NoopAnimationsModule],
    }).compileComponents();

    fixture = TestBed.createComponent(LoaderComponent);
    component = fixture.componentInstance;
    loaderService = TestBed.inject(LoaderService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not render the overlay when loading is false', () => {
    const overlay = fixture.nativeElement.querySelector('.loader-overlay');
    expect(overlay).toBeNull();
  });

  it('should render the overlay when loading is true', () => {
    loaderService.show();
    fixture.detectChanges();

    const overlay = fixture.nativeElement.querySelector('.loader-overlay');
    expect(overlay).toBeTruthy();
  });

  it('should hide the overlay when loading switches back to false', () => {
    loaderService.show();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.loader-overlay')).toBeTruthy();

    loaderService.hide();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.loader-overlay')).toBeNull();
  });
});
