import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Livro } from '../model/livro.model';

@Injectable({
  providedIn: 'root'
})
export class LivroService {

  private url = `${environment.api}`;
  constructor(private httpClient: HttpClient) {
    this.getLivros();
  }

  getLivros() {
    return this.httpClient.get<Livro[]>(`${this.url}/livros`);
  }

  fb = inject(FormBuilder);
  http = inject(HttpClient);
  router = inject(Router);
    
  save(data: Livro): Observable<any> {
    return this.http.post<{ livro: Livro }>(`${this.url}/livros`, data);
  }

  
  findLivro(): Observable<any> {
    return this.http.get<{ livro: Livro }>(`${this.url}/livros`);
  }

  getLivroById(id: any): Observable<any> {
    return this.http.get<{ livro: Livro }>(`${this.url}/livros/${id}`);
  }

  update(livro: Livro): Observable<any> {
    return this.http.patch(`${this.url}/livros/${livro.codl}`, livro, {headers: { 'Content-Type': 'application/json-patch+json' }});
  }

  remove(livro: Livro): Observable<any> {
    return this.http.delete(`${this.url}/livros/${livro.codl}`);
  }

  saveCompleto(livroCompleto: any): Observable<any> {
  const codl = livroCompleto.codL;
  
  // Verificação passo a passo
  const cond1 = codl != null;
  const cond2 = codl !== '';
  const cond3 = codl !== undefined;
  
  const temIdValido = cond1 && cond2 && cond3;
  console.log('temIdValido:', temIdValido);
  
  if (temIdValido) {
    const { codl: id, ...livroSemId } = livroCompleto;
    return this.http.patch(
      `${this.url}/livros/${livroCompleto.codL}`
      , livroSemId
      , {headers: { 'Content-Type': 'application/json-patch+json' }}
    );
  } else {
    console.log('FAZENDO CREATE (ID inválido ou não existe)');
    return this.http.post(`${this.url}/livros`, livroCompleto);
  }
}
  getAutoresDisponiveis(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/autores`);
  }

  getAssuntosDisponiveis(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/assuntos`);
  }

}