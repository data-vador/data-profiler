import { Component, OnInit } from '@angular/core';
import { CsvProfilerService } from './services/csv-profiler.service';

@Component({
  selector: 'app-root',
  template: `
    <div class="app-container">
      <mat-toolbar color="primary">
        <span>CSV Profiler avec Deequ</span>
        <span class="spacer"></span>
        <mat-icon *ngIf="isHealthy" class="health-icon">check_circle</mat-icon>
        <span *ngIf="isHealthy" class="health-text">Service opérationnel</span>
      </mat-toolbar>
      
      <main>
        <app-csv-uploader></app-csv-uploader>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
      background-color: #f5f5f5;
    }
    
    .spacer {
      flex: 1 1 auto;
    }
    
    .health-icon {
      color: #4caf50;
      margin-right: 8px;
    }
    
    .health-text {
      color: #4caf50;
      font-size: 14px;
    }
    
    main {
      padding: 20px;
    }
  `]
})
export class AppComponent implements OnInit {
  isHealthy = false;

  constructor(private csvProfilerService: CsvProfilerService) {}

  ngOnInit(): void {
    this.checkServiceHealth();
  }

  checkServiceHealth(): void {
    this.csvProfilerService.checkHealth().subscribe({
      next: () => {
        this.isHealthy = true;
      },
      error: () => {
        this.isHealthy = false;
      }
    });
  }
}