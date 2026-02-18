import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { FornecedorResponse } from '../../../core/models/fornecedor.model';
import { FornecedorService } from '../../../shared/services/fornecedor.service';
import { ButtonComponent } from '../../../shared/ui/button/button.component';

@Component({
  selector: 'app-fornecedor-detail',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatIconModule, ButtonComponent, DatePipe],
  templateUrl: './fornecedor-detail.component.html',
  styleUrl: './fornecedor-detail.component.scss',
})
export class FornecedorDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fornecedorService = inject(FornecedorService);

  readonly fornecedor = signal<FornecedorResponse | null>(null);
  readonly loading = signal(true);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.fornecedorService.findById(id).subscribe({
        next: (data) => {
          this.fornecedor.set(data);
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
        },
      });
    }
  }

  onEdit(): void {
    const f = this.fornecedor();
    if (f) {
      this.router.navigate(['/fornecedores', f.id]);
    }
  }

  onBack(): void {
    this.router.navigate(['/fornecedores']);
  }
}
