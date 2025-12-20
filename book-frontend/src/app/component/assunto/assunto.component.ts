import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActionType } from 'src/app/model/action-type.enum';
import { Assunto } from 'src/app/model/assunto.model';
import { AssuntoService } from 'src/app/service/assunto.service';
import { ModalAssuntoComponent } from './modal.assunto.component';

@Component({
  selector: 'app-assunto',
  templateUrl: './assunto.component.html',
  styleUrls: ['./assunto.component.sass']
})
export class AssuntoComponent implements OnInit {
  assuntos: Assunto[] = [];
  searchTerm = '';
  isLoading = false;

  constructor(
    private assuntoService: AssuntoService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadAssuntos();
  }

  loadAssuntos(): void {
    this.isLoading = true;
    this.assuntoService.getAssuntos().subscribe({
      next: (assuntos) => {
        this.assuntos = assuntos;
        this.isLoading = false;
        console.log('Assuntos carregados:', assuntos);
      },
      error: (error) => {
        console.error('Erro ao carregar assuntos:', error);
        this.isLoading = false;
      }
    });
  }

  openModal(actionType: ActionType, assunto?: Assunto): void {
    let title = '';
    let width = '500px';
    
    switch (actionType) {
      case ActionType.VIEW:
        title = 'Detalhes do Assunto';
        width = '500px';
        break;
      case ActionType.CREATE:
        title = 'Cadastrar Assunto';
        width = '500px';
        assunto = new Assunto();
        break;
      case ActionType.EDIT:
        title = 'Editar Assunto';
        width = '500px';
        break;
      case ActionType.DELETE:
        title = 'Remover Assunto';
        break;
    }

    const dialogRef = this.dialog.open(ModalAssuntoComponent, {
      width: width,
      data: {
        title: title,
        assunto: assunto || new Assunto(),
        actionType: actionType
      }
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result && ['created', 'updated', 'deleted'].includes(result)) {
        this.loadAssuntos();
      }
    });
  }

  viewAssunto(assunto: Assunto): void {
    this.openModal(ActionType.VIEW, assunto);
  }

  editAssunto(assunto: Assunto): void {
    console.log('Editando assunto:', assunto);
    
    this.assuntoService.getAssuntoId(this.normalizeAssunto(assunto).codAs).subscribe({
      next: (assuntoCompleto) => {
        console.log('Assunto completo carregado:', assuntoCompleto);
        this.openModal(ActionType.EDIT, assuntoCompleto);
      },
      error: (error) => {
        console.error('Erro ao carregar assunto para edição:', error);
        this.openModal(ActionType.EDIT, assunto);
      }
    });
  }

  addAssunto(): void {
    this.openModal(ActionType.CREATE);
  }

  removeAssunto(assunto: Assunto): void {
    this.openModal(ActionType.DELETE, assunto);
  }

  
  normalizeAssunto(data: any): Assunto {
    return {
      codAs: data.codAs,
      descricao: data.descricao
    };
  }
}