package me.davide.chaos.guns

import com.badlogic.gdx.graphics.Camera
import me.davide.chaos.entities.Bullet

interface Gun {
    fun shoot(camera: Camera, bullets: ArrayList<Bullet>, delta: Float)
    fun reload()
}