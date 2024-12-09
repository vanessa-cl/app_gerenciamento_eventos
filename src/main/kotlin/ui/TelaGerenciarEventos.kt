package ui

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import logic.entities.Participante
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.collections.ListaEventos
import logic.entities.Evento
import util.enums.*
import logic.database.EventoDAO
import java.time.LocalDate

class TelaGerenciarEventos(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val eventos: ListaEventos,
    val usuarioLogado: Participante
) {
    private var vbox = VBox(10.0)
    private val resultadoText = SimpleStringProperty()
    private var resultadoLabel = Label()
    private lateinit var telaGerenciarPalestras: TelaGerenciarPalestras
    private val eventoDAO = EventoDAO()

    fun gerenciarEventosScene(): Scene {
        val pageLabel = Label("Gerenciamento de Eventos")
        val vboxEventos = exibirEventosBox()
        val btnCadastro = Button("Cadastrar novo evento")
        btnCadastro.setOnAction {
            vbox.children.clear()
            primaryStage.scene = cadastrarEventoScene()
        }
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = mainApp.getInitialScene()
        }
        vbox = VBox(
            10.0,
            pageLabel,
            vboxEventos,
            btnCadastro,
            btnVoltar
        )

        val titledPane = TitledPane("Gerenciamento de Eventos", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Gerenciar Eventos"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun cadastrarEventoScene(): Scene {
        resultadoLabel.textProperty().bind(resultadoText)
        resultadoText.set("")
        val labelNome = Label("Nome do evento:")
        val inputNome = TextField()
        val labelDataInicio = Label("Data de início:")
        val inputDataInicio = DatePicker()
        val labelDataTermino = Label("Data de término:")
        val inputDataTermino = DatePicker()
        val labelDescricao = Label("Descrição:")
        val inputDescricao = TextField()
        val labelValorInscricao = Label("Valor da inscrição (Ex: R$0.00):")
        val inputValorInscricao = TextField()
        val labelTurno = Label("Turno:")
        val comboBoxTurno = ComboBox<TurnoEnum>()
        comboBoxTurno.items.addAll(TurnoEnum.entries)

        val btnConfirmar = Button("Cadastrar")
        btnConfirmar.setOnAction {
            val inputNomeContent = inputNome.text
            val inputDataInicioContent = inputDataInicio.value
            val inputDataTerminoContent = inputDataTermino.value
            val inputDescricaoContent = inputDescricao.text
            val inputValorInscricaoContent = inputValorInscricao.text
            val comboBoxTurnoContent = comboBoxTurno.selectionModel.selectedItem
            val novoEvento =
                Evento(
                    0,
                    inputNomeContent,
                    inputDescricaoContent,
                    inputValorInscricaoContent.toDouble(),
                    inputDataInicioContent,
                    inputDataTerminoContent,
                    StatusEnum.PENDENTE,
                    comboBoxTurnoContent
                )
            val idGerado = eventoDAO.insertEvento(novoEvento)
            if (idGerado != null) {
                novoEvento.id = idGerado
                eventos.inserirEvento(novoEvento)
                resultadoText.set("Evento cadastrado com sucesso!")
            } else {
                resultadoText.set("Erro ao cadastrar evento!")
            }
        }

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = gerenciarEventosScene()
        }
        vbox.children.addAll(
            labelNome,
            inputNome,
            labelDataInicio,
            inputDataInicio,
            labelDataTermino,
            inputDataTermino,
            labelTurno,
            comboBoxTurno,
            labelDescricao,
            inputDescricao,
            labelValorInscricao,
            inputValorInscricao,
            resultadoLabel,
            btnConfirmar,
            btnVoltar
        )
        val titledPane = TitledPane("Cadastrar novo evento", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)

        return scene
    }

    private fun exibirEventosBox(): VBox {
        val vbox = VBox(10.0)
        resultadoLabel.textProperty().bind(resultadoText)
        val eventos = eventos.buscarTodosEventos()
        if (eventos == null) {
            resultadoText.set("Não há eventos cadastrados!")
            vbox.children.addAll(
                resultadoLabel
            )
            return vbox
        }

        val table = gerenciarEventosTable(eventos)
        vbox.children.addAll(
            table
        )

        return vbox
    }

    private fun gerenciarEventosTable(eventos: Array<Evento?>): TableView<Evento> {
        val tableView = TableView<Evento>()

        val idColumn = TableColumn<Evento, Int>("ID")
        idColumn.cellValueFactory = PropertyValueFactory("id")

        val nomeColumn = TableColumn<Evento, String>("Nome")
        nomeColumn.cellValueFactory = PropertyValueFactory("nome")

        val dataInicioColumn = TableColumn<Evento, LocalDate>("Data de Início")
        dataInicioColumn.cellValueFactory = PropertyValueFactory("dataInicio")

        val dataTerminoColumn = TableColumn<Evento, LocalDate>("Data de Término")
        dataTerminoColumn.cellValueFactory = PropertyValueFactory("dataFim")

        val turnoColumn = TableColumn<Evento, TurnoEnum>("Turno")
        turnoColumn.cellValueFactory = PropertyValueFactory("turno")

        val descricaoColumn = TableColumn<Evento, String>("Descrição")
        descricaoColumn.cellValueFactory = PropertyValueFactory("descricao")

        val valorInscricaoColumn = TableColumn<Evento, Double>("Valor da Inscrição")
        valorInscricaoColumn.cellValueFactory = PropertyValueFactory("valorInscricao")

        val statusColumn = TableColumn<Evento, StatusEnum>("Status")
        statusColumn.cellValueFactory = PropertyValueFactory("status")

        val actionColumn = TableColumn<Evento, Void>("Ações")
        actionColumn.setCellFactory {
            object : TableCell<Evento, Void>() {
                private val btn = Button("Palestras")

                init {
                    btn.setOnAction {
                        val evento = tableView.items[index]
                        telaGerenciarPalestras = TelaGerenciarPalestras(primaryStage, evento, this@TelaGerenciarEventos)
                        primaryStage.scene = telaGerenciarPalestras.gerenciarPalestrasScene()
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

        tableView.columns.addAll(
            idColumn,
            nomeColumn,
            dataInicioColumn,
            dataTerminoColumn,
            turnoColumn,
            descricaoColumn,
            valorInscricaoColumn,
            statusColumn,
            actionColumn
        )

        val data = FXCollections.observableArrayList(eventos.filterNotNull())
        tableView.items = data
        return tableView
    }

}