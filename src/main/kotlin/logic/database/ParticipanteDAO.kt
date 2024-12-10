package logic.database

import logic.collections.FilaEspera
import logic.collections.ListaParticipantes
import logic.entities.Participante
import util.enums.CargoEnum

interface IParticipanteDAO {
    fun getParticipante(idParticipante: Int): Participante?
    fun getParticipantesPalestra(idPalestra: Int, limParticipantes: Int): ListaParticipantes
    fun getParticipantesFilaEspera(idPalestra: Int): FilaEspera
}

class ParticipanteDAO : IParticipanteDAO {

    override fun getParticipante(idParticipante: Int): Participante? {
        val connection = DatabaseUtil.getConnection()
        var participante: Participante? = null
        val sql = "SELECT * FROM participantes"
        connection?.use {
            val statement = it.prepareStatement(sql)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                participante = Participante(
                    id = resultSet.getInt("id"),
                    nome = resultSet.getString("nome"),
                    email = resultSet.getString("email"),
                    cpf = resultSet.getString("cpf"),
                    cargo = CargoEnum.valueOf(resultSet.getString("cargo"))
                )
            }
        }
        return participante
    }

    override fun getParticipantesPalestra(idPalestra: Int, limParticipantes: Int): ListaParticipantes {
        val connection = DatabaseUtil.getConnection()
        val sql =
            "SELECT * FROM participantes WHERE id IN (SELECT idParticipante FROM palestra_participantes WHERE idPalestra = ?)"
        var participantes = ListaParticipantes(limParticipantes)
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, idPalestra)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val participante = Participante(
                    id = resultSet.getInt("id"),
                    nome = resultSet.getString("nome"),
                    email = resultSet.getString("email"),
                    cpf = resultSet.getString("cpf"),
                    cargo = CargoEnum.valueOf(resultSet.getString("cargo"))
                )
                participantes.inserirParticipante(participante)
            }
        }
        return participantes
    }

    override fun getParticipantesFilaEspera(idPalestra: Int): FilaEspera {
        val connection = DatabaseUtil.getConnection()
        val sql =
            "SELECT * FROM participantes WHERE id IN (SELECT idParticipante FROM fila_espera WHERE idPalestra = ?)"
        var filaEspera = FilaEspera()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, idPalestra)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val participante = Participante(
                    id = resultSet.getInt("id"),
                    nome = resultSet.getString("nome"),
                    email = resultSet.getString("email"),
                    cpf = resultSet.getString("cpf"),
                    cargo = CargoEnum.valueOf(resultSet.getString("cargo"))
                )
                filaEspera.inserirParticipanteFim(participante)
            }
        }
        return filaEspera
    }
}