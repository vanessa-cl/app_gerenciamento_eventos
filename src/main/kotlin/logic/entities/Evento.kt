package logic.entities

import logic.collections.AgendaPalestras
import util.StatusEnum
import java.time.LocalDate

data class Evento(
    val id: Int,
    val nome: String,
    val descricao: String,
    val valorInscricao: Double,
    val dataInicio: LocalDate,
    val dataFim: LocalDate,
) {
    var status = StatusEnum.PENDENTE
    val agenda = AgendaPalestras()
}