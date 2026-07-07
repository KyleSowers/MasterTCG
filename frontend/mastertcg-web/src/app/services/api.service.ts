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
}
