package util

import javafx.collections.FXCollections
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import logic.entities.Evento
import java.time.LocalDate

class Tables {

    fun eventosTable(eventos: Array<Evento?>): TableView<Evento> {
        val tableView = TableView<Evento>()
        val idColumn = TableColumn<Evento, Int>("ID")
        idColumn.cellValueFactory = PropertyValueFactory("id")
        val nomeColumn = TableColumn<Evento, String>("Nome")
        nomeColumn.cellValueFactory = PropertyValueFactory("nome")
        val dataInicioColumn = TableColumn<Evento, LocalDate>("Data de Início")
        dataInicioColumn.cellValueFactory = PropertyValueFactory("dataInicio")
        val dataTerminoColumn = TableColumn<Evento, LocalDate>("Data de Término")
        dataTerminoColumn.cellValueFactory = PropertyValueFactory("dataFim")
        val descricaoColumn = TableColumn<Evento, String>("Descrição")
        descricaoColumn.cellValueFactory = PropertyValueFactory("descricao")
        val valorInscricaoColumn = TableColumn<Evento, Double>("Valor da Inscrição")
        valorInscricaoColumn.cellValueFactory = PropertyValueFactory("valorInscricao")
        tableView.columns.addAll(
            idColumn,
            nomeColumn,
            dataInicioColumn,
            dataTerminoColumn,
            descricaoColumn,
            valorInscricaoColumn
        )

        val data = FXCollections.observableArrayList(
            eventos.filterNotNull().toList()
        )
        tableView.items = data
        return tableView
    }
}