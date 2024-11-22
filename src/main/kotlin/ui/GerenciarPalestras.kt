package ui

import logic.collections.ListaEventos
import logic.entities.Evento
import logic.entities.Palestra
import util.PrintTables
import util.SequentialId
import java.time.LocalDate
import java.time.LocalTime

class GerenciarPalestras {
    private var id = SequentialId()
    private lateinit var evento: Evento
    private var printTables = PrintTables()

    fun mostrarMenuGerenciarPalestras(todosEventos: ListaEventos) {
        println("Digite o ID do evento que deseja gerenciar as palestras:")
        val idEvento = readln().toInt()
        val eventoSelecionado = todosEventos.buscarEventoPeloId(idEvento)
        if (eventoSelecionado != null) {
            evento = eventoSelecionado
            var voltar = false
            while (!voltar) {
                println("Gerenciamento de Palestras do Evento ${evento.getNome()}")
                println("1 - Cadastrar nova palestra")
                println("2 - Listar palestras")
                println("3 - Listar palestras em ordem cronológica")
                println("4 - Cancelar palestra")
                println("5 - Atualizar horário de palestra")
                println("6 - Consultar participantes")
                println("7 - Consultar lista de espera")
                println("8 - Voltar")
                val opcao = readln().toInt()
                when (opcao) {
                    1 -> cadastrarPalestra()
                    2 -> exibirPalestras()
                    3 -> exibirPalestrasPorHorario()
                    4 -> cancelarPalestra()
                    5 -> atualizarHorarioPalestra()
                    6 -> consultarParticipantes()
                    7 -> consultarListaEspera()
                    8 -> voltar = true
                    else -> println("Opção inválida! Tente novamente")
                }
            }
        } else {
            println("Evento não encontrado")
        }
    }

    private fun cadastrarPalestra() {
//        val palestraTeste = Palestra(
//            id.gerarId(),
//            "Introdução ao Kotlin",
//            "Erica Meire",
//            50,
//            "H408",
//            LocalDate.parse("2024-10-06"),
//            LocalTime.parse("19:00"),
//            LocalTime.parse("20:00")
//        )
//        evento.getAgenda().inserirPalestra(palestraTeste)
        println("Digite o título da palestra:")
        val tituloPalestra = readln()
        println("Digite o nome do palestrante:")
        val nomePalestrante = readln()
        println("Digite a data da palestra (YYYY-MM-DD):")
        val dataPalestra = readln()
        println("Digite o local da palestra:")
        val localPalestra = readln()
        println("Digite o limite de participantes:")
        val limiteParticipantes = readln().toInt()
        println("Digite o horário de início (HH:MM):")
        val horarioInicio = readln()
        println("Digite o horário de término (HH:MM):")
        val horarioTermino = readln()
        val novaPalestra = Palestra(
            id.gerarId(),
            tituloPalestra,
            nomePalestrante,
            limiteParticipantes,
            localPalestra,
            LocalDate.parse(dataPalestra),
            LocalTime.parse(horarioInicio),
            LocalTime.parse(horarioTermino)
        )
        val sucesso = evento.getAgenda().inserirPalestra(novaPalestra)
        if (!sucesso) {
            println("Erro ao cadastrar palestra! Conflito de horários")
            return
        }
        println("Palestra cadastrada com sucesso!")
    }

    private fun exibirPalestras() {
        val palestras = evento.getAgenda().buscarTodasPalestras()
        if (palestras == null) {
            println("Não há palestras cadastradas para este evento")
            return
        }
        printTables.tablePalestras(palestras)
    }

    private fun exibirPalestrasPorHorario() {
        // TODO: implementar ordenação
        // printTables.tablePalestras()
    }

    private fun cancelarPalestra() {
        println("Digite o ID da palestra que deseja cancelar:")
        val idPalestra = readln().toInt()
        val palestraEncontrada = evento.getAgenda().buscarPalestraPeloId(idPalestra)
        if (palestraEncontrada == null) {
            println("Palestra não encontrada!")
            return
        }
        println("Tem certeza que deseja cancelar a palestra? (S/N)")
        val confirmacao = readln()
        if (confirmacao == "S") {
            val sucesso = evento.getAgenda().removerPalestraPeloId(idPalestra)
            if (!sucesso) {
                println("Erro ao cancelar palestra!")
                return
            }
            println("Palestra cancelada com sucesso!")
            return
        }
        if (confirmacao == "N") {
            return
        }


    }

    private fun atualizarHorarioPalestra() {
        // TODO: tirar dúvida sobre atualização de horário em caso de conflito (impedir ou reorganizar?)
        println("Digite o ID da palestra que deseja atualizar o horário:")
        val idPalestra = readln().toInt()
        val palestra = evento.getAgenda().buscarPalestraPeloId(idPalestra)
        if (palestra == null) {
            println("Palestra não encontrada!")
            return
        }
        println("Digite o novo horário de início (HH:MM):")
        val horarioInicio = readln()
        println("Digite o novo horário de término (HH:MM):")
        val horarioTermino = readln()
        val sucesso = evento.getAgenda()
            .atualizarHorarioPalestra(palestra, LocalTime.parse(horarioInicio), LocalTime.parse(horarioTermino))
        if (!sucesso) {
            println("Erro ao atualizar horário da palestra! Conflito de horários")
            return
        }
        println("Horário da palestra atualizado com sucesso!")
        return
    }

    private fun consultarParticipantes() {
        println("Digite o ID da palestra que deseja consultar os participantes:")
        val idPalestra = readln().toInt()
        val palestraEncontrada = evento.getAgenda().buscarPalestraPeloId(idPalestra)
        if (palestraEncontrada == null) {
            println("Palestra não encontrada!")
            return
        }
        val participantes = palestraEncontrada.getParticipantes().buscarTodosParticipantes()
        if (participantes == null) {
            println("Não há participantes inscritos nesta palestra")
            return
        }
        printTables.tableParticipantes(participantes)
    }

    private fun consultarListaEspera() {
        println("Digite o ID da palestra que deseja consultar a lista de espera:")
        val idPalestra = readln().toInt()
        val palestraEncontrada = evento.getAgenda().buscarPalestraPeloId(idPalestra)
        if (palestraEncontrada == null) {
            println("Palestra não encontrada!")
            return
        }
        val participantesFilaEspera = palestraEncontrada.getFilaEspera().buscarTodosParticipantes()
        if (participantesFilaEspera == null) {
            println("Não há participantes na lista de espera!")
            return
        }
        printTables.tableParticipantes(participantesFilaEspera)
    }

}