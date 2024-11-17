package ui

import logic.entities.Evento
import logic.entities.Palestra
import java.time.LocalDate
import java.time.LocalTime

class GerenciarPalestras(private val evento: Evento) {

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
        val palestraTeste = Palestra(
            "Introdução ao Kotlin",
            "Erica Meire",
            50,
            "H408",
            LocalDate.parse("2024-10-06"),
            LocalTime.parse("19:00"),
            LocalTime.parse("20:00")
        )
        evento.getAgenda().inserirPalestra(palestraTeste)
//        println("Digite o título da palestra:")
//        val tituloPalestra = readln()
//        println("Digite o nome do palestrante:")
//        val nomePalestrante = readln()
//        println("Digite a data da palestra (YYYY-MM-DD):")
//        val dataPalestra = readln()
//        println("Digite o local da palestra:")
//        val localPalestra = readln()
//        println("Digite o limite de participantes:")
//        val limiteParticipantes = readln().toInt()
//        println("Digite o horário de início:")
//        val horarioInicio = readln()
//        println("Digite o horário de término:")
//        val horarioTermino = readln()
//        val novaPalestra = Palestra(
//            tituloPalestra,
//            nomePalestrante,
//            limiteParticipantes,
//            localPalestra,
//            LocalDate.parse(dataPalestra),
//            LocalTime.parse(horarioInicio),
//            LocalTime.parse(horarioTermino)
//        )
//        evento.getAgenda().inserirPalestra(novaPalestra)
//        println("Palestra cadastrada com sucesso!")
    }

    fun exibirPalestras() {
        val palestras = evento.getAgenda().buscarTodasPalestras()
        for (palestra in palestras) {
            println("Título: ${palestra?.getTitulo()}")
//            println("Palestrante: ${palestra?.getPalestrante()}")
//            println("Data: ${palestra?.getData()}")
//            println("Local: ${palestra?.getLocal()}")
//            println("Horário de início: ${palestra?.getHorarioInicio()}")
//            println("Horário de término: ${palestra?.getHorarioFim()}")
//            println("Limite de participantes: ${palestra?.getLimiteParticipantes()}")
            println("_________________________________________________________")
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
        evento.getAgenda().removerPalestraPeloTitulo(tituloPalestra)
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
            palestra.setHorarioInicio(LocalTime.parse(horarioInicio))
            palestra.setHorarioFim(LocalTime.parse(horarioTermino))
            println("Horário da palestra atualizado com sucesso!")
        }
    }

    fun consultarParticipantes() {
        println("Digite o título da palestra que deseja consultar os participantes:")
        val tituloPalestra = readln()
        val palestra = evento.getAgenda().buscarPalestraPeloTitulo(tituloPalestra)
        if (palestra != null) {
            val participantes = palestra.getParticipantes()
//            for (participante in participantes) {
//                println("Nome: ${participante?.getNome()}")
//                println("_________________________________________________________")
//            }
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
            for (participante in filaEspera.buscarTodosParticipantes()) {
                println("Nome: ${participante?.getNome()}")
                println("_________________________________________________________")
            }
        }
    }
}