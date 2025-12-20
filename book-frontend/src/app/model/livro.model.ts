import { Assunto } from "./assunto.model";
import { Autor } from "./autor.model";

export class Livro {
  codl?: number;
  titulo?: string;
  editora?: string;
  edicao?: number;
  anoPublicacao?: string;
  valor?: number;
  autores?: Autor[];
  assuntos?: Assunto[];
  autoresIds?: number[];
  assuntosIds?: number[];
  
}
