package logic.database

import logic.structures.ListaEstatica
import util.enums.TurnoEnum

interface IHorarioDAO {
    fun getHorariosPorTurno(turno: TurnoEnum): ListaEstatica<String>
}

class HorarioDAO : IHorarioDAO {
    override fun getHorariosPorTurno(turno: TurnoEnum): ListaEstatica<String> {
        val connection = DatabaseUtil.getConnection()
        var table = when (turno) {
            TurnoEnum.MATUTINO -> "horarios_matutino"
            TurnoEnum.VESPERTINO -> "horarios_vespertino"
            TurnoEnum.NOTURNO -> "horarios_noturno"
        }
        val sql = "SELECT horario FROM $table".trimIndent()
        val horarios = ListaEstatica<String>(12)
        connection?.use {
            val statement = it.prepareStatement(sql)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                horarios.anexar(resultSet.getString("horario"))
            }
        }
        return horarios
    }
}