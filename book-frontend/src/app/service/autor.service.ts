import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Autor } from '../model/autor.model';

@Injectable({
  providedIn: 'root'
})
export class AutorService {

  private url = `${environment.api}/autores`;
  constructor(private httpClient: HttpClient) {
    this.getAutors();
  }

  getAutors() {
    return this.httpClient.get<Autor[]>(this.url);
  }

  fb = inject(FormBuilder);
  http = inject(HttpClient);
  router = inject(Router);
    
  save(data: Autor): Observable<any> {
    return this.http.post<{ autor: Autor }>(this.url, data);
  }

  findAutor(): Observable<any> {
    return this.http.get<{ autor: Autor }>(this.url);
  }

  getByAutorId(id: any): Observable<any> {
    return this.http.get<{ autor: Autor }>(`${this.url}/${id}`);
  }

  update(autor: Autor): Observable<any> {
    return this.http.patch(`${this.url}/${autor.codAu}`, autor, {headers: { 'Content-Type': 'application/json-patch+json' }});
  }

  remove(autor: Autor): Observable<any> {
    return this.http.delete(`${this.url}/${autor.codAu}`);
  }

}