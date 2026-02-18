import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ButtonComponent } from '../../../shared/ui/button/button.component';
import { InputComponent } from '../../../shared/ui/input/input.component';
import { AuthState } from '../state/auth.state';

@Component({
  selector: 'app-login',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    FormsModule,
    MatCardModule,
    MatSnackBarModule,
    MatIconModule,
    InputComponent,
    ButtonComponent,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  readonly email = signal('');
  readonly senha = signal('');
  readonly showPassword = signal(false);

  constructor(readonly authState: AuthState) {}

  onSubmit(): void {
    if (!this.email() || !this.senha()) return;

    this.authState.login({
      email: this.email(),
      senha: this.senha(),
    });
  }
}
