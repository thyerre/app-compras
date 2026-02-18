import { BreakpointObserver } from '@angular/cdk/layout';
import { ChangeDetectionStrategy, Component, OnInit, ViewChild, computed, inject, signal } from '@angular/core';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { RouterOutlet } from '@angular/router';
import { MenuItemModel } from '../../../core/models/auth.model';
import { AuthService } from '../../../core/services/auth.service';
import { AuthState } from '../../../features/auth/state/auth.state';
import { HeaderComponent } from './header/header.component';
import { SidenavComponent } from './sidenav/sidenav.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    RouterOutlet,
    MatSidenavModule,
    HeaderComponent,
    SidenavComponent,
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})
export class LayoutComponent implements OnInit {
  @ViewChild('sidenav') sidenav!: MatSidenav;

  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly authService = inject(AuthService);
  readonly authState = inject(AuthState);

  readonly menus = signal<MenuItemModel[]>([]);
  readonly isMobile = signal(false);
  readonly isSmallScreen = signal(false);

  readonly isCollapsed = computed(() => this.isSmallScreen() && !this.isMobile());

  ngOnInit(): void {
    this.loadMenus();
    this.observeBreakpoints();
  }

  private loadMenus(): void {
    this.authService.getMenus().subscribe({
      next: (menus) => this.menus.set(menus),
      error: () => this.menus.set([]),
    });
  }

  private observeBreakpoints(): void {
    // Mobile: < 600px
    this.breakpointObserver
      .observe(['(max-width: 599.98px)'])
      .subscribe(result => this.isMobile.set(result.matches));

    // Small screen (tablet): 600px - 959px → collapsed sidenav
    this.breakpointObserver
      .observe(['(min-width: 600px) and (max-width: 959.98px)'])
      .subscribe(result => this.isSmallScreen.set(result.matches));
  }

  onToggleMenu(): void {
    this.sidenav.toggle();
  }

  onMenuClick(): void {
    if (this.isMobile()) {
      this.sidenav.close();
    }
  }
}
