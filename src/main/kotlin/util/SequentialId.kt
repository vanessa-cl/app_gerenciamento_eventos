package util

class SequentialId {
    private var currentId = 0

    fun gerarId(): Int {
        return ++currentId
    }
}