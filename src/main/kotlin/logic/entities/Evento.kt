package logic.entities

import logic.collections.AgendaPalestras
import util.enums.StatusEnum
import util.enums.TurnoEnum
import java.time.LocalDate

data class Evento(
    val id: Int,
    val nome: String,
    val descricao: String,
    val valorInscricao: Double,
    val dataInicio: LocalDate,
    val dataFim: LocalDate,
    val status: StatusEnum = StatusEnum.PENDENTE,
    val turno: TurnoEnum
) {
    val agenda = AgendaPalestras()

    override fun toString(): String {
        return "$id,$nome,$descricao,$valorInscricao,$dataInicio,$dataFim,$status,$turno"
    }
}