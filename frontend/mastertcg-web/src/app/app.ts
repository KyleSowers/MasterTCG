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
  activePage: 'COLLECTION' | 'PROFILE_BUILDER' = 'COLLECTION';
  cards: CardDto[] = [];
  collectionProfileName = 'My Collection Profile';
  collectionScope = {
    includeNormal: true,
    includeHolo: true,
    includeReverseHolo: true,
    includeSpecialFinishes: true,

    includeCommon: true,
    includeUncommon: true,
    includeRare: true,

    includeMainCards: true,
    includeSecretCards: true
  };
  ownedCards: OwnedCardDto[] = [];
  searchTerm = '';
  selectedFinish = 'ALL';
  selectedOwnership = 'ALL';
  selectedProfileSetIds: string[] = [];
  selectedRarity = 'ALL';
  selectedSet: SetDto | null = null;
  // includeReverseHolosInCompletion = true;
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

  // ---------------------------------------------------------------------------
  // APPLICATION INITIALIZATION
  // ---------------------------------------------------------------------------
ngOnInit() {
    this.api.getSets().subscribe({
      next: (data) => {
        this.sets = this.sortSetsByReleaseDate(data);
        // this.selectedProfileSetIds = sets.map(set => set.id);
        this.selectedProfileSetIds = this.sets.map((set: SetDto) => set.id);

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

sortSetsByReleaseDate(sets: SetDto[]): SetDto[] {
    return [...sets].sort((a, b) => {
      const dateA = a.releaseDate ?? '9999-12-31';
      const dateB = b.releaseDate ?? '9999-12-31';

      return dateA.localeCompare(dateB);
    });
  }

  // ---------------------------------------------------------------------------
  // PAGE NAVIGATION
  // ---------------------------------------------------------------------------
showCollectionPage() {
    this.activePage = 'COLLECTION';
  }

showProfileBuilderPage() {
    this.activePage = 'PROFILE_BUILDER';
  }

  // ---------------------------------------------------------------------------
  // SET DASHBOARD
  // ---------------------------------------------------------------------------
getProfileSets(): SetDto[] {
    return this.sets.filter(set => this.isSetInCollectionProfile(set));
  }

getCardsForSet(set: SetDto): CardDto[] {
    return this.setCardsBySetId[set.id] ?? [];
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

getSetCompletionClass(set: SetDto): string {
    const owned = this.getOwnedCountForSet(set);
    const total = this.getTotalVariantCountForSet(set);

    if (total === 0 || owned === 0) {
      return 'set-missing';
    }

    if (owned === total) {
      return 'set-complete';
    }

    return 'set-partial';
  }

getOwnedCountForSet(set: SetDto): number {
    return this.getCardsForSet(set).reduce((total, card) => {
      return total + this.getCollectionScopeVariantsForCard(card, set)
        .filter(variant => this.isOwned(variant.id))
        .length;
    }, 0);
  }

getTotalVariantCountForSet(set: SetDto): number {
    return this.getCardsForSet(set).reduce((total, card) => {
      return total + this.getCollectionScopeVariantsForCard(card, set).length;
    }, 0);
  }

getCompletionPercentageForSet(set: SetDto): number {
    const total = this.getTotalVariantCountForSet(set);

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedCountForSet(set) / total) * 1000) / 10;
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

  // ---------------------------------------------------------------------------
  // SELECTED COLLECTION PROGRESS
  // ---------------------------------------------------------------------------
getOwnedCount(): number {
    return this.cards.reduce((total, card) => {
      return total + this.getCollectionScopeVariantsForCard(card)
        .filter(variant => this.isOwned(variant.id))
        .length;
    }, 0);
  }

getTotalVariantCount(): number {
    return this.cards.reduce((total, card) => {
      return total + this.getCollectionScopeVariantsForCard(card).length;
    }, 0);
  }

getCompletionPercentage(): number {
    const total = this.getTotalVariantCount();

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedCount() / total) * 1000) / 10;
  }

  // ---------------------------------------------------------------------------
  // SEARCH AND FILTER CONTROLS
  // ---------------------------------------------------------------------------
getAvailableRarities(): string[] {
    const rarityOrder = ['COMMON', 'UNCOMMON', 'RARE'];

    return rarityOrder.filter(rarity =>
      this.cards.some(card =>
        card.rarity === rarity &&
        this.isCardRarityInCollectionScope(card)
      )
    );
  }

getAvailableFinishes(): string[] {
    return this.trackedFinishes.filter(finish =>
      this.cards.some(card =>
        card.variants.some(variant =>
          variant.finish === finish &&
          this.isVariantInCollectionScope(card, variant)
        )
      )
    );
  }

displayRarity(rarity: string): string {
    switch (rarity) {
      case 'COMMON':
        return 'Common';
      case 'UNCOMMON':
        return 'Uncommon';
      case 'RARE':
        return 'Rare';
      default:
        return rarity;
    }
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

      const hasVisibleVariants =
        this.getVisibleVariants(card).length > 0;

      return matchesSearch && matchesRarity && hasVisibleVariants;
    });
  }

getVisibleCardCount(): number {
    return this.getFilteredCards().length;
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
        const matchingVariants = card.variants.filter(variant => {
          const matchesScope =
            this.isVariantInCollectionScope(card, variant);

          const matchesFinish =
            this.selectedFinish === 'ALL' ||
            variant.finish === this.selectedFinish;

          return matchesScope && matchesFinish;
        });

        return total + matchingVariants.length;
      }, 0);
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
      const matchingOwnedVariants = card.variants.filter(variant => {
        const matchesScope =
          this.isVariantInCollectionScope(card, variant);

        const matchesFinish =
          this.selectedFinish === 'ALL' ||
          variant.finish === this.selectedFinish;

        return matchesScope && matchesFinish && this.isOwned(variant.id);
      });

      return total + matchingOwnedVariants.length;
    }, 0);
  }

getFilteredMissingVariantCount(): number {
    return this.getFilteredBaseVariantCount() - this.getFilteredOwnedVariantCount();
  }

getFilterBarPercentage(): number {
    const total = this.getFilteredBaseVariantCount();

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getFilteredOwnedVariantCount() / total) * 1000) / 10;
  }

getVisibleVariants(card: CardDto): CardVariantDto[] {
    return card.variants.filter(variant => {
      const matchesScope =
        this.isVariantInCollectionScope(card, variant);

      const matchesFinish =
        this.selectedFinish === 'ALL' || variant.finish === this.selectedFinish;

      const matchesOwnership =
        this.selectedOwnership === 'ALL' ||
        (this.selectedOwnership === 'OWNED' && this.isOwned(variant.id)) ||
        (this.selectedOwnership === 'MISSING' && !this.isOwned(variant.id));

      return matchesScope && matchesFinish && matchesOwnership;
    });
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

  // ---------------------------------------------------------------------------
  // STATISTICS SIDEBAR
  // ---------------------------------------------------------------------------
getOwnedVariantCountByRarity(rarity: string): number {
    return this.cards
      .filter(card => card.rarity === rarity)
      .reduce((total, card) => {
        return total + this.getCollectionScopeVariantsForCard(card)
          .filter(variant => this.isOwned(variant.id))
          .length;
      }, 0);
  }

getTotalVariantCountByRarity(rarity: string): number {
    return this.cards
      .filter(card => card.rarity === rarity)
      .reduce((total, card) => {
        return total + this.getCollectionScopeVariantsForCard(card).length;
      }, 0);
  }

getCompletionPercentageByRarity(rarity: string): number {
    const total = this.getTotalVariantCountByRarity(rarity);

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedVariantCountByRarity(rarity) / total) * 1000) / 10;
  }

getOwnedVariantCountByFinish(finish: string): number {
    return this.cards.reduce((total, card) => {
      return total + this.getCollectionScopeVariantsForCard(card)
        .filter(variant =>
          variant.finish === finish &&
          this.isOwned(variant.id)
        )
        .length;
    }, 0);
  }

getTotalVariantCountByFinish(finish: string): number {
    return this.cards.reduce((total, card) => {
      return total + this.getCollectionScopeVariantsForCard(card)
        .filter(variant => variant.finish === finish)
        .length;
    }, 0);
  }

getCompletionPercentageByFinish(finish: string): number {
    const total = this.getTotalVariantCountByFinish(finish);

    if (total === 0) {
      return 0;
    }

    return Math.round((this.getOwnedVariantCountByFinish(finish) / total) * 1000) / 10;
  }

  // ---------------------------------------------------------------------------
  // CARD DISPLAY AND STATUS
  // ---------------------------------------------------------------------------
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

getOwnedVariantCountForCard(card: CardDto): number {
    return this.getCollectionScopeVariantsForCard(card)
      .filter(variant => this.isOwned(variant.id))
      .length;
  }

getTotalVariantCountForCard(card: CardDto): number {
    return this.getCollectionScopeVariantsForCard(card).length;
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

getTypeClass(type: string | null): string {
    if (!type) {
      return 'type-default';
    }

    return `type-${type.toLowerCase()}`;
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

  // ---------------------------------------------------------------------------
  // COLLECTION PROFILE BUILDER
  // ---------------------------------------------------------------------------
onCollectionScopeChanged() {
    const availableFinishes = this.getAvailableFinishes();

    if (
      this.selectedFinish !== 'ALL' &&
      !availableFinishes.includes(this.selectedFinish)
    ) {
      this.selectedFinish = 'ALL';
    }

    const availableRarities = this.getAvailableRarities();

    if (
      this.selectedRarity !== 'ALL' &&
      !availableRarities.includes(this.selectedRarity)
    ) {
      this.selectedRarity = 'ALL';
    }
  }

isSetInCollectionProfile(set: SetDto): boolean {
    return this.selectedProfileSetIds.includes(set.id);
  }

toggleProfileSet(set: SetDto, event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;

    if (checked) {
      if (!this.selectedProfileSetIds.includes(set.id)) {
        this.selectedProfileSetIds = [...this.selectedProfileSetIds, set.id];
      }
    } else {
      this.selectedProfileSetIds = this.selectedProfileSetIds.filter(
        setId => setId !== set.id
      );

      if (this.selectedSet?.id === set.id) {
        this.selectedSet = null;
        this.cards = [];
      }
    }
  }

getAvailableEras(): string[] {
  return Array.from(new Set(this.sets.map(set => set.era)));
}

getSetsForEra(era: string): SetDto[] {
  return this.sets.filter(set => set.era === era);
}

isEraFullySelected(era: string): boolean {
  const eraSets = this.getSetsForEra(era);

  return eraSets.length > 0 && eraSets.every(set =>
    this.isSetInCollectionProfile(set)
  );
}

isEraPartiallySelected(era: string): boolean {
  const eraSets = this.getSetsForEra(era);

  const selectedCount = eraSets.filter(set =>
    this.isSetInCollectionProfile(set)
  ).length;

  return selectedCount > 0 && selectedCount < eraSets.length;
}

toggleProfileEra(era: string, event: Event): void {
  const checked = (event.target as HTMLInputElement).checked;
  const eraSets = this.getSetsForEra(era);
  const eraSetIds = eraSets.map(set => set.id);

  if (checked) {
    const combinedSetIds = new Set([
      ...this.selectedProfileSetIds,
      ...eraSetIds
    ]);

    this.selectedProfileSetIds = Array.from(combinedSetIds);
  } else {
    this.selectedProfileSetIds = this.selectedProfileSetIds.filter(
      setId => !eraSetIds.includes(setId)
    );

    if (
      this.selectedSet &&
      eraSetIds.includes(this.selectedSet.id)
    ) {
      this.selectedSet = null;
      this.cards = [];
    }
  }
}

  // ---------------------------------------------------------------------------
  // COLLECTION SCOPE HELPERS
  // ---------------------------------------------------------------------------

getNumericCardNumber(cardNumber: string): number | null {
    const match = cardNumber.match(/\d+/);

    if (!match) {
      return null;
    }

    return Number(match[0]);
  }

isSecretCardForSet(card: CardDto, set: SetDto | null = this.selectedSet): boolean {
    if (!set?.totalCardsMain) {
      return false;
    }

    const numericCardNumber = this.getNumericCardNumber(card.cardNumber);

    if (numericCardNumber === null) {
      return false;
    }

    return numericCardNumber > set.totalCardsMain;
  }

isCardCatalogInCollectionScope(card: CardDto, set: SetDto | null = this.selectedSet): boolean {
    const isSecret = this.isSecretCardForSet(card, set);

    if (isSecret) {
      return this.collectionScope.includeSecretCards;
    }

    return this.collectionScope.includeMainCards;
  }

// isCardCatalogInCollectionScope(card: CardDto, set: SetDto | null = this.selectedSet): boolean {
//   return true;
// }

getCollectionScopeVariantsForCard(
    card: CardDto,
    set: SetDto | null = this.selectedSet
  ): CardVariantDto[] {
    return card.variants.filter(variant =>
      this.isVariantInCollectionScope(card, variant, set)
    );
  }

isCardRarityInCollectionScope(card: CardDto): boolean {
    switch (card.rarity) {
      case 'COMMON':
        return this.collectionScope.includeCommon;
      case 'UNCOMMON':
        return this.collectionScope.includeUncommon;
      case 'RARE':
        return this.collectionScope.includeRare;
      default:
        return true;
    }
  }

isVariantInCollectionScope(
    card: CardDto,
    variant: CardVariantDto,
    set: SetDto | null = this.selectedSet
  ): boolean {
    if (!this.isCardCatalogInCollectionScope(card, set)) {
      return false;
    }

    if (!this.isCardRarityInCollectionScope(card)) {
      return false;
    }

    switch (variant.finish) {
      case 'NORMAL':
        return this.collectionScope.includeNormal;
      case 'HOLO':
        return this.collectionScope.includeHolo;
      case 'REVERSE_HOLO':
        return this.collectionScope.includeReverseHolo;
      default:
        return this.collectionScope.includeSpecialFinishes;
    }
  }


  // ---------------------------------------------------------------------------
  // OWNERSHIP HELPERS
  // ---------------------------------------------------------------------------
isOwned(cardId: string): boolean {
    return this.ownedCards.some(oc => oc.cardId === cardId && oc.ownedCount > 0);
  }

}
