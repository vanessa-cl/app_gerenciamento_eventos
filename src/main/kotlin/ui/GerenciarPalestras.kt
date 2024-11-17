package ui

import logic.entities.Evento
import logic.entities.Palestra
import util.SequentialId
import java.time.LocalDate
import java.time.LocalTime

class GerenciarPalestras(private val evento: Evento) {
    private var id = SequentialId()

    fun mostrarMenuPalestras() {
        println("Gerenciamento de Palestras do Evento ${evento.getNome()}")
        println("1 - Cadastrar nova palestra")
        println("2 - Listar palestras")
        println("3 - Cancelar palestra")
        println("4 - Atualizar horário de palestra")
        println("5 - Consultar participantes")
        println("6 - Consultar lista de espera")
        println("7 - Voltar")
    }

    fun cadastrarPalestra() {
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
        println("Digite o horário de início:")
        val horarioInicio = readln()
        println("Digite o horário de término:")
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

    fun exibirPalestras() {
        val palestras = evento.getAgenda().buscarTodasPalestras()
        println("_______________________________________________________________________________________________________________________________")
        println("ID | Título                  | Palestrante     | Local | Data       | Início | Término | Limite Part. | Status ")
//      println("1  | Introdução ao Kotlin    | Erica Meire     | H408  | 2024-12-01 | 19:00  | 20:00   | 50           | Pendente")
        println("_______________________________________________________________________________________________________________________________")
        for (palestra in palestras) {
            if (palestra == null) {
                continue
            }
            println("${palestra.getId()}  | ${palestra.getTitulo()}    | ${palestra.getPalestrante()}     | ${palestra.getLocal()}  | ${palestra.getData()} | ${palestra.getHorarioInicio()}  | ${palestra.getHorarioFim()}   | ${palestra.getLimiteParticipantes()}           | ${palestra.getStatus()}")
            println("_______________________________________________________________________________________________________________________________")
        }
    }

    fun cancelarPalestra() {
        println("Digite o título da palestra que deseja cancelar:")
        val tituloPalestra = readln()
        println("Tem certeza que deseja cancelar a palestra? (S/N)")
        val confirmacao = readln()
        if (confirmacao == "N") {
            return
        }
        val sucesso = evento.getAgenda().removerPalestraPeloTitulo(tituloPalestra)
        if (sucesso == null) {
            println("Erro ao cancelar palestra! Palestra não encontrada")
            return
        }
        println("Palestra cancelada com sucesso!")
    }

    fun atualizarHorarioPalestra() {
        println("Digite o título da palestra que deseja atualizar o horário:")
        val tituloPalestra = readln()
        val palestra = evento.getAgenda().buscarPalestraPeloTitulo(tituloPalestra)
        if (palestra != null) {
            println("Digite o novo horário de início:")
            val horarioInicio = readln()
            println("Digite o novo horário de término:")
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
        println("Palestra não encontrada")
    }

    fun consultarParticipantes() {
        println("Digite o título da palestra que deseja consultar os participantes:")
        val tituloPalestra = readln()
        val palestra = evento.getAgenda().buscarPalestraPeloTitulo(tituloPalestra)
        if (palestra != null) {
            val participantes = palestra.getParticipantes().buscarTodosParticipantes()
            println("_______________________________________________________________________________________________________________________________")
            println("ID | Nome              | Email                     | CPF             | Cargo ")
            println("_______________________________________________________________________________________________________________________________")
            for (participante in participantes) {
                if (participante == null) {
                    continue
                }
                println("${participante.getId()}  | ${participante.getNome()}        | ${participante.getEmail()}    | ${participante.getCpf()}  | ${participante.getCargo()}")
            }
        }
    }

    fun consultarListaEspera() {
        println("Digite o título da palestra que deseja consultar a lista de espera:")
        val tituloPalestra = readln()
        val palestra = evento.getAgenda().buscarPalestraPeloTitulo(tituloPalestra)
        if (palestra != null) {
            val filaEspera = palestra.getFilaEspera()
            if (filaEspera.estaVazia()) {
                println("Não há participantes na lista de espera")
                return
            }
            val participantes = palestra.getParticipantes().buscarTodosParticipantes()
            println("_______________________________________________________________________________________________________________________________")
            println("ID | Nome              | Email                     | CPF             | Cargo ")
            println("_______________________________________________________________________________________________________________________________")
            for (participante in participantes) {
                if (participante == null) {
                    continue
                }
                println("${participante.getId()}  | ${participante.getNome()}        | ${participante.getEmail()}    | ${participante.getCpf()}  | ${participante.getCargo()}")
            }
        }
    }
}