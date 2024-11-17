package ui

import logic.collections.ListaEventos
import logic.entities.Evento
import util.SequentialId
import java.time.LocalDate

class GerenciarEventos {
    var todosEventos = ListaEventos()
    private var id = SequentialId()

    fun mostrarMenuColaborador() {
        println("Gerenciamento de Eventos")
        println("1 - Cadastrar novo evento")
        println("2 - Listar eventos")
        println("3 - Gerenciar palestras")
        println("4 - Encerrar programa")
    }

    fun cadastrarEvento() {
        val eventoTeste = Evento(
            id.gerarId(),
            "Conferência de Kotlin",
            "Evento da linguagem Kotlin",
            20.0,
            LocalDate.parse("2024-10-01"),
            LocalDate.parse("2024-10-08")
        )
        todosEventos.inserirEvento(eventoTeste)
//        println("Preencha os campos para cadastrar um novo evento:")
//        println("Nome do evento:")
//        val nome = readln()
//        println("Data de início:")
//        val dataInicio = readln()
//        println("Data de término:")
//        val dataTermino = readln()
//        println("Descrição:")
//        val descricao = readln()
//        println("Valor da inscrição:")
//        val valorInscricao = readln().toDouble()
//        val novoEvento = Evento(nome, descricao, valorInscricao, LocalDate.parse(dataInicio), LocalDate.parse(dataTermino))
//        todosEventos.inserirEvento(novoEvento)
        println("Evento cadastrado com sucesso!")
    }

    fun exibirEventos() {
        println("Eventos cadastrados:")
        val eventos = todosEventos.buscarTodosEventos()
        println("_______________________________________________________________________________________________________________________________")
        println("ID | Nome                     | Data de início | Data de término | Descrição                  | Valor da inscrição")
        println("_______________________________________________________________________________________________________________________________")
        for (evento in eventos) {
            if (evento == null) {
                continue
            }
            println("${evento.getId()}  | ${evento.getNome()}    | ${evento.getDataInicio()}     | ${evento.getDataFim()}      | ${evento.getDescricao()} | ${evento.getValorInscricao()}")
            println("_______________________________________________________________________________________________________________________________")
        }
    }
}