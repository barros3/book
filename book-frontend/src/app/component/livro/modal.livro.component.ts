import { Component, Inject, Input, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ModalerrorComponent } from 'src/app/modalerror/modalerror.component';
import { ActionType } from 'src/app/model/action-type.enum';
import { Assunto } from 'src/app/model/assunto.model';
import { Autor } from 'src/app/model/autor.model';
import { Livro } from 'src/app/model/livro.model';
import { ResponseError } from 'src/app/model/response.error';
import { LivroService } from 'src/app/service/livro.service';

@Component({
  selector: 'app-modal-cadastra-livro',
  templateUrl: './modal.livro.component.html',
  styleUrls: ['./modal.livro.component.sass']
})
export class ModalLivroComponent implements OnInit {
  
  @Input() title: string | undefined;
  currentDate!: string;
  formData = new FormData();
  
  livro!: Livro;
  autoresDisponiveis: any[] = [];
  assuntosDisponiveis: any[] = [];
  selectedAutores: number[] = [];
  selectedAssuntos: number[] = [];

  isViewMode = false;
  isLoading = false;
  isLoadingRelacionamentos = false;
  isEditMode = false;
  isDeleteMode = false;
  isLoadingAutores = false;
  isLoadingAssuntos = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { 
      title: string, 
      livro: Livro,
      actionType: ActionType
    },
    private dialogRef: MatDialogRef<ModalLivroComponent>,
    private dialogError: MatDialog,
    private router: Router,
    private livroService: LivroService,
  ) {
    this.title = data.title;
    this.livro = { ...data.livro };
    this.isViewMode = data.actionType === ActionType.VIEW;
    this.isDeleteMode = data.actionType === ActionType.DELETE;
    this.isEditMode = data.actionType === ActionType.EDIT || data.actionType === ActionType.CREATE;
  }

  autoresRelacionados: Autor[] = [];
  assuntosRelacionados: Assunto[] = [];
  
  ngOnInit() {
    // Se for modo visualização, carrega dados completos
    if (this.data.actionType === ActionType.VIEW && this.livro.codl) {
      this.loadLivroDetails();
      this.loadRelacionamentos();
    }
        // Carrega listas disponíveis para CREATE e EDIT
    if (this.isEditMode) {
      this.carregarAutoresDisponiveis();
      this.carregarAssuntosDisponiveis();
    }

        // Se for EDIT, configura os selecionados
    if (this.data.actionType === ActionType.EDIT) {
      // Se vier com autoresIds/assuntosIds do backend
      if (this.livro.autoresIds) {
        this.selectedAutores = [...this.livro.autoresIds];
      }
      if (this.livro.assuntosIds) {
        this.selectedAssuntos = [...this.livro.assuntosIds];
      }
      
      // Ou se vier com array de objetos
      if (this.livro.autores && Array.isArray(this.livro.autores)) {
        this.selectedAutores = this.livro.autores.map((a: any) => a.codAu);
      }
      if (this.livro.assuntos && Array.isArray(this.livro.assuntos)) {
        this.selectedAssuntos = this.livro.assuntos.map((a: any) => a.codAs);
      }
    }
  }

  // Método único que decide qual ação executar
  actionLivro(): void {
    switch (this.data.actionType) {
      case ActionType.VIEW:
        this.detailLivro();
        break;
      case ActionType.CREATE:
        this.salvarLivro();
        break;
      case ActionType.EDIT:
        this.updateLivro();
        break;
      case ActionType.DELETE:
        this.removeLivro();
        break;
      default:
        console.error('Tipo de ação não suportado:', this.data.actionType);
    }
  }

  // 1. Detalhar Livro (VIEW)
  private detailLivro(): void {
    if (!this.livro.titulo) return;
    
    this.isLoading = true;
    this.livroService.getLivroById(this.livro.codl).subscribe({
      next: (livroDetalhado) => {
        this.livro = livroDetalhado;
        this.isLoading = false;
        
        // Se o backend já retornar os relacionamentos
        if (livroDetalhado.autores) {
          this.autoresRelacionados = livroDetalhado.autores;
        }
        if (livroDetalhado.assuntos) {
          this.assuntosRelacionados = livroDetalhado.assuntos;
        }
        
        console.log('Livro detalhado:', livroDetalhado);
      },
      error: (error) => {
        this.handleError(error, 'Erro ao carregar detalhes do livro');
        this.isLoading = false;
      }
    });
  }

  // 2. Salvar Livro (CREATE)
  private saveLivro(): void {
    this.isLoading = true;
    this.livroService.save(this.livro).subscribe({
      next: (response) => {
        this.handleSuccess(
          `Livro "${response.titulo}" cadastrado com sucesso!`,
          response.createdAtLivro || this.formatDate(new Date())
        );
        this.isLoading = false;
        this.dialogRef.close('created'); // Fecha o modal com resultado
      },
      error: (error) => {
        this.handleError(error, 'Erro ao cadastrar livro');
        this.isLoading = false;
      }
    });
  }

  // 3. Atualizar Livro (EDIT)
  private updateLivro(): void {
    if (!this.livro.codl) return;
    
    this.isLoading = true;
    this.livroService.update(this.livro).subscribe({
      next: (response) => {
        this.handleSuccess(
          `Livro "${response.titulo}" atualizado com sucesso!`,
          this.formatDate(new Date())
        );
        this.isLoading = false;
        this.dialogRef.close('updated'); // Fecha o modal com resultado
      },
      error: (error) => {
        this.handleError(error, 'Erro ao atualizar livro');
        this.isLoading = false;
      }
    });
  }

  // 4. Remover Livro (DELETE)
  removeLivro(): void {
    if (!this.livro.codl) return;
    
    if (confirm(`Tem certeza que deseja remover o livro "${this.livro.titulo}"?`)) {
      this.isLoading = true;
      this.livroService.remove(this.livro).subscribe({
        next: () => {
          this.handleSuccess(
            `Livro "${this.livro.titulo}" removido com sucesso!`,
            this.formatDate(new Date())
          );
          this.isLoading = false;
          this.dialogRef.close('deleted');
        },
        error: (error) => {
          this.handleError(error, 'Erro ao remover livro');
          this.isLoading = false;
        }
      });
    }
  }

  // Método para carregar autores e assuntos relacionados
  private loadRelacionamentos(): void {
    if (!this.livro.titulo) return;
    
    this.isLoadingRelacionamentos = true;
    
    this.livroService.getLivroById(this.livro.codl).subscribe({
      next: (livro) => {
        this.autoresRelacionados = livro.autores;
        this.assuntosRelacionados = livro.assuntos;
        this.isLoadingRelacionamentos = false;
      },
      error: (error) => {
        console.error('Erro ao carregar autores:', error);
        this.isLoadingRelacionamentos = false;
      }
    });
  }

  // 5. Carregar detalhes do livro (para visualização)
  private loadLivroDetails(): void {
    if (!this.livro.codl) return;
    
    this.isLoading = true;
    this.livroService.getLivroById(this.livro.codl).subscribe({
      next: (livroCompleto) => {
        this.livro = livroCompleto;
        this.isLoading = false;
      },
      error: (error) => {
        this.handleError(error, 'Erro ao carregar detalhes');
        this.isLoading = false;
      }
    });
  }

  // Métodos auxiliares
  private handleSuccess(message: string, date: string): void {
    const error = new ResponseError(
      message,
      `Data: ${date}`,
      'Sucesso!'
    );
    this.openErrorModal(error);
  }

  private handleError(error: any, defaultMessage: string): void {
    console.error(defaultMessage, error);
    
    const errorMsg = error?.error?.mensagem || error?.message || defaultMessage;
    const errorCode = error?.error?.errorCode || 'ERRO_DESCONHECIDO';
    
    const responseError = new ResponseError(
      errorMsg,
      `Código: ${errorCode}`,
      'Ops..'
    );
    
    this.openErrorModal(responseError);
  }

  private openErrorModal(error: ResponseError): void {
    const dialogRef = this.dialogError.open(ModalerrorComponent, {
      width: '500px',
      data: error
    });

    dialogRef.afterClosed().subscribe(() => {
      this.router.navigateByUrl('/livro');
    });
  }

  closeModal(): void {
    this.dialogRef.close();
  }

  getButtonText(): string {
    switch (this.data.actionType) {
      case ActionType.VIEW: return 'Fechar';
      case ActionType.CREATE: return this.isLoading ? 'Cadastrando...' : 'Cadastrar';
      case ActionType.EDIT: return this.isLoading ? 'Atualizando...' : 'Atualizar';
      case ActionType.DELETE: return this.isLoading ? 'Removendo...' : 'Confirmar Remoção';
      default: return 'Salvar';
    }
  }

  getButtonColor(): string {
    switch (this.data.actionType) {
      case ActionType.DELETE: return 'btn-danger';
      default: return 'btn-primary';
    }
  }

  isButtonDisabled(): boolean {
    if (this.isLoading) return true;
    if (this.data.actionType === ActionType.VIEW) return false;

    return !this.livro.titulo || !this.livro.editora;
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
    hasRelacionamentos(): boolean {
    return this.autoresRelacionados.length > 0 || this.assuntosRelacionados.length > 0;
  }
  
  visualizarAutor(autor: Autor): void {
    // Pode abrir outro modal ou navegar
    console.log('Visualizando autor:', autor);
    // this.router.navigate(['/autor', autor.codAu]);
  }

  visualizarAssunto(assunto: Assunto): void {
    console.log('Visualizando assunto:', assunto);
    // Exemplo: this.router.navigate(['/assunto', assunto.codAs]);
  }

  carregarAutoresDisponiveis(): void {
    this.isLoadingAutores = true;
    this.livroService.getAutoresDisponiveis().subscribe({
      next: (autores) => {
        this.autoresDisponiveis = autores;
        this.isLoadingAutores = false;
      },
      error: (error) => {
        console.error('Erro ao carregar autores:', error);
        this.isLoadingAutores = false;
      }
    });
  }

  carregarAssuntosDisponiveis(): void {
    this.isLoadingAssuntos = true;
    this.livroService.getAssuntosDisponiveis().subscribe({
      next: (assuntos) => {
        this.assuntosDisponiveis = assuntos;
        this.isLoadingAssuntos = false;
      },
      error: (error) => {
        console.error('Erro ao carregar assuntos:', error);
        this.isLoadingAssuntos = false;
      }
    });
  }

  salvarLivro(): void {
    this.isLoading = true;
    
    // Prepara objeto completo para enviar
  const livroParaSalvar = {
      codL: this.livro.codl, // Explicitamente pegar o codL primeiro
      ...this.livro,
      autoresIds: this.selectedAutores,
      assuntosIds: this.selectedAssuntos
  };
    
    console.log('Enviando livro:', livroParaSalvar);
    
    this.livroService.saveCompleto(livroParaSalvar).subscribe({
      next: (response) => {
        console.log('Livro salvo com sucesso:', response);
        this.dialogRef.close('updated');
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao salvar livro:', error);
        this.isLoading = false;
        // Tratar erro
      }
    });
  }

  compararPorId(o1: any, o2: any): boolean {
    if (o1 && o2) {
      return o1.codAu === o2.codAu || o1.codAs === o2.codAs;
    }
    return false;
}


getAutorNome(autorId: number): string {
  const autor = this.autoresDisponiveis.find(a => a.codAu === autorId);
  return autor ? autor.nome : `Autor ${autorId}`;
}

getAssuntoDescricao(assuntoId: number): string {
  const assunto = this.assuntosDisponiveis.find(a => a.codAs === assuntoId);
  return assunto ? assunto.descricao : `Assunto ${assuntoId}`;
}

removerAutor(autorId: number): void {
  this.selectedAutores = this.selectedAutores.filter(id => id !== autorId);
}

removerAssunto(assuntoId: number): void {
  this.selectedAssuntos = this.selectedAssuntos.filter(id => id !== assuntoId);
}

  confirmDelete(): void {
    this.salvarAssunto();
  }
  
  salvarAssunto(): void {
    this.isLoading = true;
    
    if (this.data.actionType === 'create') {
      this.livroService.save(this.livro).subscribe({
        next: (response) => {
          this.dialogRef.close('created');
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao criar assunto:', error);
          this.isLoading = false;
        }
      });
    } else if (this.data.actionType === 'edit') {
      this.livroService.update(this.livro).subscribe({
        next: (response) => {
          this.dialogRef.close('updated');
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao atualizar assunto:', error);
          this.isLoading = false;
        }
      });
    } else if (this.data.actionType === 'delete') {
      this.livroService.remove(this.livro!).subscribe({
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

  onValorChange(valorFormatado: string) {
    const valorLimpo = valorFormatado
      .replace('R$', '')
      .replace('.', '')
      .replace(',', '.')
      .trim();
    
    this.livro.valor = parseFloat(valorLimpo) || 0;
  }
}