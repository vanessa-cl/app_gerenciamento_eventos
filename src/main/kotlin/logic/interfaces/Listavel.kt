package logic.interfaces

interface Listavel {
    fun anexar(dado: T)
    fun Inserir(posicao: Int, dado: T)
    fun selecionar(posicao: Int): T?
    fun selecionarTodos(): Array<T?>
    fun buscarPosicao(dado: T?): Int
    fun atualizar(posicao: Int, dado: T?)
    fun apagar(posicao: Int): T?
    fun apagarTodos(): Array<T?>
    fun estaVazia(): Boolean
    fun estaCheia(): Boolean
}
