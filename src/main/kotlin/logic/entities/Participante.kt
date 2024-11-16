package logic.entities

import logic.collections.AgendaPalestras

class Participante(
    private var id: Int,
    private var nome: String,
    private var email: String,
    private var cpf: String,
    private var cargo: String
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

    fun getCargo(): String {
        return cargo
    }

    fun getPalestrasInscritas(): AgendaPalestras {
        return agenda
    }

}