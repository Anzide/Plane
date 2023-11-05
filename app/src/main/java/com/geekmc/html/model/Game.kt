package com.geekmc.html.model

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * 游戏状态
 */
enum class GameState {
    Waiting, // wait to start
    Running, // gaming
    Paused, // pause
    Dying, // hit enemy and dying
    Over, // over
    Exit // finish activity
}

/**
 * 游戏动作（需要跟Compose UI 交互的才需在这定义）
 */
@InternalCoroutinesApi
data class GameAction(
    val start: () -> Unit = {},
    val pause: () -> Unit = {},
    val reset: () -> Unit = {},
    val die: () -> Unit = {},
    val over: () -> Unit = {},
    val exit: () -> Unit = {},
    val playByRes: (resId: Int) -> Unit = { _: Int -> },
    val movePlayerPlane: (x: Int, y: Int) -> Unit = { _: Int, _: Int -> },//移动玩家飞机
    val dragPlayerPlane: (dragAmount: Offset) -> Unit = { _: Offset -> },//拖拽玩家飞机
    val createBullet: () -> Unit = { },//子弹生成
    val moveBullet: (bullet: Bullet) -> Unit = { _: Bullet -> },//子弹射击
    val moveEnemyPlane: (enemyPlane: EnemyPlane, onBombAnimChange: (Boolean) -> Unit) -> Unit ={ _: EnemyPlane, _: (Boolean) -> Unit -> },//敌机飞行
    val moveAward: (award: Award) -> Unit = { _: Award -> },//奖励下落
    val destroyAllEnemy: () -> Unit = {},//摧毁所有敌机
)

/**
 * 游戏动作接口定义
 */
interface IGameAction {

    fun start()
    fun pause()
    fun reset()
    fun die()
    fun over()
    fun exit()
    fun score()
    fun award()
    fun levelUp()
    fun movePlayerPlane()
    fun dragPlayerPlane()
    fun createBullet()
    fun moveBullet()
    fun moveEnemyPlane()
    fun moveAward()
    fun collisionDetect()
    fun destroyAllEnemy()

}
