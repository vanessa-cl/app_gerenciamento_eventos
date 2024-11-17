package logic.interfaces

interface Listavel<T> {
    fun anexar(dado: T)
    fun inserir(posicao: Int, dado: T)
    fun selecionar(posicao: Int): T?
    fun selecionarTodos(): Array<Any?>
    fun buscarPosicao(dado: T?): Int
    fun atualizar(posicao: Int, dado: T?)
    fun apagar(posicao: Int): T?
    fun apagarTodos(): Array<T?>
    fun estaVazia(): Boolean
    fun estaCheia(): Boolean
}
