package logic.interfaces

interface Enfileiravel{
    fun enfileirar(dado: T?)
    fun desenfileirar(): T?
    fun atualizar(dado: T?)
    fun frente(): T?
    fun estaVazia(): Boolean
    fun estaCheia(): Boolean
    fun imprimir(): String
}
