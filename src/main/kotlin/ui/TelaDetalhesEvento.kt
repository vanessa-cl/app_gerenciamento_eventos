package ui

import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.database.PalestraDAO
import logic.entities.Evento
import logic.entities.Palestra
import logic.entities.Participante
import util.ui.PalestrasUI

class TelaDetalhesEvento(
    private val primaryStage: Stage,
    private val evento: Evento,
    private val telaMenuEventos: TelaMenuEventos,
    private val usuarioLogado: Participante
) {
    private val vbox = VBox(10.0)
    private val palestraDAO = PalestraDAO()
    private val palestrasUI = PalestrasUI()

    fun detalhesEventoScene(): Scene {
        val pageLabel = Label(evento.nome)
        val vboxInfo = VBox(10.0)
        val infoLabel = Label("Detalhes do Evento")
        val descricaoLabel = Label("Descrição: ${evento.descricao}")
        val periodoLabel = Label("Período: ${evento.dataInicio} até ${evento.dataFim}")
        val valorInscricaoLabel = Label("Valor da inscrição: ${evento.valorInscricao}")
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            vbox.children.clear()
            primaryStage.scene = telaMenuEventos.menuEventosScene()
        }
        vboxInfo.children.addAll(
            pageLabel,
            infoLabel,
            descricaoLabel,
            periodoLabel,
            valorInscricaoLabel,
            btnVoltar
        )

        val hboxActions = HBox(10.0)
        val btnConsultarAgenda = Button("Consultar agenda do evento")
        btnConsultarAgenda.setOnAction {
            vbox.children.clear()
            primaryStage.scene = this.agendaEventoScene()
        }
        val btnMinhasInscricoes = Button("Minhas inscrições")
        btnMinhasInscricoes.setOnAction {
            vbox.children.clear()
            primaryStage.scene = this.inscricoesUsuarioScene()
        }
        hboxActions.children.addAll(btnConsultarAgenda, btnMinhasInscricoes)

        vbox.children.addAll(
            vboxInfo,
            hboxActions
        )

        val titledPane = TitledPane("Detalhes do Evento", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Detalhes do Evento"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun agendaEventoScene(): Scene {
        val pageLabel = Label("Agenda do Evento ${evento.nome}")
        val palestras = palestraDAO.getPalestras(evento.id).buscarTodasPalestras()
        if (palestras == null) {
            vbox.children.add(Label("Não há palestras cadastradas para este evento"))
        }

        val table = agendaEventoTable(palestras!!)
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            vbox.children.clear()
            primaryStage.scene = this.detalhesEventoScene()
        }
        vbox.children.addAll(
            pageLabel,
            table,
            btnVoltar
        )

        val titledPane = TitledPane("Agenda do Evento", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Agenda do Evento"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun agendaEventoTable(palestras: Array<Palestra?>): TableView<Palestra> {
        val actionColumn = palestrasUI.getActionCollumn("Inscreva-se", ::inscreverUsuario)
        val tableView = palestrasUI.baseTablePalestras(palestras, actionColumn)
        return tableView
    }

    fun inscreverUsuario(palestra: Palestra) {
        val inscricao = palestraDAO.subscribeParticipante(palestra.id, usuarioLogado.id)
        val label = Label(if (inscricao) "Inscrição realizada com sucesso" else "Erro ao realizar inscrição")
        vbox.children.add(label)
    }

    private fun inscricoesUsuarioScene(): Scene {
        val pageLabel = Label("Minhas inscrições no Evento ${evento.nome}")
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            vbox.children.clear()
            primaryStage.scene = this.detalhesEventoScene()
        }
        val palestras = palestraDAO.getSubscriptionsParticipante(evento.id, usuarioLogado.id).buscarTodasPalestras()
        println(palestras)
        if (palestras == null) {
            vbox.children.addAll(
                Label("Você não está inscrito em nenhuma palestra deste evento"),
                btnVoltar
            )
        }

        val table = inscricoesUsuarioTable(palestras!!)

        vbox.children.addAll(
            pageLabel,
            table,
            btnVoltar
        )

        val titledPane = TitledPane("Minhas inscrições", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Minhas inscrições"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun inscricoesUsuarioTable(palestras: Array<Palestra?>): TableView<Palestra> {
        val actionColumn = palestrasUI.getActionCollumn("Cancelar inscrição", ::cancelarInscricao)
        val tableView = palestrasUI.baseTablePalestras(palestras, actionColumn)
        return tableView
    }

    fun cancelarInscricao(palestra: Palestra) {
        val inscricao = palestraDAO.unsubscribeParticipante(palestra.id, usuarioLogado.id)
        val label = Label(if (inscricao) "Inscrição cancelada com sucesso" else "Erro ao cancelar inscrição")
        vbox.children.add(label)
    }

}