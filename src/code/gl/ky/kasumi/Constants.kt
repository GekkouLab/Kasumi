@file:JvmName("Constants")

package gl.ky.kasumi

import java.util.logging.Logger

lateinit var main: Main
lateinit var log: Logger

fun info(s: String) = log.info(s)