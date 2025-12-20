import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpClientModule } from '@angular/common/http';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AssuntoComponent } from './component/assunto/assunto.component';
import { AutorComponent } from './component/autor/autor.component';
import { LivroComponent } from './component/livro/livro.component';
import { ModalLivroComponent } from './component/livro/modal.livro.component';
import { RelatorioComponent } from './component/relatorio/relatorio.component';
import { ModalerrorComponent } from './modalerror/modalerror.component';

import { ModalAssuntoComponent } from './component/assunto/modal.assunto.component';
import { ModalAutorComponent } from './component/autor/modal.autor.component';
import { FilterPipe } from './filter.pipe';
import { RelatorioPipe } from './relatorio.pipe';

@NgModule({
  declarations: [
    AppComponent,
    AssuntoComponent,
    ModalAssuntoComponent,
    AutorComponent,
    ModalAutorComponent,
    LivroComponent,
    ModalLivroComponent,
    RelatorioComponent,
    ModalerrorComponent,
    FilterPipe,
    RelatorioPipe
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    MatSlideToggleModule,
    MatDialogModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    MatFormFieldModule,
  ],
  providers: [Router],
  bootstrap: [AppComponent]
})
export class AppModule { }
