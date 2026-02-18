# App Compras — Frontend

## Overview

Single Page Application (SPA) for the **Procurement Management System** (Sistema de Gestão de Compras para Prefeituras), built with **Angular 19** and **Angular Material**.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Angular | 19.1 | UI framework |
| Angular Material | 19.1 | Component library (Material Design) |
| Tailwind CSS | 3.4 | Utility-first styling |
| @ngneat/elf | 2.5 | Reactive state management |
| @ngx-translate | 16.0 | Internationalization (i18n) |
| Jest | 29.7 | Unit testing framework |
| jest-preset-angular | 14.4 | Angular Jest integration |
| Playwright | — | E2E testing |
| TypeScript | 5.7 | Typed JavaScript |
| RxJS | 7.8 | Reactive programming |
| Nginx | Alpine | Production static server |
| Docker | — | Containerization |

---

## Architecture

The project follows a **Feature-based Architecture** with **Standalone Components** (no NgModules):

```
src/app/
├── app.component.ts/html/scss       # Root component
├── app.config.ts                    # Application providers configuration
├── app.routes.ts                    # Top-level route definitions
│
├── core/                            # Singleton services & infrastructure
│   ├── guards/                      #   Route guards
│   │   ├── auth.guard.ts            #     Protects authenticated routes
│   │   └── guest.guard.ts           #     Prevents login access when authenticated
│   ├── interceptors/                #   HTTP interceptors
│   │   ├── auth.interceptor.ts      #     Injects Bearer token + handles 401
│   │   ├── error.interceptor.ts     #     Global HTTP error handling
│   │   └── loader.interceptor.ts    #     Shows/hides global loader on mutations
│   ├── models/                      #   TypeScript interfaces
│   │   ├── auth.model.ts            #     LoginRequest, LoginResponse, Usuario, MenuItemModel
│   │   └── fornecedor.model.ts      #     Fornecedor DTOs, Estado, Municipio, PageResponse
│   └── services/                    #   Application-wide services
│       ├── auth.service.ts          #     Login, logout, token management
│       └── loader.service.ts        #     Global loading state (signal-based)
│
├── shared/                          # Reusable modules
│   ├── bases/                       #   Abstract base classes
│   │   ├── base-form.component.ts   #     Generic form component (init, load, save, cancel)
│   │   ├── base-list.component.ts   #     Generic list component (pagination, search, CRUD nav)
│   │   ├── base.service.ts          #     Generic CRUD service (HttpClient wrapper)
│   │   └── base.state.ts           #     Generic state with loading/error signals
│   ├── services/                    #   Feature HTTP services
│   │   └── fornecedor.service.ts    #     Fornecedor CRUD + domain data
│   └── ui/                          #   Reusable UI components
│       ├── input/                   #     Text input (ControlValueAccessor + ErrorStateMatcher)
│       ├── select/                  #     Select dropdown (ControlValueAccessor + ErrorStateMatcher)
│       ├── datepicker/              #     Date picker (ControlValueAccessor + ErrorStateMatcher)
│       ├── button/                  #     Action button (raised, flat, stroked, basic variants)
│       ├── filter/                  #     Collapsible filter panel with badge counter
│       ├── data-table/              #     Generic data table (sort, paginate, view/edit/delete actions)
│       ├── loader/                  #     Global fullscreen loader overlay
│       └── layout/                  #     Application shell
│           ├── layout.component.*   #       Main container (sidenav + toolbar + router-outlet)
│           ├── header/              #       Top toolbar with user menu
│           └── sidenav/             #       Side navigation with accordion menu tree
│
└── features/                        # Feature modules (lazy-loaded)
    ├── auth/                        #   Authentication feature
    │   ├── login/                   #     Login page component
    │   └── state/                   #     Auth state management (Elf store)
    ├── dashboard/                   #   Dashboard feature
    │   └── dashboard.component.*    #     Summary cards + placeholder
    └── fornecedores/                #   Supplier management feature
        ├── list/                    #     Listing with filters, pagination, actions
        ├── form/                    #     Create/edit form with sections
        └── detail/                  #     Read-only detail view with all sections
```

---

## Design Patterns & Principles

### 1. Standalone Components
- All components are `standalone: true` — no NgModules required.
- Imports are explicit per component, enabling better tree-shaking.

### 2. Signal-based Reactivity
- Angular Signals (`signal()`, `computed()`) used for local state.
- Inputs via `input()` function, outputs via `output()` function (modern Angular API).
- `ChangeDetectionStrategy.OnPush` on all components.

### 3. Functional Guards & Interceptors
- `CanActivateFn` used for route guards (no class-based guards).
- `HttpInterceptorFn` used for interceptors (functional style).

### 4. ControlValueAccessor Pattern
- Shared form components (`app-input`, `app-select`, `app-datepicker`) implement `ControlValueAccessor`.
- Custom `ErrorStateMatcher` (computed from `errorMessage` input) triggers `mat-error` display.
- This allows seamless integration with Angular Reactive Forms.

### 5. Base Component Pattern
- `BaseFormComponent` — handles route param extraction, edit mode detection, `initForm()`, `loadEntity()`, `onSubmit()`, `onCancel()`.
- `BaseListComponent<T>` — handles pagination, search, clear filters, navigation to add/edit.
- Feature components extend these bases and only implement feature-specific logic.

### 6. Lazy Loading
- All feature routes use `loadComponent()` for code-splitting.
- Each feature is loaded on demand, reducing initial bundle size.

### 7. Smart/Dumb Component Pattern
- **Layout** (smart): manages state, API calls, breakpoint observation.
- **Header**, **Sidenav** (dumb): receive inputs, emit outputs, no direct service injection.

### 8. Global Loader Pattern
- `LoaderService` exposes a `loading` signal.
- `loaderInterceptor` automatically shows the loader on `POST`, `PUT`, `DELETE` requests.
- `LoaderComponent` renders a fullscreen overlay when active.

---

## Development Rules

### ⚠️ CRITICAL — Form Components

1. **NEVER use `mat-form-field` directly in feature components.** Always use the shared UI components:
   - `<app-input>` for text inputs
   - `<app-select>` for dropdowns
   - `<app-datepicker>` for date pickers
2. These components encapsulate `mat-form-field` internally with proper `ErrorStateMatcher` and `ControlValueAccessor`.
3. Pass validation errors via `[errorMessage]="getError('fieldName')"`.

### Component Rules

4. All components must be **standalone** (`standalone: true`).
5. All components must use `ChangeDetectionStrategy.OnPush`.
6. Use **Signals** (`signal()`, `computed()`, `input()`, `output()`) — avoid `@Input()` / `@Output()` decorators.
7. Guards and interceptors must be **functional** (not class-based).
8. Features must use **lazy loading** via `loadComponent()` in routes.
9. Form components must extend `BaseFormComponent`.
10. List components must extend `BaseListComponent<T>`.

### Styling Rules

11. Use **Tailwind CSS** for layout and spacing (flex, grid, gap, padding, margin).
12. Use **Angular Material** for interactive components (buttons, checkboxes, tables, dialogs).
13. Component-specific styles go in `.component.scss` — avoid global overrides except in `styles.scss`.
14. Responsive design with Tailwind breakpoints: `sm:`, `md:`, `lg:`.

### File Convention

15. Every component has **4 files**:
```
component-name/
├── component-name.component.ts       # Logic & metadata
├── component-name.component.html     # Template
├── component-name.component.scss     # Styles
└── component-name.component.spec.ts  # Unit tests (Jest)
```

---

## Shared UI Components

| Component | Selector | Purpose | Key Inputs |
|---|---|---|---|
| InputComponent | `<app-input>` | Text input with CVA | `label`, `type`, `errorMessage`, `prefixIcon`, `suffixIcon` |
| SelectComponent | `<app-select>` | Dropdown with CVA | `label`, `options`, `errorMessage` |
| DatepickerComponent | `<app-datepicker>` | Date picker with CVA | `label`, `errorMessage`, `minDate`, `maxDate` |
| ButtonComponent | `<app-button>` | Action button | `label`, `icon`, `color`, `variant`, `type`, `disabled` |
| FilterComponent | `<app-filter>` | Collapsible filter panel | `fields` |
| DataTableComponent | `<app-data-table>` | Sortable paginated table | `columns`, `data`, `totalElements`, `showViewAction` |
| LoaderComponent | `<app-loader>` | Fullscreen spinner overlay | — |

---

## Routes

| Path | Component | Description |
|---|---|---|
| `/login` | LoginComponent | Login page (guest only) |
| `/dashboard` | DashboardComponent | Main dashboard |
| `/fornecedores` | FornecedorListComponent | Supplier listing |
| `/fornecedores/:id/detalhe` | FornecedorDetailComponent | Supplier detail (read-only) |
| `/fornecedores/:id` | FornecedorFormComponent | Create/edit supplier |

---

## Folder Conventions

| Folder | Purpose | Singleton? |
|---|---|---|
| `core/` | Services, guards, interceptors, models | Yes (app-wide) |
| `shared/ui/` | Reusable presentational components | No (imported per component) |
| `shared/bases/` | Abstract classes for forms & lists | No (extended per feature) |
| `shared/services/` | Feature HTTP services | Yes (providedIn: root) |
| `features/` | Feature-specific components & state | No (lazy-loaded) |

---

## HTTP Interceptors

| Interceptor | Responsibility |
|---|---|
| `authInterceptor` | Attaches `Authorization: Bearer <token>` header; handles 401 → redirect to login |
| `errorInterceptor` | Global HTTP error handling with snackbar notifications |
| `loaderInterceptor` | Shows global loader on `POST`/`PUT`/`DELETE` requests; hides on response or error |

---

## Running Locally

### Prerequisites
- Node.js 22+
- npm 10+

### Development Server
```bash
cd frontend
npm install
npm start
```

App runs at `http://localhost:4200`. API calls are proxied to `http://localhost:8080` via `proxy.conf.json`.

### Running Tests
```bash
npm test              # Single run
npm run test:watch    # Watch mode
npm run test:coverage # With coverage report
```

### E2E Tests
```bash
npx playwright test
```

### Production Build
```bash
npm run build
```

### Docker
```bash
docker-compose up --build
```

Frontend is served via Nginx at port 80 (mapped from container).
