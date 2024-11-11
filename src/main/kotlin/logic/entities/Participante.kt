package logic.entities

class Participante {
    private var Int id
    private var String nome
    private var String email
    private var String cpf
    private var String cargo
    private var AgendaPalestras agenda


fun constructor(nome: String, email: String, cpf: String, cargo: String) {
    this.nome = nome
    this.email = email
    this.cpf = cpf
    this.cargo = cargo

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
