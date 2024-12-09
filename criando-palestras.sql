INSERT INTO palestras (idEvento, titulo, palestrante, limiteParticipantes, local, data, horarioInicio, horarioFim, status) VALUES 
(1,'Introdução ao Kotlin','Erica Meire',50,'H408','2024-11-04','19:00','20:00','PENDENTE'),
(1,'Desenvolvimento de Aplicações Android com Kotlin','João Manoel',50,'H408','2024-11-05','18:00','20:00','PENDENTE')

SELECT * FROM palestras

DELETE FROM palestras WHERE id = 2 