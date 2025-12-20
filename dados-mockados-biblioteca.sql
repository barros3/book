-- -- INICIO (SE PRECISAR)
-- -- ESSE TRECHO DELETA E RESETA AS SEQUENCES
-- -- QUANDO A BASE JÁ FOI UTILIZADA.
-- 
-- -- LIMPAR TABELAS
-- DELETE FROM public.livro_assunto;
-- DELETE FROM public.livro_autor;
-- DELETE FROM public.livro;
-- DELETE FROM public.autor;
-- DELETE FROM public.assunto;
-- 
-- -- RESETAR SEQUENCES
-- ALTER SEQUENCE public.autor_codau_seq RESTART WITH 1;
-- ALTER SEQUENCE public.assunto_codas_seq RESTART WITH 1;
-- ALTER SEQUENCE public.livro_codl_seq RESTART WITH 1;
-- -- FIM (SE PRECISAR)

-- 1. INSERIR AUTORES (20 AUTORES)
INSERT INTO public.autor (nome) VALUES
('Fiódor Dostoiévski'),
('Machado de Assis'),
('Clarice Lispector'),
('José Saramago'),
('Jorge Amado'),
('Gabriel García Márquez'),
('J.K. Rowling'),
('George Orwell'),
('Jane Austen'),
('William Shakespeare'),
('Liev Tolstói'),
('Franz Kafka'),
('Virginia Woolf'),
('Edgar Allan Poe'),
('Agatha Christie'),
('Stephen King'),
('Miguel de Cervantes'),
('Homer'),
('Friedrich Nietzsche'),
('Sun Tzu');

-- 2. INSERIR ASSUNTOS (20 ASSUNTOS)
INSERT INTO public.assunto (descricao) VALUES
('Romance'),
('Ficção Científica'),
('Fantasia'),
('Suspense'),
('Terror'),
('Filosofia'),
('Poesia'),
('Teatro'),
('Biografia'),
('História'),
('Autoajuda'),
('Policial'),
('Distopia'),
('Realismo Mágico'),
('Psicologia'),
('Sociologia'),
('Economia'),
('Arte'),
('Religião'),
('Mitologia');

-- 3. INSERIR LIVROS (20 LIVROS)
INSERT INTO public.livro (titulo, editora, edicao, anopublicacao, valor) VALUES
('Crime e Castigo', 'Editora 34', 1, '2010', 59.90),
('Memórias Póstumas de Brás Cubas', 'Companhia das Letras', 3, '2012', 45.50),
('A Hora da Estrela', 'Rocco', 2, '2015', 39.90),
('Ensaio sobre a Cegueira', 'Companhia das Letras', 1, '2016', 52.00),
('Capitães da Areia', 'Record', 5, '2014', 48.75),
('Cem Anos de Solidão', 'Record', 10, '2018', 65.00),
('Harry Potter e a Pedra Filosofal', 'Rocco', 1, '2017', 42.90),
('1984', 'Companhia das Letras', 2, '2019', 38.50),
('Orgulho e Preconceito', 'Martin Claret', 4, '2020', 34.90),
('Hamlet', 'Penguin', 3, '2021', 41.25),
('Guerra e Paz', 'Cosac Naify', 1, '2013', 89.90),
('A Metamorfose', 'Companhia das Letras', 2, '2011', 29.90),
('Mrs. Dalloway', 'Autêntica', 1, '2014', 47.80),
('Os Crimes da Rua Morgue', 'L&PM', 1, '2016', 32.50),
('O Assassinato no Expresso do Oriente', 'HarperCollins', 2, '2017', 36.90),
('It: A Coisa', 'Suma', 1, '2018', 79.90),
('Dom Quixote', 'Editora 34', 2, '2019', 68.50),
('Odisseia', 'Penguin', 1, '2020', 55.00),
('Assim Falou Zaratustra', 'Companhia das Letras', 1, '2021', 49.90),
('A Arte da Guerra', 'Jardim dos Livros', 1, '2022', 27.90);

-- 4. ASSOCIAR AUTORES AOS LIVROS (usar IDs gerados - 1 a 20)
INSERT INTO public.livro_autor (livro_cod, autor_codau) VALUES
(1, 1),   -- Livro 1 (Crime e Castigo) -> Autor 1 (Dostoiévski)
(2, 2),   -- Livro 2 -> Autor 2
(3, 3),   -- Livro 3 -> Autor 3
(4, 4),   -- Livro 4 -> Autor 4
(5, 5),   -- Livro 5 -> Autor 5
(6, 6),   -- Livro 6 -> Autor 6
(7, 7),   -- Livro 7 -> Autor 7
(8, 8),   -- Livro 8 -> Autor 8
(9, 9),   -- Livro 9 -> Autor 9
(10, 10), -- Livro 10 -> Autor 10
(11, 11), -- Livro 11 -> Autor 11
(12, 12), -- Livro 12 -> Autor 12
(13, 13), -- Livro 13 -> Autor 13
(14, 14), -- Livro 14 -> Autor 14
(15, 15), -- Livro 15 -> Autor 15
(16, 16), -- Livro 16 -> Autor 16
(17, 17), -- Livro 17 -> Autor 17
(18, 18), -- Livro 18 -> Autor 18
(19, 19), -- Livro 19 -> Autor 19
(20, 20); -- Livro 20 -> Autor 20

-- 5. ASSOCIAR ASSUNTOS AOS LIVROS
INSERT INTO public.livro_assunto (livro_cod, assunto_codas) VALUES
(1, 1), (1, 15),   -- Livro 1 -> Romance, Psicologia
(2, 1), (2, 6),    -- Livro 2 -> Romance, Filosofia
(3, 1),            -- Livro 3 -> Romance
(4, 1), (4, 13),   -- Livro 4 -> Romance, Distopia
(5, 1),            -- Livro 5 -> Romance
(6, 1), (6, 14),   -- Livro 6 -> Romance, Realismo Mágico
(7, 3),            -- Livro 7 -> Fantasia
(8, 2), (8, 13),   -- Livro 8 -> Ficção Científica, Distopia
(9, 1),            -- Livro 9 -> Romance
(10, 8),           -- Livro 10 -> Teatro
(11, 1), (11, 10), -- Livro 11 -> Romance, História
(12, 1), (12, 6),  -- Livro 12 -> Romance, Filosofia
(13, 1),           -- Livro 13 -> Romance
(14, 4), (14, 12), -- Livro 14 -> Suspense, Policial
(15, 12),          -- Livro 15 -> Policial
(16, 5),           -- Livro 16 -> Terror
(17, 1),           -- Livro 17 -> Romance
(18, 7), (18, 20), -- Livro 18 -> Poesia, Mitologia
(19, 6),           -- Livro 19 -> Filosofia
(20, 6), (20, 10); -- Livro 20 -> Filosofia, História

-- 6. SIMULA TRIGGER
REFRESH MATERIALIZED VIEW public.vw_relatorio_geral_autor;

-- 7. VERIFICAÇÃO
SELECT 'Autores: ' || COUNT(*) FROM public.autor;
SELECT 'Assuntos: ' || COUNT(*) FROM public.assunto;
SELECT 'Livros: ' || COUNT(*) FROM public.livro;
SELECT 'Autor-Livro: ' || COUNT(*) FROM public.livro_autor;
SELECT 'Assunto-Livro: ' || COUNT(*) FROM public.livro_assunto;