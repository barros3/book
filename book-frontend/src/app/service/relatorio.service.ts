import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Relatorio } from '../model/relatorio.model';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {

  private url = `${environment.api}/relatorio-geral`;
  constructor(private httpClient: HttpClient) {
    this.getRelatorios();
  }

  getRelatorios() {
    return this.httpClient.get<Relatorio[]>(this.url);
  }

  fb = inject(FormBuilder);
  http = inject(HttpClient);
  router = inject(Router);
    
  gerarRelatorio(): Observable<Blob> {
    return this.http.get(`${this.url}/download`, {
      responseType: 'blob'
    });
  }
}