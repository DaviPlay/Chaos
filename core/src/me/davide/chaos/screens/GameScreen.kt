package me.davide.chaos.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import me.davide.chaos.Chaos
import me.davide.chaos.entities.Bullet
import me.davide.chaos.guns.AR
import kotlin.math.atan2

class GameScreen(private val game: Chaos) : Screen {
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(800f, 600f)
    private val player = Rectangle()
    private val m4 = AR("M4", 300, 30, true)
    private val enemies = Array<Rectangle>()
    private var lastEnemySpawn = 0L
    private var isAttacking = false
    private var equipped = false

    //Disposables
    private val playerImage = Sprite(Texture("main.png"))
    private val enemyImage = Sprite(Texture("enemy.png"))
    private val swipeImage = Texture("swipe.png")
    private val m4Image = Sprite(Texture("m4.png"))
    private val music = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"))

    private val bullets = ArrayList<Bullet>()

    init {
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        player.x = (Gdx.graphics.width / 2 - 16 / 2).toFloat()
        player.y = 20f
        player.width = 64f
        player.height = 64f

        m4.rect.x = 400f
        m4.rect.y = 300f
        m4Image.setSize(64f, 64f)

        music.volume = .1f
        music.isLooping = true
        //spawnEnemies()
    }

    override fun show() {
        //music.play()
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(.5f, .5f, .5f, .75f)

        viewport.apply()
        game.batch.projectionMatrix = viewport.camera.combined
        game.batch.begin()

        game.font.draw(game.batch, "Welcome to lavinia!", 350f, 600f)
        game.batch.draw(playerImage, player.x, player.y, 64f, 64f)
        m4Image.setPosition(m4.rect.x, m4.rect.y)
        m4Image.draw(game.batch)

        //Draw enemies
        for (enemy in enemies)
            game.batch.draw(enemyImage, enemy.x, enemy.y, 64f, 64f)

        equip()

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
            m4.shoot(camera, bullets, delta)

        updateEntities(delta)

        game.batch.end()

        movement()

        //Spawn enemies
        //if (TimeUtils.nanoTime() - lastEnemySpawn > 1 * 10.0.pow(9.0))
        //    spawnEnemies()

        //enemyAI()
    }

    private fun updateEntities(delta: Float) {
        //Bullets
        val bulletsBuffer = ArrayList<Bullet>()
        for (bullet in bullets) {
            bullet.update(delta)

            if (bullet.remove)
                bulletsBuffer.add(bullet)
        }
        bullets.removeAll(bulletsBuffer.toSet())

        for (bullet in bullets)
            bullet.render(game.batch)

        //Weapon position
        if (equipped) {
            val input = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
            val angle = MathUtils.radiansToDegrees * atan2(input.y - m4.rect.y, input.x - m4.rect.x)
            m4Image.rotation = angle
        }

        //Flipping
        if(Gdx.input.x < player.x + (player.width / 2)) {
            playerImage.setFlip(true, false)

            if (equipped) {
                m4Image.setFlip(false, true)
                m4.rect.y += m4.rect.width / 2
            }
        }
        else {
            playerImage.setFlip(false, false)

            if (equipped)
                m4Image.setFlip(false, false)
        }
    }

    private fun equip() {
        if (player.overlaps(m4.rect) && Gdx.input.isKeyJustPressed(Input.Keys.E))
            equipped = true

        if (equipped) {
            m4.rect.x = player.x + 30
            m4.rect.y = player.y
        }
    }

    private fun movement() {
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.y += 3
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.y -= 3
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.x += 3
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            player.x -= 3

        //No out of bounds
        //Left
        player.x = player.x.coerceAtLeast(0f)
        //Right
        player.x = player.x.coerceAtMost(Gdx.graphics.width.toFloat() - 64)
        //Up
        player.y = player.y.coerceAtMost(Gdx.graphics.height.toFloat())
        //Down
        player.y = player.y.coerceAtLeast(0f)
    }

    private fun spawnEnemies() {
        val enemy = Rectangle()
        enemy.x = MathUtils.random(0f, Gdx.graphics.width - 64f)
        enemy.y = MathUtils.random(0f, Gdx.graphics.height - 64f)
        enemy.width = 64f
        enemy.height = 64f

        enemies.add(enemy)
        lastEnemySpawn = TimeUtils.nanoTime()
    }

    private fun enemyAI() {
        //Move enemies
        val iter = enemies.iterator()
        while (iter.hasNext()) {
            val enemy = iter.next()
            val speed = 100
            val elapsed = .01f
            val direction = Vector2()
            direction.x = player.x - enemy.x
            direction.y = player.y - enemy.y
            direction.nor()

            enemy.x += direction.x * speed * elapsed
            enemy.y += direction.y * speed * elapsed

            //Delete enemy if attacked
            if (enemy.overlaps(player) && isAttacking)
                iter.remove()
            //Kill player if touched
            else if (enemy.overlaps(player)) {
                game.screen = GameOverScreen(game)
                dispose()
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        playerImage.texture.dispose()
        enemyImage.texture.dispose()
        swipeImage.dispose()
        m4Image.texture.dispose()
        music.dispose()
    }
}
