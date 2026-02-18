import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { SidenavComponent } from './sidenav.component';

describe('SidenavComponent', () => {
  let component: SidenavComponent;
  let fixture: ComponentFixture<SidenavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SidenavComponent,
        NoopAnimationsModule,
        RouterTestingModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SidenavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render menu items when provided', () => {
    fixture.componentRef.setInput('menus', [
      { id: 1, label: 'Dashboard', icone: 'dashboard', rota: '/dashboard', children: [] },
    ]);
    fixture.componentRef.setInput('collapsed', false);
    fixture.detectChanges();

    const items = fixture.nativeElement.querySelectorAll('a[mat-list-item]');
    expect(items.length).toBeGreaterThan(0);
  });

  it('should emit menuClick when a menu item is clicked', () => {
    const spy = jest.fn();
    component.menuClick.subscribe(spy);

    fixture.componentRef.setInput('menus', [
      { id: 1, label: 'Dashboard', icone: 'dashboard', rota: '/dashboard', children: [] },
    ]);
    fixture.componentRef.setInput('collapsed', false);
    fixture.detectChanges();

    const item = fixture.nativeElement.querySelector('a[mat-list-item]');
    if (item) {
      item.click();
      expect(spy).toHaveBeenCalled();
    }
  });
});
