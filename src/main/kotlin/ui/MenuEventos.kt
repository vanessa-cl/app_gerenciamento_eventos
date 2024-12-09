package ui

import logic.collections.ListaEventos
import logic.entities.Evento
import logic.entities.Participante
import util.NotificationThread
import java.time.LocalDate

class MenuEventos() {
//    private var todosEventos = ListaEventos()
//    private var id = SequentialId()
//    private var menuGerenciarPalestras = GerenciarPalestras()
//    private var printTables = PrintTables()
//    lateinit var thread: NotificationThread
//
//    fun mostrarMenuColaborador() {
//        var voltar = false
//        while (!voltar) {
//            println("Gerenciamento de Eventos")
//            println("1 - Cadastrar novo evento")
//            println("2 - Listar eventos")
//            println("3 - Gerenciar palestras")
//            println("4 - Voltar")
//            val opcao = readln().toInt()
//            when (opcao) {
//                1 -> cadastrarEvento()
//                2 -> exibirEventos()
//                3 -> menuGerenciarPalestras.mostrarMenuGerenciarPalestras(todosEventos)
//                4 -> voltar = true
//                else -> println("Opção inválida! Tente novamente")
//            }
//        }
//
//    }
//
//    private fun cadastrarEvento() {
//        val eventoTeste = Evento(
//            id.gerarId(),
//            "Conferência de Kotlin",
//            "Evento da linguagem Kotlin",
//            20.0,
//            LocalDate.parse("2024-10-01"),
//            LocalDate.parse("2024-10-08")
//        )
//        todosEventos.inserirEvento(eventoTeste)
////        println("Preencha os campos para cadastrar um novo evento:")
////        println("Nome do evento:")
////        val nome = readln()
////        println("Data de início:")
////        val dataInicio = readln()
////        println("Data de término:")
////        val dataTermino = readln()
////        println("Descrição:")
////        val descricao = readln()
////        println("Valor da inscrição:")
////        val valorInscricao = readln().toDouble()
////        val novoEvento = Evento(nome, descricao, valorInscricao, LocalDate.parse(dataInicio), LocalDate.parse(dataTermino))
////        todosEventos.inserirEvento(novoEvento)
//        println("Evento cadastrado com sucesso!")
//    }
//
//    private fun exibirEventos() {
//        println("Eventos cadastrados:")
//        val eventos = todosEventos.buscarTodosEventos()
//        if (eventos == null) {
//            println("Não há eventos cadastrados!")
//            return
//        }
//        printTables.tableEventos(eventos)
//    }
//
//    fun mostrarMenuOuvinte(participante: Participante) {
//        thread = NotificationThread(participante)
//        thread.start()
//        var voltar = false
//        println("Digite o ID de um dos eventos abaixo para consultar as opções:")
//        exibirEventos()
//        val entrada = readln().toInt()
//        val evento = todosEventos.buscarEventoPeloId(entrada)
//        if (evento != null) {
//            while (!voltar) {
//                println("Menu do Evento ${evento.getNome()}")
//                println("1 - Consultar agenda")
//                println("2 - Minhas inscrições")
//                println("3 - Voltar")
//                val opcao = readln().toInt()
//                when (opcao) {
//                    1 -> consultarAgenda(evento, participante)
//                    2 -> consultarInscricoes(evento, participante)
//                    3 -> voltar = true
//                    else -> println("Opção inválida! Tente novamente")
//                }
//            }
//        }
//    }
//
//    private fun consultarAgenda(evento: Evento, participante: Participante) {
//        println("Agenda do Evento ${evento.getNome()}")
//        val palestras = evento.getAgenda().buscarTodasPalestras()
//        if (palestras == null) {
//            println("Não há palestras cadastradas para este evento")
//            return
//        }
//        printTables.tablePalestras(palestras)
//        inscreverEmPalestra(evento, participante)
//    }
//
//    private fun inscreverEmPalestra(evento: Evento, participante: Participante) {
//        println("Digite o ID de uma palestra para se inscrever ou X para voltar:")
//        val entrada = readln()
//        if (entrada == "X") {
//            return
//        }
//        val palestra = evento.getAgenda().buscarPalestraPeloId(entrada.toInt())
//        if (palestra == null) {
//            println("Palestra não encontrada!")
//            return
//        }
//        val jaEstaInscritoPalestra = palestra.getParticipantes().buscarParticipantePeloId(participante.getId())
//        if (jaEstaInscritoPalestra != null) {
//            println("Você já está inscrito nesta palestra!")
//            return
//        }
//        if (palestra.getParticipantes().estaCheia()) {
//            println("A palestra ${palestra.getTitulo()} atingiu o limite de participantes. Deseja entrar na lista de espera? (S/N)")
//            val opcao = readln()
//            if (opcao == "S") {
//                val jaEstaNaFilaEspera = palestra.getFilaEspera().buscarParticipantePeloId(participante.getId())
//                if (jaEstaNaFilaEspera != null) {
//                    println("Você já está na lista de espera desta palestra!")
//                    return
//                }
//                palestra.getFilaEspera().inserirParticipanteFim(participante)
//                println("Inscrição na lista de espera realizada com sucesso!")
//                return
//            }
//            return
//        }
//        val sucessoPalestra = palestra.getParticipantes().inserirParticipante(participante)
//        val sucessoParticipante = participante.getPalestrasInscritas().inserirPalestra(palestra)
//        if (!sucessoPalestra && !sucessoParticipante) {
//            println("Erro ao se inscrever na palestra!")
//            return
//        }
//        println("Inscrição realizada com sucesso!")
//        return
//    }
//
//    private fun consultarInscricoes(evento: Evento, participante: Participante) {
//        println("Minhas inscrições no Evento ${evento.getNome()}:")
//        val inscricoes = participante.getPalestrasInscritas().buscarTodasPalestras()
//        if (inscricoes == null) {
//            println("Você não está inscrito em nenhuma palestra")
//            return
//        }
//        printTables.tablePalestras(inscricoes)
//        cancelarInscricao(evento, participante)
//    }
//
//    private fun cancelarInscricao(evento: Evento, participante: Participante) {
//        println("Digite o ID de uma palestra para cancelar a inscrição ou X para voltar:")
//        var entrada = readln()
//        if (entrada == "X") {
//            return
//        }
//        val palestra = evento.getAgenda().buscarPalestraPeloId(entrada.toInt())
//        if (palestra == null) {
//            println("Palestra não encontrada!")
//            return
//        }
//        val naoEstaInscrito = palestra.getParticipantes().buscarParticipantePeloId(participante.getId())
//        if (naoEstaInscrito == null) {
//            println("Você não está inscrito nesta palestra!")
//            return
//        }
//        println("Tem certeza que deseja cancelar a inscrição na palestra ${palestra.getTitulo()}? (S/N)")
//        entrada = readln()
//        if (entrada == "N") {
//            return
//        }
//        val sucessoPalestra = palestra.getParticipantes().removerParticipantePeloId(participante.getId())
//        val sucessoParticipante =
//            participante.getPalestrasInscritas().removerPalestraPeloId(palestra.getId())
//        if (!sucessoPalestra && !sucessoParticipante) {
//            println("Erro ao cancelar inscrição!")
//            return
//        }
//        println("Inscrição cancelada com sucesso!")
//        if (palestra.getFilaEspera().estaVazia()) {
//            return
//        }
//        val participanteFilaEspera = palestra.getFilaEspera().removerParticipanteInicio() ?: return
//        palestra.getParticipantes().inserirParticipante(participanteFilaEspera)
//        thread.notificarUsuario("Você foi inscrito na palestra ${palestra.getTitulo()}!", participanteFilaEspera)
//        return
//    }
}