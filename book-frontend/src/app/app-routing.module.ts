import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AssuntoComponent } from './component/assunto/assunto.component';
import { AutorComponent } from './component/autor/autor.component';
import { LivroComponent } from './component/livro/livro.component';
import { RelatorioComponent } from './component/relatorio/relatorio.component';


const routes: Routes = [
  { path: '', redirectTo: 'livro', pathMatch: 'full' },
  { path: 'livro', component: LivroComponent },
  { path: 'autor', component: AutorComponent },
  { path: 'assunto', component: AssuntoComponent },
  { path: 'relatorio', component: RelatorioComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
