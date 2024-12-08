package logic.database

import logic.collections.AgendaPalestras
import logic.collections.ListaEventos
import logic.entities.Evento
import logic.entities.Palestra
import util.enums.StatusEnum
import util.enums.TurnoEnum
import java.time.LocalDate
import java.time.LocalTime

interface IPalestraDAO {
    fun getPalestras(idEvento: Int): AgendaPalestras
    fun insertPalestra(idEvento: Int, palestra: Palestra): Int?
    fun updatePalestra(palestra: Palestra): Boolean
    fun deletePalestra(idPalestra: Int): Boolean
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
            "INSERT INTO palestras (idEvento, titulo, palestrante, limiteParticipantes, local, data, horarioInicio, horarioFim, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
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

    override fun updatePalestra(palestra: Palestra): Boolean {
        val connection = DatabaseUtil.getConnection()
        val sql =
            "UPDATE palestras SET horarioInicio = ?, horarioFim = ? WHERE id = ?"
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(6, palestra.horarioInicio.toString())
            statement.setString(7, palestra.horarioFim.toString())
            statement.setInt(9, palestra.id)
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
}