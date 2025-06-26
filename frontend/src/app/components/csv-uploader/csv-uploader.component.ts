import { Component } from '@angular/core';
import { CsvProfilerService } from '../../services/csv-profiler.service';
import { ProfileResult } from '../../models/profile-result.model';

@Component({
  selector: 'app-csv-uploadelr',
  templateUrl: './csv-uploader.component.html',
  styleUrls: ['./csv-uploader.component.css']
})
export class CsvUploaderComponent {
  selectedFile: File | null = null;
  profileResults: ProfileResult[] = [];
  isLoading = false;
  errorMessage = '';
  
  displayedColumns: string[] = [
    'columnName', 
    'dataType', 
    'completeness', 
    'distinctCount', 
    'mean', 
    'stdDev', 
    'min', 
    'max', 
    'nullCount', 
    'totalCount'
  ];

  constructor(private csvProfilerService: CsvProfilerService) { }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file && file.type === 'text/csv') {
      this.selectedFile = file;
      this.errorMessage = '';
    } else {
      this.errorMessage = 'Veuillez sélectionner un fichier CSV valide.';
      this.selectedFile = null;
    }
  }

  onUpload(): void {
    if (!this.selectedFile) {
      this.errorMessage = 'Aucun fichier sélectionné.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.profileResults = [];

    this.csvProfilerService.profileCsv(this.selectedFile).subscribe({
      next: (results) => {
        this.profileResults = results;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur:', error);
        this.errorMessage = 'Erreur lors du profilage du fichier CSV.';
        this.isLoading = false;
      }
    });
  }

  formatNumber(value: number | undefined): string {
    if (value === undefined || value === null) {
      return 'N/A';
    }
    return Number(value).toFixed(2);
  }

  formatPercentage(value: number | undefined): string {
    if (value === undefined || value === null) {
      return 'N/A';
    }
    return (Number(value) * 100).toFixed(1) + '%';
  }
}