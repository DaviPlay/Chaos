package me.davide.chaos.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Bullet(private var x: Float, private var y: Float, private var direction: Vector2, angle: Float) : Entity {
    private val speed = 500
    var remove = false
    private val sprite = Sprite(Texture("bullet.png"))

    var rect = Rectangle()

    init {
        sprite.setSize(32f, 32f)
        sprite.rotation = angle

        rect.width = sprite.width
        rect.height = sprite.height
    }

    override fun update(delta: Float) {
        x += direction.x * speed * delta
        y += direction.y * speed * delta

        if (x > Gdx.graphics.width || x < 0 || y > Gdx.graphics.height || y < 0)
            remove = true
    }

    override fun render(batch: SpriteBatch) {
        sprite.setPosition(x, y)

        sprite.draw(batch)
    }
}