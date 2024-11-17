package logic.entities

import logic.collections.AgendaPalestras
import util.SequentialId
import util.StatusEnum
import java.time.LocalDate

class Evento(
    private var nome: String,
    private var descricao: String,
    private var valorInscricao: Double,
    private var dataInicio: LocalDate,
    private var dataFim: LocalDate,

) {
    private var id = SequentialId().gerarId()
    private var status = StatusEnum.PENDENTE
    private var agenda = AgendaPalestras()

    fun getId(): Int {
        return id
    }

    fun getNome(): String {
        return nome
    }

    fun getDescricao(): String {
        return descricao
    }

    fun getDataInicio(): LocalDate {
        return dataInicio
    }

    fun getDataFim(): LocalDate {
        return dataFim
    }

    fun getValorInscricao(): Double {
        return valorInscricao
    }

    fun getStatus(): StatusEnum {
        return status
    }

    fun getAgenda(): AgendaPalestras {
        return agenda
    }
}