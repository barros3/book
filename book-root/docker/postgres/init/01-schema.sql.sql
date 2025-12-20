-- ===============================
-- TABELAS PRINCIPAIS
-- ===============================

CREATE TABLE IF NOT EXISTS public.autor (
	codau serial4 NOT NULL,
	nome varchar(40) NOT NULL,
	ativo bool DEFAULT true NULL,
	CONSTRAINT autor_pkey PRIMARY KEY (codau)
);

CREATE TABLE IF NOT EXISTS public.assunto (
	codas serial4 NOT NULL,
	descricao varchar(20) NOT NULL,
	ativo bool DEFAULT true NULL,
	CONSTRAINT assunto_pkey PRIMARY KEY (codas)
);

CREATE TABLE IF NOT EXISTS public.livro (
	codl serial4 NOT NULL,
	titulo varchar(40) NOT NULL,
	editora varchar(40) NOT NULL,
	edicao int4 NOT NULL,
	anopublicacao varchar(4) NOT NULL,
	valor numeric(12, 2) NOT NULL,
	ativo bool DEFAULT true NULL,
	CONSTRAINT livro_anopublicacao_check CHECK (((anopublicacao)::text ~ '^[0-9]{4}$'::text)),
	CONSTRAINT livro_edicao_check CHECK ((edicao > 0)),
	CONSTRAINT livro_pkey PRIMARY KEY (codl),
	CONSTRAINT livro_valor_check CHECK ((valor > (0)::numeric))
);

CREATE TABLE IF NOT EXISTS public.livro_assunto (
	livro_cod int4 NOT NULL,
	assunto_codas int4 NOT NULL,
	CONSTRAINT livro_assunto_pkey PRIMARY KEY (livro_cod, assunto_codas),
	CONSTRAINT livro_assunto_assunto_codas_fkey FOREIGN KEY (assunto_codas) REFERENCES public.assunto(codas),
	CONSTRAINT livro_assunto_livro_cod_fkey FOREIGN KEY (livro_cod) REFERENCES public.livro(codl) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS public.livro_autor (
	livro_cod int4 NOT NULL,
	autor_codau int4 NOT NULL,
	CONSTRAINT livro_autor_pkey PRIMARY KEY (livro_cod, autor_codau),
	CONSTRAINT livro_autor_autor_codau_fkey FOREIGN KEY (autor_codau) REFERENCES public.autor(codau),
	CONSTRAINT livro_autor_livro_cod_fkey FOREIGN KEY (livro_cod) REFERENCES public.livro(codl) ON DELETE CASCADE
);