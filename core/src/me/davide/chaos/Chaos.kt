package me.davide.chaos

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import me.davide.chaos.screens.MainMenuScreen

class Chaos : Game() {
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()

        this.setScreen(MainMenuScreen(this))
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}