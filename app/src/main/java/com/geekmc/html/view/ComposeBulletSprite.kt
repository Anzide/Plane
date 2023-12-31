package com.geekmc.html.view

/**
 * 子弹
 */
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.geekmc.html.model.Bullet
import com.geekmc.html.model.GameAction
import com.geekmc.html.model.GameState
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * 子弹从玩家飞机顶部发射，只能沿着X轴运动，超出屏幕则销毁，与敌机碰撞也销毁，同时计算得分
 */
@InternalCoroutinesApi
@Composable
fun BulletSprite(
    gameState: GameState = GameState.Waiting,
    bulletList: List<Bullet> = mutableListOf(),
    gameAction: GameAction = GameAction()
) {
    //重复动画，1秒60帧
    val infiniteTransition = rememberInfiniteTransition()
    val frame by infiniteTransition.animateInt(
        initialValue = 0,
        targetValue = 60,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    //游戏不在进行中
    if (gameState != GameState.Running) {
        return
    }

    //每100毫秒生成一颗子弹
    if (frame % 6 == 0) {
        gameAction.createBullet()
    }

    for (bullet in bulletList) {

        if (bullet.isAlive()) {

            //子弹位移
            gameAction.moveBullet(bullet)

            //显示子弹图片
            BulletShootingSprite(bullet)
        }

    }

}

/**
 * 更新子弹x、y值，显示子弹图片
 */
@InternalCoroutinesApi
@Composable
fun BulletShootingSprite(
    bullet: Bullet = Bullet()
) {
    //绘制图片
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = bullet.drawableId),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .offset {
                    IntOffset(
                        bullet.x,
                        bullet.y
                    )
                }
                .width(bullet.width)
                .height(bullet.height)
                .alpha(
                    if (bullet.isAlive()) {
                        1f
                    } else {
                        0f
                    }
                )
        )
    }
}

@Preview()
@Composable
fun PreviewBulletSprite() {

}

