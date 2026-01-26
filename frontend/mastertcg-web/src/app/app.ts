import { Component, OnInit } from '@angular/core';
import { ApiService, SetDto } from './services/api.service';
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
  loading = true;
  error: string | null = null;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.api.getSets().subscribe({
      next: (data) => {
        this.error = 'Failed to load sets';
        this.loading = false;
      }
    });
  }
}
