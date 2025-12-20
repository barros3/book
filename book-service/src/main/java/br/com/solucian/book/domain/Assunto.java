package br.com.solucian.book.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "assunto", schema = "public")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codas")
    private Integer codAs;

    @NotBlank
    @Size(max = 20)
    @Column(name = "descricao", length = 20, nullable = false)
    private String descricao;

    @ManyToMany(mappedBy = "assuntos")
    @Builder.Default
    private Set<Livro> livros = new HashSet<>();
}
