import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SetDto {
  id: string;
  name: string;
  era: string;
  releaseDate: string | null;
  totalCardsMain: number;
  totalCardsMaster: number;
}

export interface CardDto {
  id: string;
  cardNumber: string;
  name: string;
  rarity: string;
  imageSmallUrl: string | null;
  imageLargeUrl: string | null;
  primaryType: string | null;
  artist: string | null;
  variants: CardVariantDto[];
}

export interface CardVariantDto {
  id: string;
  finish: string;
}

export interface OwnedCardDto {
  cardId: string;
  ownedCount: number;
}

export interface CollectionProfileRequest {
  name: string;
  collectionStyle: 'MAIN_SET' | 'MASTER_SET' | 'CUSTOM';

  includeNormal: boolean;
  includeHolo: boolean;
  includeReverseHolo: boolean;
  includeSpecialFinishes: boolean;

  includeCommon: boolean;
  includeUncommon: boolean;
  includeRare: boolean;

  includeMainCards: boolean;
  includeSecretCards: boolean;

  selectedSetIds: string[];
}

export interface CollectionProfileResponse extends CollectionProfileRequest {
  id: string;
  userId: string;
  createdAt: string;
  updatedAt: string;
}

@Injectable({ providedIn: 'root'})
export class ApiService {
  private baseUrl = "http://localhost:8080";

  constructor(private http: HttpClient) {}

  getSets(): Observable<SetDto[]> {
    return this.http.get<SetDto[]>(`${this.baseUrl}/sets`);
  }

  getCards(setId: string): Observable<CardDto[]> {
    return this.http.get<CardDto[]>(`${this.baseUrl}/sets/${setId}/cards`);
  }

  getOwnedCards(): Observable<OwnedCardDto[]> {
    return this.http.get<OwnedCardDto[]>(`${this.baseUrl}/user-cards`);
  }

  toggleOwned(cardId: string): Observable<OwnedCardDto> {
  return this.http.post<OwnedCardDto>(
    `${this.baseUrl}/user-cards/${cardId}/toggle`,
    {}
  );
  }

  getDemoCollectionProfile() {
    return this.http.get<CollectionProfileResponse | null>(
      `${this.baseUrl}/profiles/demo`
    );
  }

  saveDemoCollectionProfile(profile: CollectionProfileRequest) {
    return this.http.post<CollectionProfileResponse>(
      `${this.baseUrl}/profiles/demo`,
      profile
    );
  }
  
}
