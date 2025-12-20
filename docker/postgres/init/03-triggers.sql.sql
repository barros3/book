-- ===============================
-- FUNÇÕES E TRIGGERS
-- ===============================

-- Primeiro, remova os triggers que dependem da função
DROP TRIGGER IF EXISTS trg_relatorio_autor_on_livro_insert ON public.livro;
DROP TRIGGER IF EXISTS trg_relatorio_autor_on_livro_autor_insert ON public.livro_autor;
DROP TRIGGER IF EXISTS trg_relatorio_autor_on_livro_assunto_insert ON public.livro_assunto;

--
--- Função única para todas as triggers
CREATE OR REPLACE FUNCTION public.tg_refresh_relatorio_autor()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $function$
BEGIN
    REFRESH MATERIALIZED VIEW public.vw_relatorio_geral_autor;
    RETURN NULL;
END;
$function$;

--- Triggers para as acoes
CREATE TRIGGER trg_relatorio_autor_on_livro_insert
AFTER INSERT OR UPDATE OR DELETE ON public.livro
FOR EACH STATEMENT
EXECUTE FUNCTION public.tg_refresh_relatorio_autor();

CREATE TRIGGER trg_relatorio_autor_on_livro_autor_insert
AFTER INSERT OR UPDATE OR DELETE ON public.livro_autor
FOR EACH STATEMENT
EXECUTE FUNCTION public.tg_refresh_relatorio_autor();

CREATE TRIGGER trg_relatorio_autor_on_livro_assunto_insert
AFTER INSERT OR UPDATE OR DELETE ON public.livro_assunto
FOR EACH STATEMENT
EXECUTE FUNCTION public.tg_refresh_relatorio_autor();
