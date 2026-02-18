import { ChangeDetectionStrategy, Component, inject, input, output } from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { MenuItemModel } from '../../../../core/models/auth.model';

@Component({
  selector: 'app-sidenav',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    RouterLink,
    RouterLinkActive,
    MatListModule,
    MatIconModule,
    MatExpansionModule,
    MatTooltipModule,
  ],
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.scss',
})
export class SidenavComponent {
  private readonly router = inject(Router);

  readonly menus = input<MenuItemModel[]>([]);
  readonly collapsed = input<boolean>(false);

  readonly menuClick = output<void>();

  isChildActive(menu: MenuItemModel): boolean {
    if (!menu.children?.length) return false;
    return menu.children.some(child =>
      child.rota ? this.router.isActive(child.rota, { paths: 'subset', queryParams: 'subset', fragment: 'ignored', matrixParams: 'ignored' }) : false
    );
  }
}
