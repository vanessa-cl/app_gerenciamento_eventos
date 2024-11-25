package logic.entities

import logic.collections.FilaEspera
import logic.collections.ListaParticipantes
import util.StatusEnum
import java.time.LocalDate
import java.time.LocalTime

data class Palestra(
    val id: Int,
    val titulo: String,
    val palestrante: String,
    val limiteParticipantes: Int,
    val local: String,
    val data: LocalDate,
    var horarioInicio: LocalTime,
    var horarioFim: LocalTime
) {
    var status = StatusEnum.PENDENTE
    val participantes = ListaParticipantes(limiteParticipantes)
    val filaEspera = FilaEspera()

    fun getDuracao(): Int {
        return horarioFim.hour - horarioInicio.hour
    }
}
