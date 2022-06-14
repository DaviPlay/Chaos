package me.davide.chaos.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface Entity {
    fun update(delta: Float)
    fun render(batch: SpriteBatch)
}