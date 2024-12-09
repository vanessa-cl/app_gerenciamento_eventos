package ui

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.collections.ListaEventos
import logic.entities.Participante

class TelaMenuEventos(
    private val primaryStage: Stage,
    private val mainApp: MainApp,
    private val eventos: ListaEventos,
    private val usuarioLogado: Participante
) {
    private var vbox = VBox(10.0)

    fun menuEventosScene(): Scene {
        val pageLabel = Label("Menu de Eventos")
        val pageSublabel =
            Label("Escolha um dos eventos abaixo para consultar detalhes como data, valor da inscrição, entre outros.")
        val btnVoltar = Button("Voltar")
        btnVoltar.setOnAction {
            primaryStage.scene = mainApp.getInitialScene()
        }
        val vboxEventos = exibirEventosBox()
        vbox.children.addAll(
            pageLabel,
            pageSublabel,
            vboxEventos,
            btnVoltar
        )

        val titledPane = TitledPane("Menu de Eventos", vbox)
        val scene = Scene(titledPane, 1000.0, 600.0)
        primaryStage.title = "Menu de Eventos"
        primaryStage.scene = scene
        primaryStage.show()
        return scene
    }

    private fun exibirEventosBox(): VBox {
        val vboxEventos = VBox(10.0)
        eventos.buscarTodosEventos()?.filterNotNull()?.forEach { evento ->
            val btnEvento = Button(evento.nome)
            btnEvento.setOnAction {
                println("Evento selecionado: ${evento.nome}")
                val telaDetalhesEvento = TelaDetalhesEvento(primaryStage, evento, this, usuarioLogado)
                vbox.children.clear()
                primaryStage.scene = telaDetalhesEvento.detalhesEventoScene()
            }
            vboxEventos.children.add(btnEvento)
        }
        return vboxEventos
    }
}