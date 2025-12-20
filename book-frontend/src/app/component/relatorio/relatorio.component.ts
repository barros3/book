import { Component, OnInit } from '@angular/core';
import { RelatorioService } from 'src/app/service/relatorio.service';

@Component({
  selector: 'app-relatorio',
  templateUrl: './relatorio.component.html',
  styleUrls: ['./relatorio.component.sass']
})
export class RelatorioComponent implements OnInit {
  relatorios: any[] = [];
  searchNome = '';
  isLoading = false;

  constructor(
    private relatorioService: RelatorioService
  ) {}

  ngOnInit(): void {
    this.loadRelatorios();
  }

  loadRelatorios(): void {
    this.isLoading = true;
    this.relatorioService.getRelatorios().subscribe({
      next: (relatorios) => {
        this.relatorios = relatorios;
        this.isLoading = false;
        console.log('Relatórios carregados:', relatorios);
      },
      error: (error) => {
        console.error('Erro ao carregar relatórios:', error);
        this.isLoading = false;
      }
    });
  }

  downloadRelatorio(): void {
    this.isLoading = true;
    this.relatorioService.gerarRelatorio().subscribe({
      next: (pdfBlob) => {
        const url = window.URL.createObjectURL(pdfBlob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'relatorio.pdf';
        a.click();
        window.URL.revokeObjectURL(url);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao baixar relatório:', error);
        this.isLoading = false;
      }
    });
  }
}