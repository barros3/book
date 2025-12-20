-- ===============================
-- VIEW MATERIALIZED DO RELATÓRIO
-- ===============================

CREATE MATERIALIZED VIEW public.vw_relatorio_geral_autor
TABLESPACE pg_default
AS SELECT a.codau AS autor_codau,
    a.nome AS autor_nome,
    COALESCE(string_agg(DISTINCT l.titulo::text, ', '::text ORDER BY (l.titulo::text)), ''::text) AS livros,
    COALESCE(string_agg(DISTINCT s.descricao::text, ', '::text ORDER BY (s.descricao::text)), ''::text) AS assuntos,
    count(DISTINCT l.codl) AS qtd_livros,
    count(DISTINCT s.codas) AS qtd_assuntos,
    COALESCE(sum(DISTINCT l.valor), 0::numeric) AS valor_total,
    now() AS atualizado_em
   FROM autor a
     LEFT JOIN livro_autor la ON la.autor_codau = a.codau
     LEFT JOIN livro l ON l.codl = la.livro_cod
     LEFT JOIN livro_assunto ls ON ls.livro_cod = l.codl
     LEFT JOIN assunto s ON s.codas = ls.assunto_codas
  GROUP BY a.codau, a.nome
WITH DATA;

-- View indexes:
CREATE INDEX idx_vw_relatorio_autor_codau ON public.vw_relatorio_geral_autor USING btree (autor_codau);