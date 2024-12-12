package ui

import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import logic.database.PalestraDAO
import logic.database.HorarioDAO
import logic.database.ParticipanteDAO
import logic.entities.Evento
import logic.entities.Palestra
import logic.entities.Participante
import logic.structures.ListaEstatica
import util.enums.StatusEnum
import util.enums.TurnoEnum
import util.notifyUsers
import util.ui.Header
import java.time.LocalTime

class TelaGerenciarPalestras(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val evento: Evento,
    private val telaGerenciarEventos: TelaGerenciarEventos,
    private val usuarioLogado: Participante
) {
    private var titulo = "Gerenciamento de Palestras do Evento ${evento.nome}"
    private val header = Header(primaryStage, mainApp, usuarioLogado, titulo)
    private var vbox = VBox(10.0)
    private val resultadoText = SimpleStringProperty()
    private var resultadoLabel = Label()
    private val palestraDAO = PalestraDAO()
    private val participanteDAO = ParticipanteDAO()
    private val horarioDAO = HorarioDAO()

    init {
        resultadoLabel.textProperty().bind(resultadoText)
        loadDataDB()
    }

    private fun loadDataDB() {
        usuarioLogado.inscricoes = palestraDAO.getSubscriptionsParticipante(evento.id, usuarioLogado.id)
        val palestras = palestraDAO.getPalestras(evento.id)
        evento.agenda = palestras
        val todasPalestras = palestras.buscarTodasPalestras()

        for (palestra in todasPalestras!!) {
            if (palestra != null) {
                val participantes =
                    participanteDAO.getParticipantesPalestra(palestra.id, palestra.participantes.limiteParticipantes)
                palestra.participantes = participantes
                val filaEspera = participanteDAO.getParticipantesFilaEspera(palestra.id)
                palestra.filaEspera = filaEspera
            }
        }
    }

    fun gerenciarPalestrasScene(): Scene {
        val gerenciarPalestrasVBox = VBox(10.0)
        gerenciarPalestrasVBox.children.add(header.getHeader())

        val btnCadastro = Button("Cadastrar nova palestra")
        btnCadastro.setOnAction {
            primaryStage.scene = cadastrarPalestraScene()
        }

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            gerenciarPalestrasVBox.children.clear()
            primaryStage.scene = telaGerenciarEventos.gerenciarEventosScene()
        }

        gerenciarPalestrasVBox.children.addAll(
            exibirPalestrasBox().apply { alignment = Pos.CENTER; maxWidth = 1300.0 },
            HBox(10.0, btnCadastro, btnVoltar).apply { alignment = Pos.BOTTOM_RIGHT }
        )

        gerenciarPalestrasVBox.apply { spacing = 10.0; padding = Insets(10.0) }
        val scene = Scene(gerenciarPalestrasVBox, 1300.0, 600.0)
        primaryStage.title = "Gerenciar Palestras"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun cadastrarPalestraScene(): Scene {
        val cadastrarPalestraVbox = VBox(10.0)
        cadastrarPalestraVbox.children.add(header.getHeader())

        resultadoLabel.textProperty().bind(resultadoText)
        resultadoText.set("")

        val inputTitulo = TextField()
        val inputNomePalestrante = TextField()
        val inputLocal = TextField()
        val inputLimitePart = TextField()
        val inputData = DatePicker()
        val inputDuracao = TextField()
        val comboBoxHoraInicio = ComboBox<String>()
        val inputHoraTermino = TextField()
        val horarios = buscarHorarios(evento.turno)
        var palestrasExistentes: Array<Palestra?>? = null

        val linha1Hbox = HBox(10.0)
        linha1Hbox.children.addAll(
            VBox(Label("Título da palestra:"), inputTitulo),
            VBox(Label("Nome do palestrante:"), inputNomePalestrante),
            VBox(Label("Duração da palestra em horas:"), inputDuracao),
            VBox(Label("Data:"), inputData),
        )
        linha1Hbox.apply { spacing = 10.0 }

        val linha2Hbox = HBox(10.0)
        linha2Hbox.children.addAll(
            VBox(Label("Local:"), inputLocal),
            VBox(Label("Limite de participantes:"), inputLimitePart),
            VBox(Label("Horário de início:"), comboBoxHoraInicio),
            VBox(Label("Horário de término:"), inputHoraTermino),
        )
        linha2Hbox.apply { spacing = 10.0 }

        inputData.valueProperty().addListener { _, _, valor ->
            palestrasExistentes = null
            comboBoxHoraInicio.items.clear()
            inputHoraTermino.clear()
            if (valor != null && inputDuracao.text != null) {
                palestrasExistentes = palestraDAO.getPalestras(evento.id).buscarTodasPalestrasPorDia(valor)
                for (horario in horarios.selecionarTodos()) {
                    if (horario == null) {
                        continue
                    } else {
                        if (isHorarioDisponivel(
                                LocalTime.parse(horario.toString()),
                                inputDuracao.text.toLong(),
                                palestrasExistentes
                            )
                        ) {
                            comboBoxHoraInicio.items.add(horario.toString())
                        }
                    }
                }
            }
        }

        inputDuracao.textProperty().addListener { _, _, valor ->
            inputData.value = null
            comboBoxHoraInicio.items.clear()
            inputHoraTermino.clear()
        }

        comboBoxHoraInicio.valueProperty().addListener { _, _, valor ->
            inputHoraTermino.clear()
            if (valor != null && inputDuracao.text != null && palestrasExistentes != null) {
                val horaInicio = LocalTime.parse(valor)
                if (isHorarioDisponivel(horaInicio, inputDuracao.text.toLong(), palestrasExistentes)) {
                    inputHoraTermino.text = horaInicio.plusHours(inputDuracao.text.toLong()).toString()
                }
            }
        }

        val btnConfirmar = Button("Cadastrar")
        btnConfirmar.setOnAction {
            val novaPalestra =
                Palestra(
                    0,
                    inputTitulo.text,
                    inputNomePalestrante.text,
                    inputLimitePart.text.toInt(),
                    inputLocal.text,
                    inputData.value,
                    inputDuracao.text.toLong(),
                    LocalTime.parse(comboBoxHoraInicio.value),
                    LocalTime.parse(inputHoraTermino.text)
                )
            val idGerado = palestraDAO.insertPalestra(evento.id, novaPalestra)
            if (idGerado != null) {
                novaPalestra.id = idGerado
                evento.agenda.inserirPalestra(novaPalestra)
                resultadoText.set("Palestra cadastrada com sucesso!")
            } else {
                resultadoText.set("Erro ao cadastrar palestra!")
            }
        }

        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = gerenciarPalestrasScene()
        }

        cadastrarPalestraVbox.children.addAll(
            VBox(
                10.0,
                Label("Preencha os campos abaixo para cadastrar uma nova palestra:").apply {
                    alignment = Pos.TOP_LEFT
                },
                linha1Hbox,
                linha2Hbox,
                HBox(10.0, btnConfirmar, btnVoltar).apply { alignment = Pos.BOTTOM_RIGHT }
            ).apply { alignment = Pos.TOP_LEFT; padding = Insets(10.0) },
        )

        cadastrarPalestraVbox.apply { spacing = 10.0; padding = Insets(10.0) }
        val scene = Scene(cadastrarPalestraVbox, 900.0, 400.0)
        primaryStage.title = "Cadastro de Palestras"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun isHorarioDisponivel(
        novaHoraInicio: LocalTime,
        novaDuracao: Long,
        palestras: Array<Palestra?>?
    ): Boolean {
        val novaHoraTermino = novaHoraInicio.plusHours(novaDuracao)

        if (palestras == null) {
            return true
        }
        for (palestra in palestras) {
            if (palestra != null) {
                val horaInicioExistente = palestra.horarioInicio
                val horaTerminoExistente = palestra.horarioFim

                if (novaHoraInicio.isBefore(horaTerminoExistente) && novaHoraInicio.isAfter(
                        horaInicioExistente.minusMinutes(
                            1
                        )
                    )
                ) {
                    return false
                }

                if (novaHoraTermino.isBefore(horaTerminoExistente) && novaHoraTermino.isAfter(horaInicioExistente)) {
                    return false
                }
            }
        }

        return true
    }

    private fun exibirPalestrasBox(): VBox {
        val vbox = VBox(10.0)
        val btnOrdenarPorDataHora = Button("Ordenar por data e horário")
        val btnOrdenarPorId = Button("Ordenar por ID")
        resultadoLabel.textProperty().bind(resultadoText)
        var palestras: Array<Palestra> = arrayOf()
        val palestrasDB = palestraDAO.getPalestras(evento.id)
        if (!palestrasDB.estaVazia()) {
            palestras = palestrasDB.buscarTodasPalestras().filterNotNull().toTypedArray()
        } else {
            resultadoText.set("Não há palestras cadastradas para este evento")
            vbox.children.addAll(
                resultadoLabel
            )
            return vbox
        }

        btnOrdenarPorId.setOnAction {
            palestras = palestrasDB.buscarTodasPalestras().filterNotNull().toTypedArray()
            vbox.children.clear()
            vbox.children.addAll(
                btnOrdenarPorDataHora,
                gerenciarPalestrasTable(palestras)
            )
        }
        btnOrdenarPorDataHora.setOnAction {
            palestras = palestrasDB.ordenarPalestrasPorHorario()
            vbox.children.clear()
            vbox.children.addAll(
                btnOrdenarPorId,
                gerenciarPalestrasTable(palestras)
            )
        }

        val tableView = gerenciarPalestrasTable(palestras)

        vbox.children.addAll(
            HBox(10.0, btnOrdenarPorDataHora).apply { alignment = Pos.TOP_LEFT },
            tableView
        )
        return vbox
    }

    private fun gerenciarPalestrasTable(palestras: Array<Palestra>): TableView<Palestra> {
        val tableView = TableView<Palestra>()

        val colId = TableColumn<Palestra, Int>("ID")
        colId.cellValueFactory = PropertyValueFactory("id")
        val colTitulo = TableColumn<Palestra, String>("Título")
        colTitulo.cellValueFactory = PropertyValueFactory("titulo")
        val colPalestrante = TableColumn<Palestra, String>("Palestrante")
        colPalestrante.cellValueFactory = PropertyValueFactory("palestrante")
        val colLocal = TableColumn<Palestra, String>("Local")
        colLocal.cellValueFactory = PropertyValueFactory("local")
        val colData = TableColumn<Palestra, String>("Data")
        colData.cellValueFactory = PropertyValueFactory("data")
        val colDuracao = TableColumn<Palestra, String>("Duração em Horas")
        colDuracao.cellValueFactory = PropertyValueFactory("duracao")
        val colHorarioInicio = TableColumn<Palestra, String>("Horário de Início")
        colHorarioInicio.cellValueFactory = PropertyValueFactory("horarioInicio")
        val colHorarioTermino = TableColumn<Palestra, String>("Horário de Término")
        colHorarioTermino.cellValueFactory = PropertyValueFactory("horarioFim")
        val colLimiteParticipantes = TableColumn<Palestra, Int>("Limite de Participantes")
        colLimiteParticipantes.cellValueFactory = PropertyValueFactory("limiteParticipantes")
        val colStatus = TableColumn<Palestra, StatusEnum>("Status")
        colStatus.cellValueFactory = PropertyValueFactory("status")
        val actionColumn = TableColumn<Palestra, Void>("Ações")
        actionColumn.setCellFactory {
            object : TableCell<Palestra, Void>() {
                private val btnCancelar = Button("Cancelar")
                private val btnAtualizarHorario = Button("Atualizar horário")

                init {
                    btnCancelar.setOnAction {
                        val palestra = tableView.items[index]
                        cancelarPalestraModal(palestra)
                    }
                    btnAtualizarHorario.setOnAction {
                        val palestra = tableView.items[index]
                        atualizarHorarioPalestraModal(palestra)
                    }
                }

                override fun updateItem(item: Void?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        graphic = null
                    } else {
                        graphic = HBox(10.0, btnCancelar, btnAtualizarHorario)
                    }
                }
            }
        }

        tableView.columns.addAll(
            colId,
            colTitulo,
            colPalestrante,
            colLocal,
            colDuracao,
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

    fun cancelarPalestraModal(palestra: Palestra) {
        val modalStage = Stage()
        modalStage.initModality(Modality.APPLICATION_MODAL)
        modalStage.title = "Tem certeza de que deseja cancelar a palestra?"

        val alerta = Label("Esta ação não poderá ser revertida.")
        val btnConfirmar = Button("Confirmar")
        val btnVoltar = Button("Voltar")

        btnConfirmar.setOnAction {
            val sucesso = palestraDAO.deletePalestra(palestra.id)
            if (sucesso) {
                evento.agenda.removerPalestraPeloId(palestra.id)
                vbox.children.clear()
                primaryStage.scene = gerenciarPalestrasScene()
                notifyUsers(
                    "A palestra ${palestra.titulo} agendada para ${palestra.data} foi cancelada",
                    palestra.participantes.buscarTodosParticipantes()!!.filterNotNull().toTypedArray()
                )
            } else {
                println("Erro ao cancelar palestra!")
            }
            modalStage.close()
        }

        btnVoltar.setOnAction {
            modalStage.close()
        }

        val hbox = HBox(10.0, btnConfirmar, btnVoltar)
        val vbox = VBox(10.0, alerta, hbox)
        val modalScene = Scene(vbox, 400.0, 150.0)

        modalStage.scene = modalScene
        modalStage.showAndWait()
    }

    private fun buscarHorarios(turno: TurnoEnum): ListaEstatica<String> {
        return horarioDAO.getHorariosPorTurno(turno)
    }

    fun atualizarHorarioPalestraModal(palestra: Palestra) {
        val modalStage = Stage()
        modalStage.initModality(Modality.APPLICATION_MODAL)
        modalStage.title = "Atualizar horário da palestra ${palestra.titulo}"

        val tituloLabel = Label("Título: ${palestra.titulo}")
        val dataLabel = Label("Data: ${palestra.data}")
        val localLabel = Label("Local: ${palestra.local}")
        val limLabel = Label("Limite de participantes: ${palestra.limiteParticipantes}")
        val duracaoLabel = Label("Duração da palestra em horas")
        val inputDuracao = TextField()
        val horarioInicioLabel = Label("Horário de início")
        val comboBoxHorarioInicio = ComboBox<String>()
        val horarioFimLabel = Label("Horário de término")
        val inputHorarioFim = TextField()
        val horarios = buscarHorarios(evento.turno)
        val palestrasExistentes = palestraDAO.getPalestras(evento.id).buscarTodasPalestrasPorDia(palestra.data)

        for (horario in horarios.selecionarTodos()) {
            if (horario == null) {
                continue
            } else {
                if (isHorarioDisponivel(
                        LocalTime.parse(horario.toString()),
                        palestra.duracao,
                        palestrasExistentes
                    )
                ) {
                    comboBoxHorarioInicio.items.add(horario.toString())
                }
            }
        }

        inputDuracao.textProperty().addListener { _, _, valor ->
            comboBoxHorarioInicio.items.clear()
            inputHorarioFim.clear()
            for (horario in horarios.selecionarTodos()) {
                if (horario == null) {
                    continue
                } else {
                    if (isHorarioDisponivel(
                            LocalTime.parse(horario.toString()),
                            inputDuracao.text.toLong(),
                            palestrasExistentes
                        )
                    ) {
                        comboBoxHorarioInicio.items.add(horario.toString())
                    }
                }
            }
        }

        comboBoxHorarioInicio.valueProperty().addListener { _, _, valor ->
            inputHorarioFim.clear()
            if (valor != null && inputDuracao.text != null && palestrasExistentes != null) {
                val horaInicio = LocalTime.parse(valor)
                if (isHorarioDisponivel(horaInicio, inputDuracao.text.toLong(), palestrasExistentes)) {
                    inputHorarioFim.text = horaInicio.plusHours(inputDuracao.text.toLong()).toString()
                }
            }
        }

        val btnConfirmar = Button("Confirmar")
        val btnVoltar = Button("Voltar")

        btnConfirmar.setOnAction {
            val atualizar = palestraDAO.updatePalestra(
                palestra,
                inputDuracao.text.toLong(),
                LocalTime.parse(comboBoxHorarioInicio.value),
                LocalTime.parse(inputHorarioFim.text)
            )
            if (atualizar) {
                vbox.children.clear()
                evento.agenda.atualizarHorarioPalestra(
                    palestra,
                    inputDuracao.text.toLong(),
                    LocalTime.parse(comboBoxHorarioInicio.value),
                    LocalTime.parse(inputHorarioFim.text)
                )
                println("Palestra atualizada!")
                primaryStage.scene = gerenciarPalestrasScene()
                loadDataDB()
                notifyUsers(
                    "O horário da palestra ${palestra.titulo} foi atualizado para às ${comboBoxHorarioInicio.value}",
                    palestra.participantes.buscarTodosParticipantes()!!.filterNotNull().toTypedArray()
                )
            } else {
                println("Erro ao atualizar horário da palestra!")
            }
            modalStage.close()
        }

        btnVoltar.setOnAction {
            modalStage.close()
        }

        val vboxDuracao = VBox(10.0, duracaoLabel, inputDuracao)
        val vboxPalestra = VBox(10.0, tituloLabel, dataLabel, localLabel, limLabel)
        val vboxHorarioInicio = VBox(10.0, horarioInicioLabel, comboBoxHorarioInicio)
        val vboxHorarioFim = VBox(10.0, horarioFimLabel, inputHorarioFim)
        val hboxBtn = HBox(10.0, btnConfirmar, btnVoltar)
        val vbox = VBox(10.0, vboxPalestra, vboxDuracao, vboxHorarioInicio, vboxHorarioFim, hboxBtn)
        val modalScene = Scene(vbox, 500.0, 350.0)

        modalStage.scene = modalScene
        modalStage.showAndWait()
    }

}