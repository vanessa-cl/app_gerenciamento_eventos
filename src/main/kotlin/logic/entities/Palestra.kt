package logic.entities

import logic.collections.FilaEspera
import logic.collections.ListaParticipantes
import util.enums.StatusEnum
import java.time.LocalDate
import java.time.LocalTime

data class Palestra(
    var id: Int,
    val titulo: String,
    val palestrante: String,
    val limiteParticipantes: Int,
    val local: String,
    val data: LocalDate,
    var horarioInicio: LocalTime,
    var horarioFim: LocalTime,
    var status: StatusEnum = StatusEnum.PENDENTE
) {
    val participantes = ListaParticipantes(limiteParticipantes)
    val filaEspera = FilaEspera()

    fun getDuracao(): Int {
        return horarioFim.hour - horarioInicio.hour
    }

    override fun toString(): String {
        return "$id,$titulo,$palestrante,$limiteParticipantes,$local,$data,$horarioInicio,$horarioFim,$status"
    }
}
