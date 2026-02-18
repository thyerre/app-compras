import { Directive, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Directive()
export abstract class BaseFormComponent implements OnInit {
  protected readonly router = inject(Router);
  protected readonly route = inject(ActivatedRoute);

  readonly isEditMode = signal(false);
  readonly entityId = signal<number | null>(null);
  readonly saving = signal(false);
  readonly loadingData = signal(false);

  abstract getRouteBase(): string;
  abstract initForm(): void;
  abstract loadEntity(id: number): void;
  abstract onSubmit(): void;

  ngOnInit(): void {
    this.initForm();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam && idParam !== 'novo') {
      const id = Number(idParam);
      if (!isNaN(id)) {
        this.isEditMode.set(true);
        this.entityId.set(id);
        this.loadEntity(id);
      }
    }
  }

  onCancel(): void {
    this.router.navigate([this.getRouteBase()]);
  }

  protected navigateBack(): void {
    this.router.navigate([this.getRouteBase()]);
  }
}
