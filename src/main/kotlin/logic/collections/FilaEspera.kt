package logic.collections

import logic.entities.Participante
import logic.structures.FilaEstatica

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

    fun buscarTodosParticipantes(): Array<Participante?> {
        return participantes.buscarDados()
    }

    fun estaCheia(): Boolean {
        return participantes.estaCheia()
    }

    fun estaVazia(): Boolean {
        return participantes.estaVazia()
    }

}
