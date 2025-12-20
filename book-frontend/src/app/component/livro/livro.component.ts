import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ModalerrorComponent } from 'src/app/modalerror/modalerror.component';
import { ActionType } from 'src/app/model/action-type.enum';
import { Livro } from 'src/app/model/livro.model';
import { LivroService } from 'src/app/service/livro.service';
import { ModalLivroComponent } from './modal.livro.component';

@Component({
  selector: 'app-livro',
  templateUrl: './livro.component.html',
  styleUrls: ['./livro.component.sass']
})
export class LivroComponent {

  showLivroModal: boolean = false;
  livro!: Livro;
  livros$ = new Observable<Livro[]>;
  livroModal!: Livro;
  livros: Livro[] = [];
  showModal: boolean = false;
  selectedLivro: Livro = new Livro();
  searchTerm = '';
  currentDate!: string;

  constructor(private livroService: LivroService,
    private dialogError: MatDialog,
    private router: Router,
    private dialog: MatDialog){
    this.livroModal = new Livro();
    this.livroService.getLivros().subscribe(livros => this.livros = livros);
    this.livro = new Livro;
  }
  
  getLivros() {
    this.livros$ = this.livroService.getLivros();
  }

  openLivroModal(): void {
    this.showLivroModal = true;
  }

  showLivro(livro: Livro) {
    this.selectedLivro = { ...livro };
    this.showModal = true;
  }
openModal(actionType: ActionType, livro?: Livro): void {
  let title = '';
  let width = '500px';
  
  switch (actionType) {
    case ActionType.VIEW:
      title = 'Detalhes do Livro';
      width = '900px';
      break;
    case ActionType.CREATE:
      title = 'Cadastrar Livro';
      width = '650px';
      livro = new Livro();
      break;
    case ActionType.EDIT:
      title = 'Editar Livro';
      width = '350px !important';
      break;
    case ActionType.DELETE:
      title = 'Remover Livro';
      break;
  }

  const dialogRef = this.dialog.open(ModalLivroComponent, {
    width: width,
    data: {
      title: title,
      livro: livro || new Livro(),
      actionType: actionType
    }
  });
  
  dialogRef.afterClosed().subscribe(result => {
    
    if (result && ['created', 'updated', 'deleted'].includes(result)) {
      this.loadLivros();
    }
  });
  
}
  loadLivros(): void {
    this.livroService.getLivros().subscribe({
      next: (livros) => {
        this.livros = livros;
        console.log('Livros carregados:', livros);
      },
      error: (error) => {
        console.error('Erro ao carregar livros:', error);
      }
    });
  }

// Métodos atualizados no componente principal
viewLivro(livro: Livro): void {
  this.openModal(ActionType.VIEW, livro);
}

editLivro(livro: Livro): void {
    console.log('Editando livro:', livro);
    
    // Carrega os dados COMPLETOS para edição
    this.livroService.getLivroById(livro.codl!).subscribe({
      next: (livroCompleto) => {
        console.log('Livro completo carregado:', livroCompleto);
        this.openModal(ActionType.EDIT, livroCompleto);
      },
      error: (error) => {
        console.error('Erro ao carregar livro para edição:', error);
        this.openModal(ActionType.EDIT, livro);
      }
    });
  }

addLivro(): void {
  this.openModal(ActionType.CREATE);
}

removeLivro(livro: Livro): void {
  this.openModal(ActionType.DELETE, livro);
}

  private openErrorModal(error: any) {
    const dialogRef = this.dialogError.open(ModalerrorComponent, {
      width: '500px',
      data: {
        message: error.message,
        code: error.code,
        title: error.title
       }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.router.navigateByUrl('/');
    });
  }

  private formatDate(date: Date): string {
    return date.toLocaleString('pt-BR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
