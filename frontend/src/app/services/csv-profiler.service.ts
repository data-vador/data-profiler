import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProfileResult } from '../models/profile-result.model';

@Injectable({
  providedIn: 'root'
})
export class CsvProfilerService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  profileCsv(file: File): Observable<ProfileResult[]> {
    const formData = new FormData();
    formData.append('file', file);
    
    return this.http.post<ProfileResult[]>(`${this.apiUrl}/profile`, formData);
  }

  checkHealth(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }
}