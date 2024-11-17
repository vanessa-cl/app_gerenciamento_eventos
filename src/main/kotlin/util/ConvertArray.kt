package util

inline fun <reified T> convertArray(array: Array<Any?>): Array<T> {
    val arrayConv = arrayOfNulls<T>(array.size)
    for (i in array.indices) {
        if (array[i] == null) {
            continue
        }
        arrayConv[i] = array[i] as T
    }
    return arrayConv as Array<T>
}