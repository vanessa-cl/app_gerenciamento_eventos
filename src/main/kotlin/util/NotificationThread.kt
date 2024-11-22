package util

import logic.entities.Participante

class NotificationThread(private val usuario: Participante) : Thread() {
    fun notificarUsuario(message: String, usuario: Participante) {
        if (usuario == this.usuario) {
            println(message)
        }
        return
    }

    override fun run() {
        println("Thread iniciada")
    }
}