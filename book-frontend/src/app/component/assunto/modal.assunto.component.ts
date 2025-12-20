import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ActionType } from 'src/app/model/action-type.enum';
import { Assunto } from 'src/app/model/assunto.model';
import { AssuntoService } from 'src/app/service/assunto.service';

export interface DialogData {
  title: string;
  assunto: Assunto;
  actionType: string;
}

@Component({
  selector: 'app-modal-assunto',
  templateUrl: './modal.assunto.component.html',
  styleUrls: ['./modal.assunto.component.sass']
})
export class ModalAssuntoComponent implements OnInit {
  assunto: Assunto = new Assunto();
  title: string = '';
  actionType: string = '';
  isDeleteMode = false;
  
  isLoading = false;
  
  get isViewMode(): boolean {
    return this.actionType === 'view';
  }
  
  get isEditMode(): boolean {
    return this.actionType === 'edit' || this.actionType === 'create';
  }

  constructor(
    public dialogRef: MatDialogRef<ModalAssuntoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private assuntoService: AssuntoService
  ) {
    this.title = data.title;
    this.assunto = { ...data.assunto };
    this.actionType = data.actionType;
    this.isDeleteMode = data.actionType === ActionType.DELETE;
  }

  ngOnInit(): void {
    if (this.isViewMode && this.assunto.codAs) {
      this.loadAssuntoDetails();
    }
  }

  loadAssuntoDetails(): void {
    if (!this.assunto.codAs) return;
    
    this.isLoading = true;
    this.assuntoService.getAssuntoId(this.assunto.codAs).subscribe({
      next: (assuntoCompleto) => {
        this.assunto = assuntoCompleto;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar detalhes do assunto:', error);
        this.isLoading = false;
      }
    });
  }

  salvarAssunto(): void {
    this.isLoading = true;
    
    if (this.actionType === 'create') {
      this.assuntoService.save(this.assunto).subscribe({
        next: (response) => {
          this.dialogRef.close('created');
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao criar assunto:', error);
          this.isLoading = false;
        }
      });
    } else if (this.actionType === 'edit') {
      console.log('assunto modal comepnet ', this.assunto)
      this.assuntoService.update(this.assunto).subscribe({
        next: (response) => {
          this.dialogRef.close('updated');
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao atualizar assunto:', error);
          this.isLoading = false;
        }
      });
    } else if (this.actionType === 'delete') {
      this.assuntoService.remove(this.assunto).subscribe({
        next: (response) => {
          this.dialogRef.close('deleted');
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao remover assunto:', error);
          this.isLoading = false;
        }
      });
    }
  }

  closeModal(): void {
    this.dialogRef.close();
  }

  confirmDelete(): void {
    this.salvarAssunto();
  }

  removeAssunto(assunto: Assunto): void {
    this.openModal(ActionType.DELETE, assunto);
  }

  openModal(actionType: ActionType, assunto?: Assunto): void {
      let title = '';
      let width = '500px';
      
      switch (actionType) {
        case ActionType.VIEW:
          title = 'Detalhes do Assunto';
          width = '900px';
          break;
        case ActionType.CREATE:
          title = 'Cadastrar Assunto';
          width = '650px';
          assunto = new Assunto();
          break;
        case ActionType.EDIT:
          title = 'Editar Assunto';
          width = '350px !important';
          break;
        case ActionType.DELETE:
          title = 'Remover Assunto';
          break;
      }
  }

}