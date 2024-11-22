package logic.collections

import logic.entities.Participante
import logic.structures.ListaEstatica
import util.convertArray

class ListaParticipantes(private var limiteParticipantes: Int) {
    private var participantes = ListaEstatica<Participante>(limiteParticipantes)

    fun inserirParticipante(participante: Participante): Boolean {
        if (participantes.estaCheia()) {
            return false
        }
        participantes.anexar(participante)
        return true
    }

    fun removerParticipantePeloNome(nome: String): Boolean {
        val participante = buscarParticipantePeloNome(nome)
        if (participante != null) {
            participantes.apagar(participantes.buscarPosicao(participante))
            return true
        }
        return false
    }

    fun removerParticipantePeloId(id: Int): Boolean {
        val participante = buscarParticipantePeloId(id)
        if (participante != null) {
            participantes.apagar(participantes.buscarPosicao(participante))
            return true
        }
        return false
    }

    fun buscarParticipantePeloNome(nome: String): Participante? {
        for (i in 0 until participantes.selecionarTodos().size) {
            val participante = participantes.selecionar(i)
            if (participante != null && participante.getNome() == nome) {
                return participante
            }
        }
        return null
    }

    fun buscarParticipantePeloId(id: Int): Participante? {
        for (i in 0 until participantes.selecionarTodos().size) {
            val participante = participantes.selecionar(i)
            if (participante != null && participante.getId() == id) {
                return participante
            }
        }
        return null
    }

    fun buscarTodosParticipantes(): Array<Participante?>? {
        if (participantes.estaVazia()) {
            return null
        }
        val todosParticipantes = participantes.selecionarTodos()
        val participantesConv = convertArray<Participante?>(todosParticipantes)
        return participantesConv
    }

    fun estaCheia(): Boolean {
        return participantes.estaCheia()
    }

    fun estaVazia(): Boolean {
        return participantes.estaVazia()
    }

}