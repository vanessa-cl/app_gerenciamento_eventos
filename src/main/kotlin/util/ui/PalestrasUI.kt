package util.ui

import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import logic.entities.Palestra
import util.enums.StatusEnum

class PalestrasUI {

    fun getActionCollumn(buttonLabel: String, action: (Palestra) -> Unit): TableColumn<Palestra, Void> {
        val actionColumn = TableColumn<Palestra, Void>("Ações")
        actionColumn.setCellFactory {
            object : TableCell<Palestra, Void>() {
                private val btn = Button(buttonLabel)

                init {
                    btn.setOnAction {
                        val palestra = tableView.items[index]
                        action(palestra)
                    }
                }

                override fun updateItem(item: Void?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        graphic = null
                    } else {
                        graphic = btn
                    }
                }
            }
        }
        return actionColumn
    }

    fun baseTablePalestras(
        palestras: Array<Palestra?>,
        actionColumn: TableColumn<Palestra, Void>
    ): TableView<Palestra> {
        val tableView = TableView<Palestra>()

        val colTitulo = TableColumn<Palestra, String>("Título")
        colTitulo.cellValueFactory = PropertyValueFactory("titulo")

        val colPalestrante = TableColumn<Palestra, String>("Palestrante")
        colPalestrante.cellValueFactory = PropertyValueFactory("palestrante")

        val colData = TableColumn<Palestra, String>("Data")
        colData.cellValueFactory = PropertyValueFactory("data")

        val colHorarioInicio = TableColumn<Palestra, String>("Início")
        colHorarioInicio.cellValueFactory = PropertyValueFactory("horarioInicio")

        val colHorarioTermino = TableColumn<Palestra, String>("Término")
        colHorarioTermino.cellValueFactory = PropertyValueFactory("horarioFim")

        val colLocal = TableColumn<Palestra, String>("Local")
        colLocal.cellValueFactory = PropertyValueFactory("local")

        val colLimiteParticipantes = TableColumn<Palestra, Int>("Capacidade Máx.")
        colLimiteParticipantes.cellValueFactory = PropertyValueFactory("limiteParticipantes")

        val colStatus = TableColumn<Palestra, StatusEnum>("Status")
        colStatus.cellValueFactory = PropertyValueFactory("status")

        tableView.columns.addAll(
            colTitulo,
            colPalestrante,
            colLocal,
            colData,
            colHorarioInicio,
            colHorarioTermino,
            colLimiteParticipantes,
            colStatus,
            actionColumn
        )

        val data = FXCollections.observableArrayList(palestras.filterNotNull())
        tableView.items = data
        return tableView
    }
}