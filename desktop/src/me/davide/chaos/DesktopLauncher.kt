package me.davide.chaos

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object DesktopLauncher {

    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setWindowedMode(800, 600)
        config.setForegroundFPS(144)
        config.setTitle("Chaos")
        Lwjgl3Application(Chaos(), config)
    }
}