import { Component, OnInit } from '@angular/core';
import { ApiService, SetDto, CardDto, OwnedCardDto } from './services/api.service';
import {CommonModule} from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App implements OnInit {
  cards: CardDto[] = [];
  ownedCards: OwnedCardDto[] = [];
  searchTerm = '';
  selectedOwnership = 'ALL';
  selectedRarity = 'ALL';
  selectedSet: SetDto | null = null;
  sets: SetDto[] = [];
  
  loading = true;
  error: string | null = null;

  constructor(private api: ApiService) {}

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

  getCompletionPercentage(): number {
    const total = this.getTotalVariantCount();

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedCount() / total) * 100);
  }

  getFilteredCards(): CardDto[] {
    const term = this.searchTerm.trim().toLowerCase();

    return this.cards.filter(card => {
      const matchesSearch =
        !term ||
        card.name.toLowerCase().startsWith(term) ||
        card.cardNumber.toLowerCase() === term ||
        card.rarity.toLowerCase() === term ||
        (card.primaryType?.toLowerCase() === term) ||
        (card.artist?.toLowerCase() === term)

      const matchesRarity = 
        this.selectedRarity === 'ALL' ||
        card.rarity === this.selectedRarity;

      const isOwned = card.variants.some(variant =>
          this.isOwned(variant.id)
        );

      const matchesOwnership = 
      this.selectedOwnership === 'ALL' ||
      (this.selectedOwnership === 'OWNED' && isOwned) ||
      (this.selectedOwnership === 'MISSING' && !isOwned);

      return matchesSearch && matchesRarity && matchesOwnership;
    });
  }

  getOwnedCount(): number {
    return this.cards.reduce((total, card) => {
      return total + card.variants.filter(v => this.isOwned(v.id)).length;
    }, 0);
  }

  getRarityClass(rarity: string): string {
    switch (rarity) {
      case 'COMMON':
        return 'rarity-common';
      case 'UNCOMMON':
        return 'rarity-uncommon';
      case 'RARE':
        return 'rarity-rare';
      default:
        return 'rarity-default';
    }
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

  getTypeClass(type: string | null): string {
    if (!type) {
      return 'type-default';
    }

    return `type-${type.toLowerCase()}`;
  }

  getTotalVariantCount(): number {
  return this.cards.reduce((total, card) => total + card.variants.length, 0);
  }

  isOwned(cardId: string): boolean {
    return this.ownedCards.some(oc => oc.cardId === cardId && oc.ownedCount > 0);
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

}
