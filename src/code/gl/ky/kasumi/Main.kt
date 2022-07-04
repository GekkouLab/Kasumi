package gl.ky.kasumi

import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {
    override fun onEnable() {
        main = this
        log = logger
        Metrics(this, 15591)
        log.info("Plugin [Kasumi] has been enabled.")
    }

    override fun onDisable() {
    }

    fun reload() {
    }

}
