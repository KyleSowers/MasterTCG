import { Component, OnInit } from '@angular/core';
import { ApiService, SetDto, CardDto } from './services/api.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App implements OnInit {
  sets: SetDto[] = [];
  cards: CardDto[] = [];
  selectedSet: SetDto | null = null;
  
  loading = true;
  error: string | null = null;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.api.getSets().subscribe({
      next: (data) => {
        this.sets = data;
        this.error = null;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to load sets';
        this.loading = false;
      }
    });
  }

  loadCards(set: SetDto) {
    this.selectedSet = set;
  
    this.api.getCards(set.id).subscribe({
      next: (data) => {
        this.cards = data;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  displayFinish(finish: string): string {
    switch (finish) {
      case 'NORMAL':
        return '';

      case 'REVERSE_HOLO':
        return 'R-HOLO';

      default:
        return finish.replace('_', ' ');
    }
  }
}
