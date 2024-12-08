package logic.database

import logic.collections.ListaEventos
import logic.entities.Evento
import util.enums.StatusEnum
import util.enums.TurnoEnum
import java.time.LocalDate

interface IEventoDAO {
    fun getEventos(): ListaEventos
    fun insertEvento(evento: Evento): Int?
}

class EventoDAO : IEventoDAO {
    override fun getEventos(): ListaEventos {
        val connection = DatabaseUtil.getConnection()
        val sql = "SELECT * FROM eventos"
        val eventos = ListaEventos()
        connection?.use {
            val statement = it.prepareStatement(sql)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val evento = Evento(
                    id = resultSet.getInt("id"),
                    nome = resultSet.getString("nome"),
                    descricao = resultSet.getString("descricao"),
                    valorInscricao = resultSet.getDouble("valorInscricao"),
                    dataInicio = LocalDate.parse(resultSet.getString("dataInicio")),
                    dataFim = LocalDate.parse(resultSet.getString("dataFim")),
                    status = StatusEnum.valueOf(resultSet.getString("status")),
                    turno = TurnoEnum.valueOf(resultSet.getString("turno"))
                )
                eventos.inserirEvento(evento)
            }
        }
        return eventos
    }

    override fun insertEvento(evento: Evento): Int? {
        val connection = DatabaseUtil.getConnection()
        val sql =
            "INSERT INTO eventos (nome, descricao, valorInscricao, dataInicio, dataFim, status, turno) VALUES (?, ?, ?, ?, ?, ?, ?)"
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, evento.nome)
            statement.setString(2, evento.descricao)
            statement.setDouble(3, evento.valorInscricao)
            statement.setString(4, evento.dataInicio.toString())
            statement.setString(5, evento.dataFim.toString())
            statement.setString(6, evento.status.name)
            statement.setString(7, evento.turno.name)
            val linhasAlteradas = statement.executeUpdate()
            if (linhasAlteradas > 0) {
                val idsGerados = statement.generatedKeys
                if (idsGerados.next()) {
                    return idsGerados.getInt(1)
                }
            }
        }
        return null
    }
}