package util

class SequentialId(private var currentId: Int) {

    fun gerarId(): Int {
        return ++currentId
    }
}