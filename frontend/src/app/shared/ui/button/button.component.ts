import { Component, input, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './button.component.html',
  styleUrl: './button.component.scss',
})
export class ButtonComponent {
  readonly label = input<string>('');
  readonly icon = input<string>('');
  readonly color = input<'primary' | 'accent' | 'warn'>('primary');
  readonly variant = input<'basic' | 'raised' | 'flat' | 'stroked'>('raised');
  readonly type = input<'button' | 'submit'>('button');
  readonly disabled = input<boolean>(false);

  readonly clicked = output<Event>();
}
