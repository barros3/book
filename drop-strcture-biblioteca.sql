-- ============================================
-- SCRIPT DIRECIONADO PARA SUA ESTRUTURA
-- ============================================

-- 1. REMOVER TRIGGERS ESPECÍFICOS
DROP TRIGGER IF EXISTS tg_relatorio_autor_livro_insert ON public.livro;
-- DROP TRIGGER IF EXISTS tg_relatorio_autor_livro_autor_insert ON public.livro_autor;

-- 2. REMOVER VIEWS MATERIALIZADAS
DROP MATERIALIZED VIEW IF EXISTS public.vw_relatorio_geral_autor;

-- 3. REMOVER FUNÇÕES (CRUD)
DROP FUNCTION IF EXISTS public.fn_assunto_delete(integer);
DROP FUNCTION IF EXISTS public.fn_assunto_get(integer);
DROP FUNCTION IF EXISTS public.fn_assunto_insert(character varying);
DROP FUNCTION IF EXISTS public.fn_assunto_list();
DROP FUNCTION IF EXISTS public.fn_assunto_update(integer, character varying);

DROP FUNCTION IF EXISTS public.fn_autor_delete(integer);
DROP FUNCTION IF EXISTS public.fn_autor_get(integer);
DROP FUNCTION IF EXISTS public.fn_autor_insert(character varying);
DROP FUNCTION IF EXISTS public.fn_autor_list();
DROP FUNCTION IF EXISTS public.fn_autor_update(integer, character varying);

DROP FUNCTION IF EXISTS public.fn_livro_delete(integer);
DROP FUNCTION IF EXISTS public.fn_livro_get(integer);
DROP FUNCTION IF EXISTS public.fn_livro_insert(character varying, character varying, integer, character varying, numeric, integer[], integer[]);
DROP FUNCTION IF EXISTS public.fn_livro_list();
DROP FUNCTION IF EXISTS public.fn_livro_update(integer, character varying, character varying, integer, character varying, numeric, integer[], integer[]);

-- 4. REMOVER TABELAS (em ordem correta por dependências)
DROP TABLE IF EXISTS public.livro_autor CASCADE;
DROP TABLE IF EXISTS public.livro_assunto CASCADE;
DROP TABLE IF EXISTS public.livro CASCADE;
DROP TABLE IF EXISTS public.assunto CASCADE;
DROP TABLE IF EXISTS public.autor CASCADE;

-- 5. REMOVER SEQUENCES (se existirem fora das tabelas)
DROP SEQUENCE IF EXISTS public.autor_codau_seq CASCADE;
DROP SEQUENCE IF EXISTS public.assunto_codas_seq CASCADE;
DROP SEQUENCE IF EXISTS public.livro_codl_seq CASCADE;

-- 6. VERIFICAR REMOÇÃO
SELECT 
    'Tabelas restantes' as status,
    COUNT(*) as total
FROM pg_tables 
WHERE schemaname = 'public';