package gl.ky.kasumi.stdlib

import gl.ky.kasumi.old.Environment
import org.bukkit.entity.Player

fun kill(env: Environment) {
    env.getAs<Player>("player")?.let {
        it.damage(it.health)
    }
}





