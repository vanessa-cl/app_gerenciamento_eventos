package ui

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.FlowPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.collections.ListaEventos
import logic.entities.Participante
import util.ui.Header

class TelaMenuEventos(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val eventos: ListaEventos,
    private val usuarioLogado: Participante
) {
    private var titulo = "Eventos"
    private val header = Header(primaryStage, mainApp, usuarioLogado, titulo)

    fun menuEventosScene(): Scene {
        val menuEventosVbox = VBox(10.0)
        menuEventosVbox.children.add(header.getHeader())

        menuEventosVbox.children.addAll(
            VBox(
                10.0,
                HBox(
                    10.0,
                    Label("Escolha um dos eventos abaixo para consultar detalhes como data, valor da inscrição, entre outros.")
                ).apply { alignment = Pos.TOP_LEFT },
                exibirEventosBox().apply { alignment = Pos.CENTER; maxWidth = 1300.0 }
            ).apply { alignment = Pos.TOP_LEFT; padding = Insets(10.0) }
        )

        menuEventosVbox.apply { spacing = 10.0; padding = Insets(10.0) }
        val scene = Scene(menuEventosVbox, 900.0, 400.0)
        primaryStage.title = "Eventos"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun exibirEventosBox(): VBox {
        val vboxEventos = VBox(10.0)
        val flowPaneEventos = FlowPane(Orientation.HORIZONTAL, 10.0, 10.0)
        eventos.buscarTodosEventos()?.filterNotNull()?.forEach { evento ->
            val btnEvento = Button(evento.nome)
            btnEvento.setOnAction {
                val telaDetalhesEvento = TelaDetalhesEvento(primaryStage, mainApp, evento, this, usuarioLogado)
                primaryStage.scene = telaDetalhesEvento.detalhesEventoScene()
            }
            flowPaneEventos.children.add(btnEvento)
        }
        vboxEventos.children.add(flowPaneEventos)
        return vboxEventos
    }
}