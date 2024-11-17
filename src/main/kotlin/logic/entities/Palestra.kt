package logic.entities

import logic.collections.FilaEspera
import logic.collections.ListaParticipantes
import util.SequentialId
import util.StatusEnum
import java.time.LocalDate
import java.time.LocalTime

class Palestra(
    private var titulo: String,
    private var palestrante: String,
    private var limiteParticipantes: Int,
    private var local: String,
    private var data: LocalDate,
    private var horarioInicio: LocalTime,
    private var horarioFim: LocalTime
) {
    private var id = SequentialId().gerarId()
    private var status = StatusEnum.PENDENTE
    private var participantes = ListaParticipantes()
    private var filaEspera = FilaEspera()

    fun getId(): Int {
        return id
    }

    fun getTitulo(): String {
        return titulo
    }

    fun getPalestrante(): String {
        return palestrante
    }

    fun getLimiteParticipantes(): Int {
        return limiteParticipantes
    }

    fun getLocal(): String {
        return local
    }

    fun getData(): LocalDate {
        return data
    }

    fun getStatus(): StatusEnum {
        return status
    }

    fun getDuracao(): Int {
        return horarioFim.hour - horarioInicio.hour
    }

    fun getHorarioInicio(): LocalTime {
        return horarioInicio
    }

    fun getHorarioFim(): LocalTime {
        return horarioFim
    }

    fun getParticipantes() {
//        return participantes.buscarTodosParticipantes()
    }

    fun getFilaEspera(): FilaEspera {
        return filaEspera
    }

    fun setHorarioInicio(horarioInicio: LocalTime) {
        this.horarioInicio = horarioInicio
    }

    fun setHorarioFim(horarioFim: LocalTime) {
        this.horarioFim = horarioFim
    }

    fun setStatus(status: StatusEnum) {
        this.status = status
    }
}
