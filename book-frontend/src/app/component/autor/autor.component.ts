import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActionType } from 'src/app/model/action-type.enum';
import { Autor } from 'src/app/model/autor.model';
import { AutorService } from 'src/app/service/autor.service';
import { ModalAutorComponent } from './modal.autor.component';

@Component({
  selector: 'app-autor',
  templateUrl: './autor.component.html',
  styleUrls: ['./autor.component.sass']
})
export class AutorComponent implements OnInit {
  autores: Autor[] = [];
  searchTerm = '';
  isLoading = false;

  constructor(
    private autorService: AutorService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadAutores();
  }

  loadAutores(): void {
    this.isLoading = true;
    this.autorService.getAutors().subscribe({
      next: (autores) => {
        this.autores = autores;
        this.isLoading = false;
        console.log('Autores carregados:', autores);
      },
      error: (error) => {
        console.error('Erro ao carregar autores:', error);
        this.isLoading = false;
      }
    });
  }

  openModal(actionType: ActionType, autor?: Autor): void {
    let title = '';
    let width = '500px';
    
    switch (actionType) {
      case ActionType.VIEW:
        title = 'Detalhes do Autor';
        width = '500px';
        break;
      case ActionType.CREATE:
        title = 'Cadastrar Autor';
        width = '500px';
        autor = new Autor();
        break;
      case ActionType.EDIT:
        title = 'Editar Autor';
        width = '500px';
        break;
      case ActionType.DELETE:
        title = 'Remover Autor';
        break;
    }

    const dialogRef = this.dialog.open(ModalAutorComponent, {
      width: width,
      data: {
        title: title,
        autor: autor || new Autor(),
        actionType: actionType
      }
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result && ['created', 'updated', 'deleted'].includes(result)) {
        this.loadAutores();
      }
    });
  }

  viewAutor(autor: Autor): void {
    this.openModal(ActionType.VIEW, autor);
  }

  editAutor(autor: Autor): void {
    console.log('Editando autor:', autor);
    
    this.autorService.getByAutorId(autor.codAu!).subscribe({
      next: (autorCompleto) => {
        console.log('Autor completo carregado:', autorCompleto);
        this.openModal(ActionType.EDIT, autorCompleto);
      },
      error: (error) => {
        console.error('Erro ao carregar autor para edição:', error);
        this.openModal(ActionType.EDIT, autor);
      }
    });
  }

  addAutor(): void {
    this.openModal(ActionType.CREATE);
  }

  removeAutor(autor: Autor): void {
    this.openModal(ActionType.DELETE, autor);
  }
}