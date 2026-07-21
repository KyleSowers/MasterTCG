import { Component, OnInit } from '@angular/core';
import { ApiService, SetDto, CardDto, OwnedCardDto, CardVariantDto } from './services/api.service';
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
  selectedFinish = 'ALL';
  selectedOwnership = 'ALL';
  selectedRarity = 'ALL';
  selectedSet: SetDto | null = null;
  sets: SetDto[] = [];
  setCardsBySetId: { [setId: string]: CardDto[] } = {};
  setCardBackBackground =
  'radial-gradient(circle at 25% 50%, rgba(255, 255, 255, 0.35) 0 8%, transparent 9%), linear-gradient(90deg, #2563eb, #facc15)';
  trackedFinishes = [
    'NORMAL',
    'HOLO',
    'REVERSE_HOLO',
    'COSMOS_HOLO',
    'CRACKED_ICE_HOLO',
    'SHATTER_HOLO',
    'MIRROR_HOLO',
    'POKE_BALL',
    'MASTER_BALL'
  ];
  
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

  getAvailableFinishes(): string[] {
    return this.trackedFinishes.filter(finish =>
      this.getTotalVariantCountByFinish(finish) > 0
    );
  }

  getCardsForSet(set: SetDto): CardDto[] {
    return this.setCardsBySetId[set.id] ?? [];
  }
  
  getCardStatus(card: CardDto): 'COMPLETE' | 'PARTIAL' | 'MISSING' {
    const ownedCount = this.getOwnedVariantCountForCard(card);
    const totalCount = this.getTotalVariantCountForCard(card);

    if (totalCount === 0 || ownedCount === 0) {
      return 'MISSING';
    }

    if (ownedCount === totalCount) {
      return 'COMPLETE';
    }

    return 'PARTIAL';
  }

  getCardStatusBadgeClass(card: CardDto): string {
    switch (this.getCardStatus(card)) {
      case 'COMPLETE':
        return 'status-complete';
      case 'PARTIAL':
        return 'status-partial';
      case 'MISSING':
        return 'status-missing';
    }
  }

  getCardStatusClass(card: CardDto): string {
    switch (this.getCardStatus(card)) {
      case 'COMPLETE':
        return 'card-complete';
      case 'PARTIAL':
        return 'card-partial';
      case 'MISSING':
        return 'card-missing';
    }
  }

  getCardStatusLabel(card: CardDto): string {
    const ownedCount = this.getOwnedVariantCountForCard(card);
    const totalCount = this.getTotalVariantCountForCard(card);

    if (totalCount === 0 || ownedCount === 0) {
      return 'Missing';
    }

    if (ownedCount === totalCount) {
      return 'Complete';
    }

    return `${ownedCount}/${totalCount}`;
  }

  getCompletionPercentage(): number {
    const total = this.getTotalVariantCount();

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedCount() / total) * 1000) / 10;
  }

  getCompletionPercentageByFinish(finish: string): number {
    const total = this.getTotalVariantCountByFinish(finish);

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedVariantCountByFinish(finish) / total) * 1000) / 10;
  }

  getCompletionPercentageByRarity(rarity: string): number {
    const total = this.getTotalVariantCountByRarity(rarity);

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedVariantCountByRarity(rarity) / total) * 1000) / 10;
  }

  getCompletionPercentageForSet(set: SetDto): number {
    const total = this.getTotalVariantCountForSet(set);

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedCountForSet(set) / total) * 1000) / 10;
  }

  getFilterBarPercentage(): number {
    const total = this.getFilteredBaseVariantCount();

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getFilteredOwnedVariantCount() / total) * 1000) / 10;
  }

  getFilteredBaseVariantCount(): number {
    const term = this.searchTerm.trim().toLowerCase();

    return this.cards
      .filter(card => {
        const matchesSearch = 
          !term ||
          card.name.toLowerCase().startsWith(term) ||
          card.cardNumber.toLowerCase() === term ||
          card.rarity.toLowerCase() === term ||
          card.primaryType?.toLowerCase() === term ||
          card.artist?.toLowerCase() === term;

        const matchesRarity = 
          this.selectedRarity === 'ALL' ||
          card.rarity === this.selectedRarity;

        return matchesSearch && matchesRarity;
      })
      .reduce((total, card) => {
        const matchingVariants = card.variants.filter(variant =>
          this.selectedFinish === 'ALL' || variant.finish === this.selectedFinish
        );

        return total + matchingVariants.length;
      }, 0);
  }

  getFilteredCards(): CardDto[] {
    const term = this.searchTerm.trim().toLowerCase();

    return this.cards.filter(card => {
      const matchesSearch =
        !term ||
        card.name.toLowerCase().startsWith(term) ||
        card.cardNumber.toLowerCase() === term ||
        card.rarity.toLowerCase() === term ||
        card.primaryType?.toLowerCase() === term ||
        card.artist?.toLowerCase() === term;

      const matchesRarity =
        this.selectedRarity === 'ALL' ||
        card.rarity === this.selectedRarity;

      const hasVisibleVariants = this.getVisibleVariants(card).length > 0;

      return matchesSearch && matchesRarity && hasVisibleVariants;
    });
  }

  getFilteredMissingVariantCount(): number {
    return this.getFilteredBaseVariantCount() - this.getFilteredOwnedVariantCount();
  }

  getFilteredOwnedVariantCount(): number {
    const term = this.searchTerm.trim().toLowerCase();

    return this.cards
      .filter(card => {
        const matchesSearch =
          !term ||
          card.name.toLowerCase().startsWith(term) ||
          card.cardNumber.toLowerCase() === term ||
          card.rarity.toLowerCase() === term ||
          card.primaryType?.toLowerCase() === term ||
          card.artist?.toLowerCase() === term;

        const matchesRarity =
          this.selectedRarity === 'ALL' ||
          card.rarity === this.selectedRarity;

        return matchesSearch && matchesRarity;
      })
      .reduce((total, card) => {
        return total + card.variants.filter(variant => {
          const matchesFinish =
            this.selectedFinish === 'ALL' ||
            variant.finish === this.selectedFinish;

          return matchesFinish && this.isOwned(variant.id);
        }).length;
      }, 0);
  }

  getOwnedCount(): number {
    return this.cards.reduce((total, card) => {
      return total + card.variants.filter(v => this.isOwned(v.id)).length;
    }, 0);
  }

  getOwnedCountForSet(set: SetDto): number {
    return this.getCardsForSet(set).reduce((total, card) => {
      return total + card.variants.filter(v => this.isOwned(v.id)).length;
    }, 0);
  }

  getOwnedVariantCountByFinish(finish: string): number {
    return this.cards.reduce((total, card) => {
      return total + card.variants.filter(v =>
        v.finish === finish && this.isOwned(v.id)
      ).length;
    }, 0)
  }

  getOwnedVariantCountByRarity(rarity: string): number {
    return this.cards
      .filter(card => card.rarity === rarity)
      .reduce((total, card) => {
        return total + card.variants.filter(v => this.isOwned(v.id)).length;
      }, 0);
  }

  getOwnedVariantCountForCard(card: CardDto): number {
    return card.variants.filter(variant => this.isOwned(variant.id)).length;
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

  getSetBackgroundImage(set: SetDto): string {
    const cards = this.getCardsForSet(set);
    const imageUrl = cards[0]?.imageLargeUrl ?? cards[0]?.imageSmallUrl;

    if (!imageUrl) {
      return `
        linear-gradient(rgba(255, 255, 255, 0.82), rgba(255, 255, 255, 0.82)),
        ${this.setCardBackBackground}
      `;
    }

    return `
      linear-gradient(rgba(255, 255, 255, 0.78), rgba(255, 255, 255, 0.78)),
      url("${imageUrl}"),
      ${this.setCardBackBackground}
    `;
  }

  getSetCompletionClass(set: SetDto): string {
    const status = this.getSetCompletionStatus(set);

    switch (status) {
      case 'COMPLETE':
        return 'set-status-complete';
      case 'IN_PROGRESS':
        return 'set-status-progress';
      case 'NOT_STARTED':
        return 'set-status-empty';
    }
  }

  getSetCompletionLabel(set: SetDto): string {
    const status = this.getSetCompletionStatus(set);

    switch (status) {
      case 'COMPLETE':
        return 'Complete';
      case 'IN_PROGRESS':
        return 'In Progress';
      case 'NOT_STARTED':
        return 'Not Started';
    }
  }

  getSetCompletionStatus(set: SetDto): 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETE' {
    const owned = this.getOwnedCountForSet(set);
    const total = this.getTotalVariantCountForSet(set);

    if (total === 0 || owned === 0) {
      return 'NOT_STARTED';
    }

    if (owned === total) {
      return 'COMPLETE';
    }

    return 'IN_PROGRESS';
  }

  getTotalVariantCountByRarity(rarity: string): number {
    return this.cards
      .filter(card => card.rarity === rarity)
      .reduce((total, card) => total + card.variants.length, 0);
  }

  getTotalVariantCountByFinish(finish: string): number {
    return this.cards.reduce((total, card) => {
      return total + card.variants.filter(v => v.finish === finish).length;
    }, 0);
  }

  getTotalVariantCountForCard(card: CardDto): number {
    return card.variants.length;
  }

  getTotalVariantCountForSet(set: SetDto): number {
    return this.getCardsForSet(set).reduce((total, card) => {
      return total + card.variants.length;
    }, 0);
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

  getVisibleCardCount(): number {
    return this.getFilteredCards().length;
  }

  getVisibleOwnedCount(): number {
    return this.getFilteredCards().reduce((total, card) => {
      return total + this.getVisibleVariants(card).filter(v => this.isOwned(v.id)).length;
    }, 0);
  }

  getVisibleVariantCount(): number {
    return this.getFilteredCards().reduce((total, card) => {
      return total + this.getVisibleVariants(card).length;
    }, 0);
  }

  getVisibleVariants(card: CardDto): CardVariantDto[] {
    return card.variants.filter(variant => {
      const matchesFinish = 
        this.selectedFinish === 'ALL' || variant.finish === this.selectedFinish;

      const matchesOwnership =
      this.selectedOwnership === 'ALL' ||
      (this.selectedOwnership === 'OWNED' && this.isOwned(variant.id)) ||
      (this.selectedOwnership === 'MISSING' && !this.isOwned(variant.id));

      return matchesFinish && matchesOwnership;
    });
  }

  isOwned(cardId: string): boolean {
    return this.ownedCards.some(oc => oc.cardId === cardId && oc.ownedCount > 0);
  }

  loadCards(set: SetDto) {
    this.selectedSet = set;

    const cachedCards = this.setCardsBySetId[set.id];

    if (cachedCards) {
      this.cards = cachedCards;
      return;
    }

    this.api.getCards(set.id).subscribe({
      next: (data) => {
        this.cards = data;
        this.setCardsBySetId[set.id] = data;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  loadDashboardCards() {
    this.sets.forEach(set => {
      this.api.getCards(set.id).subscribe({
        next: (data) => {
          this.setCardsBySetId[set.id] = data;
        },
        error: (err) => {
          console.error(err);
        }
      });
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
        this.sets = this.sortSetsByReleaseDate(data);
        this.error = null;
        this.loading = false;

        this.loadOwnedCards();
        this.loadDashboardCards();
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to load sets';
        this.loading = false;
      }
    });
  }

  sortSetsByReleaseDate(sets: SetDto[]): SetDto[] {
    return [...sets].sort((a, b) => {
      const dateA = a.releaseDate ?? '9999-12-31';
      const dateB = b.releaseDate ?? '9999-12-31';

      return dateA.localeCompare(dateB);
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
