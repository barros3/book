import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ 
  name: 'formatRelatorio' 
})
export class RelatorioPipe implements PipeTransform {
  transform(value: any, tipo: string = 'texto'): any {
    if (value === null || value === undefined || value === '') {
      return this.getDefaultValue(tipo);
    }
    
    switch (tipo) {
      case 'data':
        return this.formatData(value);
        
      case 'valor':
        return this.formatValor(value);
        
      case 'numero':
        return this.formatNumero(value);
        
      default:
        return String(value);
    }
  }
  
  private getDefaultValue(tipo: string): string {
    switch (tipo) {
      case 'data': return 'Não informado';
      case 'valor': return 'R$ 0,00';
      case 'numero': return '0';
      default: return '';
    }
  }
  
  private formatData(value: any): string {
    try {
      const date = new Date(value);
      
      if (isNaN(date.getTime())) {
        return 'Data inválida';
      }
      
      const dataStr = date.toLocaleDateString('pt-BR');
      const horaStr = date.toLocaleTimeString('pt-BR', { 
        hour: '2-digit', 
        minute: '2-digit' 
      });
      
      return `${dataStr} ${horaStr}`;
    } catch (error) {
      return 'Data inválida';
    }
  }
  
  private formatValor(value: any): string {
    try {
      // Converte para número
      const numero = Number(value);
      
      if (isNaN(numero)) {
        return 'R$ 0,00';
      }
      
      return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      }).format(numero);
    } catch (error) {
      return 'R$ 0,00';
    }
  }
  
  private formatNumero(value: any): string {
    try {
      const numero = Number(value);
      return isNaN(numero) ? '0' : numero.toLocaleString('pt-BR');
    } catch (error) {
      return '0';
    }
  }
}