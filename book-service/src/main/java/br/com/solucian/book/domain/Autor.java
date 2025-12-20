package br.com.solucian.book.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autor", schema = "public")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codau")
    private Integer codAu;

    @NotBlank
    @Size(max = 40)
    @Column(name = "nome", length = 40, nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "autores")
    @Builder.Default
    private Set<Livro> livros = new HashSet<>();
}
