import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(value: any[], searchTerm: string, listType?: string): any[] {
    if (!searchTerm || !value) {
      return value;
    }
    
    const term = searchTerm.toLowerCase().trim();
    
    return value.filter(item => {
      if (!item) return false;
      
      if (listType === 'autores') {
        if (item.nome && item.nome.toLowerCase().includes(term)) return true;
        if (item.id && item.id.toString().includes(term)) return true;
      }
      
      if (listType === 'livros') {
        if (item.titulo && item.titulo.toLowerCase().includes(term)) return true;
        if (item.autor) {
          if (typeof item.autor === 'string' && item.autor.toLowerCase().includes(term)) return true;
          if (item.autor.nome && item.autor.nome.toLowerCase().includes(term)) return true;
        }
        if (item.id && item.id.toString().includes(term)) return true;
      }
      
      if (listType === 'assuntos') {
        if (item.descricao && item.descricao.toLowerCase().includes(term)) return true;
        if (item.id && item.id.toString().includes(term)) return true;
      }
      
      return false;
    });
  }
}