package logic.database

import logic.collections.AgendaPalestras
import logic.entities.Palestra
import util.enums.StatusEnum
import java.time.LocalDate
import java.time.LocalTime

interface IPalestraDAO {
    fun getPalestras(idEvento: Int): AgendaPalestras
    fun insertPalestra(idEvento: Int, palestra: Palestra): Int?
    fun updatePalestra(palestra: Palestra, duracao: Long, horarioInicio: LocalTime, horarioFim: LocalTime): Boolean
    fun deletePalestra(idPalestra: Int): Boolean
    fun subscribeParticipante(idPalestra: Int, idParticipante: Int): Boolean
    fun unsubscribeParticipante(idPalestra: Int, idParticipante: Int): Boolean
    fun getSubscriptionsParticipante(idEvento: Int, idParticipante: Int): AgendaPalestras
    fun subscribeParticipanteFilaEspera(idPalestra: Int, idParticipante: Int): Boolean
    fun unsubscribeParticipanteFilaEspera(idPalestra: Int, idParticipante: Int): Boolean
}

class PalestraDAO : IPalestraDAO {
    override fun getPalestras(idEvento: Int): AgendaPalestras {
        val connection = DatabaseUtil.getConnection()
        val sql = "SELECT * FROM palestras WHERE idEvento = $idEvento"
        val palestras = AgendaPalestras()
        connection?.use {
            val statement = it.prepareStatement(sql)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val palestra = Palestra(
                    id = resultSet.getInt("id"),
                    titulo = resultSet.getString("titulo"),
                    palestrante = resultSet.getString("palestrante"),
                    limiteParticipantes = resultSet.getInt("limiteParticipantes"),
                    local = resultSet.getString("local"),
                    data = LocalDate.parse(resultSet.getString("data")),
                    duracao = resultSet.getInt("duracaoEmHoras").toLong(),
                    horarioInicio = LocalTime.parse(resultSet.getString("horarioInicio")),
                    horarioFim = LocalTime.parse(resultSet.getString("horarioFim")),
                    status = StatusEnum.valueOf(resultSet.getString("status")),
                )
                palestras.inserirPalestra(palestra)
            }
        }
        return palestras
    }

    override fun insertPalestra(idEvento: Int, palestra: Palestra): Int? {
        val connection = DatabaseUtil.getConnection()
        println(palestra.toString())
        val sql =
            "INSERT INTO palestras (idEvento, titulo, palestrante, limiteParticipantes, local, data, horarioInicio, horarioFim, status, duracaoEmHoras) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, idEvento)
            statement.setString(2, palestra.titulo)
            statement.setString(3, palestra.palestrante)
            statement.setInt(4, palestra.limiteParticipantes)
            statement.setString(5, palestra.local)
            statement.setString(6, palestra.data.toString())
            statement.setString(7, palestra.horarioInicio.toString())
            statement.setString(8, palestra.horarioFim.toString())
            statement.setString(9, palestra.status.name)
            statement.setInt(10, palestra.duracao.toInt())
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

    override fun updatePalestra(
        palestra: Palestra,
        duracao: Long,
        horarioInicio: LocalTime,
        horarioFim: LocalTime
    ): Boolean {
        println(palestra.toString())
        val connection = DatabaseUtil.getConnection()
        val sql =
            "UPDATE palestras SET duracaoEmHoras = ?, horarioInicio = ?, horarioFim = ? WHERE id = ?"
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, duracao.toInt())
            statement.setString(2, horarioInicio.toString())
            statement.setString(3, horarioFim.toString())
            statement.setInt(4, palestra.id)
            val linhasAlteradas = statement.executeUpdate()
            return linhasAlteradas > 0
        }
        return false
    }

    override fun deletePalestra(idPalestra: Int): Boolean {
        val connection = DatabaseUtil.getConnection()
        val sql = "DELETE FROM palestras WHERE id = ?"
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, idPalestra)
            val linhasAlteradas = statement.executeUpdate()
            return linhasAlteradas > 0
        }
        return false
    }

    override fun subscribeParticipante(idPalestra: Int, idParticipante: Int): Boolean {
        val connection = DatabaseUtil.getConnection()
        val checkIfSubscriptionExistsSql =
            "SELECT * FROM palestra_participantes WHERE idPalestra = ? AND idParticipante = ?"
        val insertSubscriptionSql = "INSERT INTO palestra_participantes (idPalestra, idParticipante) VALUES (?, ?)"
        connection?.use {
            val checkStatement = it.prepareStatement(checkIfSubscriptionExistsSql)
            checkStatement.setInt(1, idPalestra)
            checkStatement.setInt(2, idParticipante)
            val resultSet = checkStatement.executeQuery()
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                println("Usuário já está inscrito na palestra!")
                return false
            }
            val insertStatement = it.prepareStatement(insertSubscriptionSql)
            insertStatement.setInt(1, idPalestra)
            insertStatement.setInt(2, idParticipante)
            val linhasAlteradas = insertStatement.executeUpdate()
            return linhasAlteradas > 0
        }
        return false
    }

    override fun unsubscribeParticipante(idPalestra: Int, idParticipante: Int): Boolean {
        val connection = DatabaseUtil.getConnection()
        val sql =
            "DELETE FROM palestra_participantes WHERE idPalestra = ? AND idParticipante = ?"
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, idPalestra)
            statement.setInt(2, idParticipante)
            val linhasAlteradas = statement.executeUpdate()
            return linhasAlteradas > 0
        }
        return false
    }

    override fun getSubscriptionsParticipante(idEvento: Int, idParticipante: Int): AgendaPalestras {
        val connection = DatabaseUtil.getConnection()
        val sql =
            "SELECT * FROM palestras WHERE idEvento = ? AND id IN (SELECT idPalestra FROM palestra_participantes WHERE idParticipante = ?)"
        val palestras = AgendaPalestras()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, idEvento)
            statement.setInt(2, idParticipante)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val palestra = Palestra(
                    id = resultSet.getInt("id"),
                    titulo = resultSet.getString("titulo"),
                    palestrante = resultSet.getString("palestrante"),
                    limiteParticipantes = resultSet.getInt("limiteParticipantes"),
                    local = resultSet.getString("local"),
                    data = LocalDate.parse(resultSet.getString("data")),
                    duracao = resultSet.getInt("duracaoEmHoras").toLong(),
                    horarioInicio = LocalTime.parse(resultSet.getString("horarioInicio")),
                    horarioFim = LocalTime.parse(resultSet.getString("horarioFim")),
                    status = StatusEnum.valueOf(resultSet.getString("status")),
                )
                palestras.inserirPalestra(palestra)
            }
        }
        return palestras
    }

    override fun subscribeParticipanteFilaEspera(idPalestra: Int, idParticipante: Int): Boolean {
        val connection = DatabaseUtil.getConnection()
        val checkIfSubscriptionExistsSql =
            "SELECT * FROM fila_espera WHERE idPalestra = ? AND idParticipante = ?"
        val insertSubscriptionSql = "INSERT INTO fila_espera (idPalestra, idParticipante) VALUES (?, ?)"
        connection?.use {
            val checkStatement = it.prepareStatement(checkIfSubscriptionExistsSql)
            checkStatement.setInt(1, idPalestra)
            checkStatement.setInt(2, idParticipante)
            val resultSet = checkStatement.executeQuery()
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                println("Usuário já está inscrito na fila de espera!")
                return false
            }
            val insertStatement = it.prepareStatement(insertSubscriptionSql)
            insertStatement.setInt(1, idPalestra)
            insertStatement.setInt(2, idParticipante)
            val linhasAlteradas = insertStatement.executeUpdate()
            return linhasAlteradas > 0
        }
        return false
    }

    override fun unsubscribeParticipanteFilaEspera(idPalestra: Int, idParticipante: Int): Boolean {
        val connection = DatabaseUtil.getConnection()
        val sql =
            "DELETE FROM fila_espera WHERE idPalestra = ? AND idParticipante = ?"
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, idPalestra)
            statement.setInt(2, idParticipante)
            val linhasAlteradas = statement.executeUpdate()
            return linhasAlteradas > 0
        }
        return false
    }
}