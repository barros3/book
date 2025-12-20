import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Assunto } from '../model/assunto.model';

@Injectable({
  providedIn: 'root'
})
export class AssuntoService {

  private url = `${environment.api}/assuntos`;
  constructor(private httpClient: HttpClient) {
    this.getAssuntos();
  }

  getAssuntos() {
    return this.httpClient.get<Assunto[]>(this.url);
  }

  fb = inject(FormBuilder);
  http = inject(HttpClient);
  router = inject(Router);
    
  save(data: Assunto): Observable<any> {
    return this.http.post<{ assunto: Assunto }>(this.url, data);
  }

  findAssunto(): Observable<any> {
    return this.http.get<{ assunto: Assunto }>(this.url);
  }

  getAssuntoId(id: any): Observable<any> {
    return this.http.get<{ autor: Assunto }>(`${this.url}/${id}`).pipe(
      map((data: any) => this.normalizeAssunto(data))
    );
  }

  update(assunto: Assunto): Observable<any> {
    const id = assunto.codAs;

    return this.http.patch(`${this.url}/${id}`, assunto, {headers: { 'Content-Type': 'application/json-patch+json' }});
  }

  remove(assunto: Assunto): Observable<any> {
    return this.http.delete(`${this.url}/${assunto.codAs}`);
  }

  normalizeAssunto(data: any): Assunto {
    return {
      codAs: data.codas || data.codAs,
      descricao: data.descricao
    };
  }

}