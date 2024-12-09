package logic.database

import logic.collections.ListaParticipantes

interface IParticipanteDAO {
    fun getParticipantes(): ListaParticipantes
}

class ParticipanteDAO {
}