package util.ui

import logic.database.PalestraDAO
import logic.database.ParticipanteDAO
import logic.entities.Evento
import logic.entities.Participante

fun loadDataDB(
    usuarioLogado: Participante,
    evento: Evento,
    palestraDAO: PalestraDAO,
    participanteDAO: ParticipanteDAO
) {
    usuarioLogado.inscricoes = palestraDAO.getSubscriptionsParticipante(evento.id, usuarioLogado.id)
    val palestras = palestraDAO.getPalestras(evento.id)
    evento.agenda = palestras
    val todasPalestras = palestras.buscarTodasPalestras()

    for (palestra in todasPalestras!!) {
        if (palestra != null) {
            val participantes =
                participanteDAO.getParticipantesPalestra(palestra.id, palestra.participantes.limiteParticipantes)
            palestra.participantes = participantes
            val filaEspera = participanteDAO.getParticipantesFilaEspera(palestra.id)
            palestra.filaEspera = filaEspera
        }
    }
}