package util

import logic.entities.Evento
import logic.entities.Palestra
import logic.entities.Participante

class PrintTables {

    fun tableEventos(eventos: Array<Evento?>) {
        println("_".repeat(130))
        println("ID | Nome                     | Data de início | Data de término | Descrição                  | Valor da inscrição | Status       ")
        println("_".repeat(130))
        for (evento in eventos) {
            if (evento == null) {
                continue
            }
            println(
                "${evento.getId()}  | ${evento.getNome()}    | ${evento.getDataInicio()}     | ${evento.getDataFim()}      " +
                        "| ${evento.getDescricao()} | ${evento.getValorInscricao()}               | ${evento.getStatus()}       "
            )
            println("_".repeat(130))
        }
    }

    fun tableParticipantes(participantes: Array<Participante?>) {
        println("_".repeat(100))
        println("ID | Nome              | Email                     | CPF             | Cargo ")
        println("_".repeat(100))
        for (participante in participantes) {
            if (participante == null) {
                continue
            }
            println("${participante.getId()}  | ${participante.getNome()}        | ${participante.getEmail()}    | ${participante.getCpf()}  | ${participante.getCargo()}")
            println("_".repeat(100))
        }
    }

    fun tablePalestras(palestras: Array<Palestra?>) {
        println("_".repeat(120))
        println("ID | Título                  | Palestrante     | Local | Data       | Início | Término | Limite Part. | Status ")
        println("_".repeat(120))
        for (palestra in palestras) {
            if (palestra == null) {
                continue
            }
            println(
                "${palestra.getId()}  | ${palestra.getTitulo()}    | ${palestra.getPalestrante()}     | ${palestra.getLocal()}  " +
                        "| ${palestra.getData()} | ${palestra.getHorarioInicio()}  | ${palestra.getHorarioFim()}   | ${palestra.getLimiteParticipantes()}           " +
                        "| ${palestra.getStatus()}"
            )
            println("_".repeat(120))
        }
    }
}

