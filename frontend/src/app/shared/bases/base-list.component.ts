import { computed, Directive, inject, OnInit, signal } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';

@Directive()
export abstract class BaseListComponent<T> implements OnInit {
  protected readonly router = inject(Router);

  readonly items = signal<T[]>([]);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly pageIndex = signal(0);
  readonly loading = signal(false);

  readonly isEmpty = computed(() => this.items().length === 0 && !this.loading());

  abstract loadData(): void;
  abstract getRouteBase(): string;

  ngOnInit(): void {
    this.loadData();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadData();
  }

  onAdd(): void {
    this.router.navigate([this.getRouteBase(), 'novo']);
  }

  onEdit(id: number | string): void {
    this.router.navigate([this.getRouteBase(), id]);
  }

  onSearch(): void {
    this.pageIndex.set(0);
    this.loadData();
  }

  onClearFilters(): void {
    this.pageIndex.set(0);
    this.resetFilters();
    this.loadData();
  }

  protected abstract resetFilters(): void;
}
