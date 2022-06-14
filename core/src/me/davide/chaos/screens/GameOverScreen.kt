package me.davide.chaos.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.ScreenUtils
import me.davide.chaos.Chaos

class GameOverScreen(private val game: Chaos) : Screen {

    private val camera = OrthographicCamera()

    init {
        camera.setToOrtho(false, 800f, 600f)
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0.5f, 0f, 0f, 1f)

        camera.update()
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()
        game.font.draw(game.batch, "You lost!", 300f, 300f)
        game.font.draw(game.batch, "Tap anywhere to return to the main screen", 300f, 200f)
        game.batch.end()

        if (Gdx.input.justTouched()) {
            game.screen = MainMenuScreen(game)
            dispose()
        }
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {

    }
}