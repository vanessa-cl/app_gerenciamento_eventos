package util

import logic.entities.Participante

fun notifyUsers(mensagem: String, participantes: Array<Participante>) {
    for (participante in participantes) {
        val notificationThread = NotificationThread(participante)
        notificationThread.start()
        notificationThread.exibirNotificacao(mensagem)
    }
}