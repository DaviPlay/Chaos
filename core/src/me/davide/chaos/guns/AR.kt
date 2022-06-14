package me.davide.chaos.guns

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import me.davide.chaos.entities.Bullet
import kotlin.math.atan2

class AR(name: String, maxAmmo: Int, maxMagazine: Int, isFullAuto: Boolean) : Gun {
    var ammoCount: Int
    var magazine: Int
    var rect = Rectangle()

    init {
        ammoCount = maxAmmo
        magazine = maxMagazine

        rect.width = 64f
        rect.height = 64f
    }

    override fun shoot(camera: Camera, bullets: ArrayList<Bullet>, delta: Float) {
        val direction = Vector2()
        val input = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        direction.x = input.x - rect.x
        direction.y = input.y - rect.y
        direction.nor()

        val angle = MathUtils.radiansToDegrees * atan2(direction.y, direction.x)

        bullets.add(Bullet(rect.x + 50, rect.y + 25, direction, angle))
    }

    override fun reload() {
        TODO("Not yet implemented")
    }
}