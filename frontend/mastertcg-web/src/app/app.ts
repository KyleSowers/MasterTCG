import { Component, OnInit } from '@angular/core';
import { ApiService, SetDto, CardDto, OwnedCardDto } from './services/api.service';
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
  ownedCards: OwnedCardDto[] = [];
  
  loading = true;
  error: string | null = null;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.api.getSets().subscribe({
      next: (data) => {
        this.sets = data;
        this.error = null;
        this.loading = false;

        this.loadOwnedCards();
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
        return 'Normal';
      case 'REVERSE_HOLO':
        return 'Reverse Holo';
      case 'HOLO':
        return 'Holo';
      default:
        return finish.replace('_', ' ');
    }
  }

  loadOwnedCards() {
  this.api.getOwnedCards().subscribe({
    next: (data) => {
      this.ownedCards = data;
    },
    error: (err) => {
      console.error(err);
    }
  });
  }

  isOwned(cardId: string): boolean {
    return this.ownedCards.some(oc => oc.cardId === cardId && oc.ownedCount > 0);
  }

  toggleOwned(cardId: string) {
    this.api.toggleOwned(cardId).subscribe({
      next: (updated) => {
        this.ownedCards = this.ownedCards.filter(oc => oc.cardId !== updated.cardId);

        if (updated.ownedCount > 0) {
          this.ownedCards.push(updated);
        }
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  getTotalVariantCount(): number {
  return this.cards.reduce((total, card) => total + card.variants.length, 0);
  }

  getOwnedCount(): number {
    return this.cards.reduce((total, card) => {
      return total + card.variants.filter(v => this.isOwned(v.id)).length;
    }, 0);
  }

  getCompletionPercentage(): number {
    const total = this.getTotalVariantCount();

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedCount() / total) * 100);
  }

  getRarityLabel(rarity: string): string {
    switch (rarity) {
      case 'COMMON':
        return '● Common';
      case 'UNCOMMON':
        return '◆ Uncommon';
      case 'RARE':
        return '★ Rare';
      default:
        return rarity;
    }
  }

}
