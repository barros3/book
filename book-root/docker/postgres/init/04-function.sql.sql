-- ===============================
-- FUNCTIONS CRUD
-- ===============================

CREATE OR REPLACE FUNCTION public.fn_assunto_delete(p_codas integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE
    v_updated BOOLEAN;
BEGIN
    UPDATE public.assunto
    SET ativo = false
    WHERE codas = p_codas;

    v_updated := FOUND;
    RETURN v_updated;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_assunto_get(p_codas integer)
 RETURNS TABLE(codas integer, descricao character varying)
 LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN QUERY
    SELECT a.codas, a.descricao
    FROM public.assunto a
    WHERE a.codas = p_codas;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_assunto_insert(p_descricao character varying)
 RETURNS TABLE(codas integer, descricao character varying)
 LANGUAGE plpgsql
AS $function$
DECLARE
    v_codas INTEGER;
BEGIN
    -- Insere o novo assunto e captura o ID gerado
    INSERT INTO public.assunto (descricao)
    VALUES (p_descricao)
    RETURNING assunto.codas INTO v_codas;
    
    -- Retorna o assunto completo
    RETURN QUERY
    SELECT a.codas, a.descricao
    FROM public.assunto a
    WHERE a.codas = v_codas;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_assunto_list(
_page integer DEFAULT 1,
_page_size integer DEFAULT 10
)
RETURNS TABLE(codas integer, descricao character varying)
	LANGUAGE plpgsql
	AS $function$
	BEGIN
		RETURN QUERY
			SELECT a.codas, a.descricao
				FROM public.assunto a
			WHERE a.ativo = true
			ORDER BY a.descricao
		LIMIT _page_size
		OFFSET ((_page - 1) * _page_size);
	END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_assunto_update(p_codas integer, p_descricao character varying)
 RETURNS TABLE(codas integer, descricao character varying)
 LANGUAGE plpgsql
AS $function$
BEGIN
    -- Atualiza o assunto
    UPDATE public.assunto
    SET descricao = p_descricao
    WHERE assunto.codas = p_codas;
    
    -- Verifica se alguma linha foi atualizada
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Assunto com código % não encontrado', p_codas;
    END IF;
    
    -- Retorna o assunto atualizado
    RETURN QUERY
    SELECT a.codas, a.descricao
    FROM public.assunto a
    WHERE a.codas = p_codas;
END;
$function$
;

-- ====================================
-- FUNCTIONS / PROCEDURES CRUD AUTOR
-- ====================================

CREATE OR REPLACE FUNCTION public.fn_autor_delete(p_codau integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE
    v_updated BOOLEAN;
BEGIN
    UPDATE public.autor
    SET ativo = false
    WHERE codau = p_codau;

    v_updated := FOUND;
    RETURN v_updated;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_autor_get(p_codau integer)
 RETURNS TABLE(codau integer, nome character varying)
 LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN QUERY
    SELECT a.codau, a.nome
    FROM public.autor a
    WHERE a.codau = p_codau;
END;
$function$
;


CREATE OR REPLACE FUNCTION public.fn_autor_insert(p_nome character varying)
 RETURNS TABLE(codau integer, nome character varying)
 LANGUAGE plpgsql
AS $function$
DECLARE
    v_codau INTEGER;
BEGIN
    -- Insere o novo autor e captura o ID gerado
    INSERT INTO public.autor (nome)
    VALUES (p_nome)
    RETURNING autor.codau INTO v_codau;
    
    -- Retorna o autor completo
    RETURN QUERY
    SELECT a.codau, a.nome
    FROM public.autor a
    WHERE a.codau = v_codau;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_autor_list(
    _page integer DEFAULT 1,
    _page_size integer DEFAULT 10
)
 RETURNS TABLE(codau integer, nome character varying)
 LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN QUERY
		SELECT a.codau, a.nome
			FROM public.autor a
			WHERE a.ativo = true
		ORDER BY a.nome
    LIMIT _page_size
    OFFSET ((_page - 1) * _page_size);
END;
$function$;

CREATE OR REPLACE FUNCTION public.fn_autor_update(p_codau integer, p_nome character varying)
 RETURNS TABLE(codau integer, nome character varying)
 LANGUAGE plpgsql
AS $function$
BEGIN
    -- Atualiza o autor
    UPDATE public.autor
    SET nome = p_nome
    WHERE autor.codau = p_codau;
    
    -- Verifica se alguma linha foi atualizada
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Autor com código % não encontrado', p_codau;
    END IF;
    
    -- Retorna o autor atualizado
    RETURN QUERY
    SELECT a.codau, a.nome
    FROM public.autor a
    WHERE a.codau = p_codau;
END;
$function$
;


-- ====================================
-- FUNCTIONS / PROCEDURES CRUD LIVRO
-- ====================================

CREATE OR REPLACE FUNCTION public.fn_livro_delete(p_codl integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE
    v_updated BOOLEAN;
BEGIN
    UPDATE public.livro
    SET ativo = false
    WHERE codl = p_codl;

    v_updated := FOUND;
    RETURN v_updated;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_livro_get(p_codl integer)
 RETURNS TABLE(codl integer, titulo character varying, editora character varying, edicao integer, anopublicacao character varying, valor numeric)
 LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN QUERY
    SELECT
        l.codl,
        l.titulo,
        l.editora,
        l.edicao,
        l.anopublicacao,
        l.valor
    FROM public.livro l
    WHERE l.codl = p_codl;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_livro_insert(p_titulo character varying, p_editora character varying, p_edicao integer, p_anopublicacao character varying, p_valor numeric, p_autores integer[], p_assuntos integer[])
 RETURNS TABLE(codl integer, titulo character varying, editora character varying, edicao integer, anopublicacao character varying, valor numeric)
 LANGUAGE plpgsql
AS $function$
DECLARE
    v_codl INTEGER;
    v_id INTEGER;
BEGIN
    INSERT INTO public.livro (titulo, editora, edicao, anopublicacao, valor)
    VALUES (p_titulo, p_editora, p_edicao, p_anopublicacao, p_valor)
    RETURNING livro.codl INTO v_codl;

    -- cria relações: livro_autor
    IF p_autores IS NOT NULL THEN
        FOREACH v_id IN ARRAY p_autores LOOP
            INSERT INTO public.livro_autor (livro_cod, autor_codau)
            VALUES (v_codl, v_id)
            ON CONFLICT DO NOTHING;
        END LOOP;
    END IF;

    -- cria relações: livro_assunto
    IF p_assuntos IS NOT NULL THEN
        FOREACH v_id IN ARRAY p_assuntos LOOP
            INSERT INTO public.livro_assunto (livro_cod, assunto_codas)
            VALUES (v_codl, v_id)
            ON CONFLICT DO NOTHING;
        END LOOP;
    END IF;

    RETURN QUERY
    SELECT l.codl, l.titulo, l.editora, l.edicao, l.anopublicacao, l.valor
    FROM public.livro l
    WHERE l.codl = v_codl;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.fn_livro_list(
    _page integer DEFAULT 1,
    _page_size integer DEFAULT 10
)
 RETURNS TABLE(codl integer, titulo character varying, editora character varying, edicao integer, anopublicacao character varying, valor numeric, autores text, assuntos text)
 LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN QUERY
    SELECT
        l.codl,
        l.titulo,
        l.editora,
        l.edicao,
        l.anopublicacao,
        l.valor,
        COALESCE(string_agg(DISTINCT a.nome, ', ' ORDER BY a.nome), '') AS autores,
        COALESCE(string_agg(DISTINCT s.descricao, ', ' ORDER BY s.descricao), '') AS assuntos
    FROM public.livro l
    LEFT JOIN public.livro_autor la
        ON la.livro_cod = l.codl
    LEFT JOIN public.autor a
        ON a.codau = la.autor_codau
    LEFT JOIN public.livro_assunto las
        ON las.livro_cod = l.codl
    LEFT JOIN public.assunto s
        ON s.codas = las.assunto_codas	
	WHERE l.ativo = true
    GROUP BY
        l.codl, l.titulo, l.editora, l.edicao, l.anopublicacao, l.valor
    ORDER BY l.titulo
    LIMIT _page_size
    OFFSET ((_page - 1) * _page_size);
END;
$function$;

CREATE OR REPLACE FUNCTION public.fn_livro_update(p_codl integer, p_titulo character varying, p_editora character varying, p_edicao integer, p_anopublicacao character varying, p_valor numeric, p_autores integer[], p_assuntos integer[])
 RETURNS TABLE(codl integer, titulo character varying, editora character varying, edicao integer, anopublicacao character varying, valor numeric)
 LANGUAGE plpgsql
AS $function$
DECLARE
    v_id INTEGER;
BEGIN
    UPDATE public.livro
    SET
        titulo = p_titulo,
        editora = p_editora,
        edicao = p_edicao,
        anopublicacao = p_anopublicacao,
        valor = p_valor
    WHERE livro.codl = p_codl;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Livro com código % não encontrado', p_codl;
    END IF;

    -- substitui relações: livro_autor
    DELETE FROM public.livro_autor WHERE livro_cod = p_codl;

    IF p_autores IS NOT NULL THEN
        FOREACH v_id IN ARRAY p_autores LOOP
            INSERT INTO public.livro_autor (livro_cod, autor_codau)
            VALUES (p_codl, v_id)
            ON CONFLICT DO NOTHING;
        END LOOP;
    END IF;

    -- substitui relações: livro_assunto
    DELETE FROM public.livro_assunto WHERE livro_cod = p_codl;

    IF p_assuntos IS NOT NULL THEN
        FOREACH v_id IN ARRAY p_assuntos LOOP
            INSERT INTO public.livro_assunto (livro_cod, assunto_codas)
            VALUES (p_codl, v_id)
            ON CONFLICT DO NOTHING;
        END LOOP;
    END IF;

    RETURN QUERY
    SELECT l.codl, l.titulo, l.editora, l.edicao, l.anopublicacao, l.valor
    FROM public.livro l
    WHERE l.codl = p_codl;
END;
$function$
;