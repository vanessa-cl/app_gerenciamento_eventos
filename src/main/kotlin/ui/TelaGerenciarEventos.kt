package ui

import javafx.collections.FXCollections
import javafx.geometry.Pos
import logic.entities.Participante
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.collections.ListaEventos
import logic.entities.Evento
import util.enums.*
import logic.database.EventoDAO
import util.ui.Header
import java.time.LocalDate
import javafx.geometry.Insets
import util.ui.Alert

class TelaGerenciarEventos(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val eventos: ListaEventos,
    val usuarioLogado: Participante
) {
    private var titulo = "Gerenciamento de Eventos"
    private val header = Header(primaryStage, mainApp, usuarioLogado, titulo)
    private val alert = Alert()
    private lateinit var telaGerenciarPalestras: TelaGerenciarPalestras
    private val eventoDAO = EventoDAO()

    fun gerenciarEventosScene(): Scene {
        val gerenciarEventosVBox = VBox(10.0)
        gerenciarEventosVBox.children.add(header.getHeader())

        val btnCadastro = Button("Cadastrar novo evento")
        btnCadastro.setOnAction {
            primaryStage.scene = cadastrarEventoScene()
        }

        val eventosTable = exibirEventosBox()
        if (eventosTable != null) {
            gerenciarEventosVBox.children.addAll(
                eventosTable.apply { alignment = Pos.CENTER; maxWidth = 1300.0 },
                HBox(10.0, btnCadastro).apply { alignment = Pos.BOTTOM_RIGHT }
            )
        } else {
            gerenciarEventosVBox.children.addAll(
                HBox(10.0, btnCadastro).apply { alignment = Pos.BOTTOM_RIGHT }
            )
        }

        gerenciarEventosVBox.apply { spacing = 10.0; padding = Insets(10.0) }
        val scene = Scene(gerenciarEventosVBox, 1300.0, 600.0)
        primaryStage.title = "Gerenciamento de Eventos"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun cadastrarEventoScene(): Scene {
        val cadastrarEventoVbox = VBox(10.0)
        cadastrarEventoVbox.children.add(header.getHeader())

        val inputNome = TextField()
        val inputDataInicio = DatePicker()
        val inputDataTermino = DatePicker()
        val inputDescricao = TextField()
        val inputValorInscricao = TextField()
        val comboBoxTurno = ComboBox<TurnoEnum>()
        comboBoxTurno.items.addAll(TurnoEnum.entries)

        val linha1Hbox = HBox(10.0)
        linha1Hbox.children.addAll(
            VBox(Label("Nome do evento:"), inputNome),
            VBox(Label("Data de início:"), inputDataInicio),
            VBox(Label("Data de término:"), inputDataTermino),
        )
        linha1Hbox.apply { spacing = 10.0 }

        val linha2Hbox = HBox(10.0)
        linha2Hbox.children.addAll(
            VBox(Label("Descrição:"), inputDescricao),
            VBox(Label("Valor da inscrição (Ex: 10.0):"), inputValorInscricao),
            VBox(Label("Turno:"), comboBoxTurno),
        )
        linha2Hbox.apply { spacing = 10.0 }

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
                alert.showInfo("Cadastro de Evento", "Sucesso", "Evento cadastrado com sucesso!")
            } else {
                alert.showError("Cadastro de Evento", "Erro", "Erro ao cadastrar evento!")
            }
        }

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = gerenciarEventosScene()
        }

        cadastrarEventoVbox.children.addAll(
            VBox(
                10.0,
                Label("Preencha os campos abaixo para cadastrar um novo evento:").apply { alignment = Pos.TOP_LEFT },
                linha1Hbox,
                linha2Hbox,
                HBox(10.0, btnConfirmar, btnVoltar).apply { alignment = Pos.BOTTOM_RIGHT }
            ).apply { alignment = Pos.TOP_LEFT; padding = Insets(10.0) },
        )

        cadastrarEventoVbox.apply { spacing = 10.0; padding = Insets(10.0) }
        val scene = Scene(cadastrarEventoVbox, 900.0, 400.0)
        primaryStage.title = "Cadastro de Eventos"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun exibirEventosBox(): VBox? {
        val vbox = VBox(10.0)
        val eventos = eventos.buscarTodosEventos()
        if (eventos == null) {
            alert.showInfo("Gerenciamento de Eventos", "Nenhum evento encontrado", "Nenhum evento encontrado!")
            return null
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
                        telaGerenciarPalestras =
                            TelaGerenciarPalestras(
                                primaryStage,
                                mainApp,
                                evento,
                                this@TelaGerenciarEventos,
                                usuarioLogado
                            )
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