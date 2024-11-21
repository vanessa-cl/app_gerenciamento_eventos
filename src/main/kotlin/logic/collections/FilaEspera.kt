package logic.collections

import logic.entities.Participante
import logic.structures.FilaEstatica
import util.convertArray

class FilaEspera {
    private var participantes: FilaEstatica<Participante> = FilaEstatica()


    fun inserirParticipanteFim(participante: Participante) {
        participantes.enfileirar(participante)
    }

    fun removerParticipanteInicio(): Participante? {
        return participantes.desenfileirar()
    }

    fun buscarParticipanteInicio(): Participante? {
        return participantes.frente()
    }

    fun buscarTodosParticipantes(): Array<Participante?>? {
        if (participantes.estaVazia()) {
            return null
        }
        val todosParticipantes = participantes.buscarDados()
        val participantesArray = convertArray<Participante?>(todosParticipantes)
        return participantesArray
    }

    fun estaCheia(): Boolean {
        return participantes.estaCheia()
    }

    fun estaVazia(): Boolean {
        return participantes.estaVazia()
    }

}
