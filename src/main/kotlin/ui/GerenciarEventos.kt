package ui

import logic.collections.ListaEventos
import logic.entities.Evento
import java.time.LocalDate

class GerenciarEventos {
    var todosEventos = ListaEventos()

    fun mostrarMenuColaborador() {
        println("Gerenciamento de Eventos")
        println("1 - Cadastrar novo evento")
        println("2 - Listar eventos")
        println("3 - Gerenciar palestras")
    }

    fun cadastrarEvento() {
        val eventoTeste = Evento(
            "Conferência de Kotlin",
            "Evento para reunir desenvolvedores e entusiastas da linguagem de programação Kotlin",
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
        for (evento in eventos) {
            println("Nome: ${evento?.getNome()}")
//            println("Data de início: ${evento?.getDataInicio()}")
//            println("Data de término: ${evento?.getDataFim()}")
//            println("Descrição: ${evento?.getDescricao()}")
//            println("Valor da inscrição: ${evento?.getValorInscricao()}")
            println("_________________________________________________________")
        }
    }
}