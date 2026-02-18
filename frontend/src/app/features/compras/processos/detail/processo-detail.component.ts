import { DatePipe, DecimalPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';

import { ProcessoCompraResponse } from '../../../../core/models/compras.model';
import { ComprasService } from '../../../../shared/services/compras.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';

@Component({
  selector: 'app-processo-detail',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatIconModule, ButtonComponent, DatePipe, DecimalPipe],
  templateUrl: './processo-detail.component.html',
  styleUrl: './processo-detail.component.scss',
})
export class ProcessoDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly comprasService = inject(ComprasService);

  readonly processo = signal<ProcessoCompraResponse | null>(null);
  readonly loading = signal(true);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.comprasService.findProcessoById(id).subscribe({
        next: (data) => {
          this.processo.set(data);
          this.loading.set(false);
        },
        error: () => this.loading.set(false),
      });
    }
  }

  onEdit(): void {
    const p = this.processo();
    if (p) {
      this.router.navigate(['/processos', p.id]);
    }
  }

  onBack(): void {
    this.router.navigate(['/processos']);
  }
}
