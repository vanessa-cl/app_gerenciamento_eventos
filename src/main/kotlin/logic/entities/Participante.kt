package logic.entities

import logic.collections.AgendaPalestras
import util.CargoEnum
import util.SequentialId

class Participante(
    private var id: Int,
    private var nome: String,
    private var email: String,
    private var cpf: String,
    private var cargo: CargoEnum
) {
    private var agenda: AgendaPalestras = AgendaPalestras()

    fun getId(): Int {
        return id
    }

    fun getNome(): String {
        return nome
    }

    fun getEmail(): String {
        return email
    }

    fun getCpf(): String {
        return cpf
    }

    fun getCargo(): CargoEnum {
        return cargo
    }

    fun getPalestrasInscritas(): AgendaPalestras {
        return agenda
    }

}