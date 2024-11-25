package logic.entities

import logic.collections.AgendaPalestras
import util.CargoEnum

data class Participante(
    val id: Int,
    val nome: String,
    val email: String,
    val cpf: String,
    val cargo: CargoEnum
) {
    val agenda: AgendaPalestras = AgendaPalestras()
}