package logic.collections

class FilaEspera : FilaEstatica<Participante>() {
    private var participantes: FilaEstatica<Participante> = FilaEstatica()


    fun inserirParticipanteFim(participante: Participante) {
        participantes.enfileirar(participante)
    }

    fun removerParticipanteIniciopeloNome(nome: String): Participante? {
        getNome().equals(nome)
        return participantes.desenfileirar()
    }


    fun removerParticipanteIniciopeloId(id: Int): Participante? {
        getId().equals(id)
        return participantes.desenfileirar()
    }


    fun buscarParticipantePeloNome(nome: String): Participante? {
        for (i in 0 until participantes.tamanho) {
            val posicaoReal = (participantes.ponteiroInicio + i) % participantes.tamanho
            if (participantes.dados[posicaoReal]?.getNome().equals(nome)) {
                return participantes.dados[posicaoReal]
            }
        }
        return null
    }


    fun buscarParticipantePeloId(id: Int): Participante? {
        for (i in 0 until participantes.tamanho) {
            val posicaoReal = (participantes.ponteiroInicio + i) % participantes.tamanho
            if (participantes.dados[posicaoReal]?.getId().equals(id)) {
                return participantes.dados[posicaoReal]
            }
        }
        return null
    }


    fun buscarTodosOsParticipantes(): Array<Participante?> {
        return participantes.dados
    }


    fun estaVazia(): Boolean {
        return participantes.estaVazia()
    }


}
