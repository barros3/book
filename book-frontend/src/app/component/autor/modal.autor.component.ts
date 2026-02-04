import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ActionType } from 'src/app/model/action-type.enum';
import { Autor } from 'src/app/model/autor.model';
import { AutorService } from 'src/app/service/autor.service';

export interface DialogData {
  title: string;
  autor: Autor;
  actionType: string;
}

@Component({
  selector: 'app-modal-autor',
  templateUrl: './modal.autor.component.html',
  styleUrls: ['./modal.autor.component.sass']
})
export class ModalAutorComponent implements OnInit {
  autor: Autor = new Autor();
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
    public dialogRef: MatDialogRef<ModalAutorComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private autorService: AutorService
  ) {
    this.title = data.title;
    this.autor = { ...data.autor };
    this.actionType = data.actionType;
    this.isDeleteMode = data.actionType === ActionType.DELETE;
  }

  ngOnInit(): void {
  
    if (this.isViewMode && this.autor.codAu) {
      this.loadAutorDetails();
    }
  }

  loadAutorDetails(): void {
    if (!this.autor.codAu) return;
    
    this.isLoading = true;
    this.autorService.getByAutorId(this.autor.codAu).subscribe({
      next: (autorCompleto) => {
        this.autor = autorCompleto;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar detalhes do autor:', error);
        this.isLoading = false;
      }
    });
  }

  salvarAutor(): void {
    this.isLoading = true;
    
    if (this.actionType === 'create') {
      this.autorService.save(this.autor).subscribe({
        next: (response) => {
          this.dialogRef.close('created');
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao criar autor:', error);
          this.isLoading = false;
        }
      });
    } else if (this.actionType === 'edit') {
      this.autorService.update(this.autor).subscribe({
        next: (response) => {
          this.dialogRef.close('updated');
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao atualizar autor:', error);
          this.isLoading = false;
        }
      });
    } else if (this.actionType === 'delete') {
      this.autorService.remove(this.autor!).subscribe({
        next: (response) => {
          this.dialogRef.close('deleted');
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao remover autor:', error);
          this.isLoading = false;
        }
      });
    }
  }

  closeModal(): void {
    this.dialogRef.close();
  }

  confirmDelete(): void {
    this.salvarAutor();
  }
}