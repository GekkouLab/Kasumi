package gl.ky.kasumi.stdlib

import gl.ky.kasumi.old.Interpreter

object Loader {
    fun loadStdLibFunctions() {
        Interpreter.actionPool += mapOf(
            "kill" to ::kill,
        )
    }
}