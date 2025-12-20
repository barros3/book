package br.com.solucian.book.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "livro", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codl")
    private Integer codl;

    @NotBlank
    @Size(max = 40)
    @Column(name = "titulo", length = 40, nullable = false)
    private String titulo;

    @NotBlank
    @Size(max = 40)
    @Column(name = "editora", length = 40, nullable = false)
    private String editora;

    @NotNull
    @Column(name = "edicao", nullable = false)
    private Integer edicao;

    @NotBlank
    @Size(max = 4)
    @Column(name = "anopublicacao", length = 4, nullable = false)
    private String anoPublicacao;

    @NotBlank
    @Column(name = "valor", nullable = false)
    BigDecimal valor;

    @ManyToMany
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_cod", referencedColumnName = "codl"),
            inverseJoinColumns = @JoinColumn(name = "autor_codau", referencedColumnName = "codau")
    )
    @Builder.Default
    private Set<Autor> autores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "livro_assunto",
            joinColumns = @JoinColumn(name = "livro_cod", referencedColumnName = "codl"),
            inverseJoinColumns = @JoinColumn(name = "assunto_codas", referencedColumnName = "codas")
    )
    @Builder.Default
    private Set<Assunto> assuntos = new HashSet<>();
}
