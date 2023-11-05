package com.geekmc.html.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.geekmc.html.R
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * 实体类
 */
@InternalCoroutinesApi
open class Entity(
    open var id: Long = System.currentTimeMillis(),
    open var name: String = "默认实体",
    open var type: Int = 0,
    @DrawableRes open val drawableIds: List<Int> = listOf(
        R.drawable.sprite_player_plane_1,
        R.drawable.sprite_player_plane_2
    ),
    @DrawableRes open val bombDrawableId: Int = R.drawable.sprite_explosion_seq, //敌机爆炸帧动画资源
    open var segment: Int = 14, //爆炸效果由segment个片段组成:玩家飞机是4，小飞机是3，中飞机是4大飞机是6，explosion是14
    open var x: Int = 0,
    open var y: Int = 0,
    open var startX: Int = -100,
    open var startY: Int = -100,
    open var width: Dp = BULLET_SPRITE_WIDTH.dp,
    open var height: Dp = BULLET_SPRITE_HEIGHT.dp,
    open var velocity: Int = 40,
    open var state: EntityState = EntityState.LIFE, //控制是否显示
    open var init: Boolean = false, //是否初始化，主要用于精灵金初始化起点x，y坐标等，这里为什么不用state控制？state用于否显示，init用于重新初始化数据，而且必须是精灵离开屏幕后（走完整个移动的周期）才能重新初始化
) {

    fun isAlive() = state == EntityState.LIFE

    fun isDead() = state == EntityState.DEATH

    open fun reBirth() {
        state = EntityState.LIFE
    }

    open fun die() {
        state = EntityState.DEATH
    }

    override fun toString(): String {
        return "Sprite(id=$id, name='$name', drawableIds=$drawableIds, bombDrawableId=$bombDrawableId, segment=$segment, x=$x, y=$y, width=$width, height=$height, state=$state)"
    }
}


/**
 * 玩家飞机精灵
 */
const val PLAYER_PLANE_SPRITE_SIZE = 60
const val PLAYER_PLANE_PROTECT = 60

@InternalCoroutinesApi
data class PlayerPlane(
    override var id: Long = System.currentTimeMillis(), //id
    override var name: String = "雷电",
    @DrawableRes override val drawableIds: List<Int> = listOf(
        R.drawable.sprite_player_plane_1,
        R.drawable.sprite_player_plane_2
    ),
    @DrawableRes val bombDrawableIds: Int = R.drawable.sprite_player_plane_bomb_seq, //玩家飞机爆炸帧动画资源
    override var segment: Int = 4,
    override var x: Int = -100,
    override var y: Int = -100,
    override var width: Dp = PLAYER_PLANE_SPRITE_SIZE.dp,
    override var height: Dp = PLAYER_PLANE_SPRITE_SIZE.dp,
    var protect: Int = PLAYER_PLANE_PROTECT,
    var life: Int = 1,
    var animateIn: Boolean = true,
    var bulletAward: Int = BULLET_DOUBLE shl 16 or 0,
    var bombAward: Int = 0 shl 16 or 0,
) : Entity() {

    /**
     * 减少保护次数，为0的时候碰撞即爆炸
     */
    fun reduceProtect() {
        if (protect > 0) {
            protect--
        }
    }

    fun isNoProtect() = protect <= 0

    override fun reBirth() {
        state = EntityState.LIFE
        animateIn = true
        x = startX
        y = startY
        protect = PLAYER_PLANE_PROTECT
        bulletAward = 0
        bombAward = 0
    }
}

/**
 * 子弹精灵
 */
const val BULLET_SPRITE_WIDTH = 6
const val BULLET_SPRITE_HEIGHT = 18
const val BULLET_SINGLE = 0
const val BULLET_DOUBLE = 1

@InternalCoroutinesApi
data class Bullet(
    override var id: Long = System.currentTimeMillis(), //id
    override var name: String = "蓝色单发子弹",
    override var type: Int = BULLET_SINGLE, //类型:0单发子弹，1双发子弹
    @DrawableRes val drawableId: Int = R.drawable.sprite_bullet_single, //子弹资源图标
    override var width: Dp = BULLET_SPRITE_WIDTH.dp, //宽
    override var height: Dp = BULLET_SPRITE_HEIGHT.dp, //高
    override var x: Int = 0, //实时x轴坐标
    override var y: Int = 0, //实时y轴坐标
    override var state: EntityState = EntityState.DEATH, //默认死亡
    override var init: Boolean = false, //默认未初始化
    var hit: Int = 1,//击打能力，击中一次敌人，敌人减掉的生命值
) : Entity() {

    /**
     * 是否失效，飞出屏幕就失效(这里判断y<0就行了，如果y<0时跟敌机碰撞，敌机肯定是在屏幕外看不到的，所以这里这么处理是可以的)
     */
    fun isInvalid() = this.y < 0

    /**
     * 位移(射击)
     */
    fun move() {
        this.x = this.startX
        this.y -= this.velocity
    }
}


/**
 * 道具奖励精灵
 */

const val AWARD_BULLET = 0
const val AWARD_BOMB = 1

@InternalCoroutinesApi
data class Award(
    override var id: Long = System.currentTimeMillis(), //id
    override var name: String = "子弹道具奖励",
    override var type: Int = AWARD_BULLET, //类型:0子弹道具奖励，1爆炸道具奖励
    @DrawableRes var drawableId: Int = R.drawable.sprite_blue_shot_down, //子弹资源图标
    override var width: Dp = 50.dp, //宽
    override var height: Dp = 80.dp, //高
    override var velocity: Int = 20, //飞行速度（每帧移动的像素）
    override var x: Int = 0, //实时x轴坐标
    override var y: Int = 0, //实时y轴坐标
    override var state: EntityState = EntityState.DEATH, //默认死亡
    override var init: Boolean = false, //默认未初始化
    var amount: Int = 1, //数量

) : Entity(){

    fun move() {
        this.y += this.velocity
    }
}

/**
 * 敌机精灵
 */
const val SMALL_ENEMY_PLANE_SPRITE_SIZE = 40
const val MIDDLE_ENEMY_PLANE_SPRITE_SIZE = 60
const val BIG_ENEMY_PLANE_SPRITE_SIZE = 100

@InternalCoroutinesApi
data class EnemyPlane(
    override var id: Long = System.currentTimeMillis(), //id
    override var name: String = "敌军侦察机",
    override var type: Int = 0,
    @DrawableRes override val drawableIds: List<Int> = listOf(R.drawable.sprite_small_enemy_plane), //飞机资源图标
    @DrawableRes override val bombDrawableId: Int = R.drawable.sprite_small_enemy_plane_seq, //敌机爆炸帧动画资源
    override var segment: Int = 3, //爆炸效果由segment个片段组成，小飞机是3，中飞机是4，大飞机是6
    override var x: Int = 0, //敌机当前在X轴上的位置
    override var y: Int = 0, //敌机当前在Y轴上的位置
    override var width: Dp = SMALL_ENEMY_PLANE_SPRITE_SIZE.dp, //宽
    override var height: Dp = SMALL_ENEMY_PLANE_SPRITE_SIZE.dp, //高
    override var velocity: Int = 1, //飞行速度（每帧移动的像素）
    var bombX: Int = -100, //爆炸动画当前在X轴上的位置
    var bombY: Int = -100, //爆炸动画当前在Y轴上的位置
    val power: Int = 1, //生命值，敌机的抗打击能力
    var hit: Int = 0, //被击中消耗的生命值
    val value: Int = 10, //打一个敌机的得分

) : Entity() {

    fun beHit(reduce: Int) {
        hit += reduce
    }

    fun isNoPower() = (power - hit) <= 0

    fun bomb() {
        hit = power
    }

    fun move() {
        this.y += this.velocity
    }

    fun getRealDrawableId(): Int {
        var realDrawableId = drawableIds[0]
        //如果被击中，则重新计算显示的图片index，先算生命值按图片数量平均，一张图片对应的powerSegment是多少，再用被击中消耗的生命值除以powerSegment，得到倍数
        if (hit > 0) {
            val hitPerPower = hit / (power / drawableIds.size)
            val drawableIdsIndex = when {
                hitPerPower < 0 -> {
                    0
                }
                hitPerPower >= drawableIds.size -> {
                    drawableIds.size - 1
                }
                else -> {
                    hitPerPower
                }
            }
            realDrawableId = drawableIds[drawableIdsIndex]
        }
        return realDrawableId
    }

    override fun reBirth() {
        state = EntityState.LIFE
        hit = 0
    }

    override fun die() {
        state = EntityState.DEATH
        bombX = x
        bombY = y
    }

    override fun toString(): String {
        return "EnemyPlane(state=$state, id=$id, name='$name', drawableIds=$drawableIds, bombDrawableId=$bombDrawableId, segment=$segment, x=$x, y=$y, width=$width, height=$height, power=$power, hit=$hit, value=$value, startY=$startY)"
    }


}

/**
 * 爆炸动画精灵
 */
@InternalCoroutinesApi
data class Bomb(
    override var id: Long = System.currentTimeMillis(), //id
    override var name: String = "爆炸动画",
    @DrawableRes override var bombDrawableId: Int = R.drawable.sprite_explosion_seq, //爆炸帧动画资源
    override var segment: Int = 14, //爆炸效果由segment个片段组成，小飞机是3，中飞机是4，大飞机是6，explosion是14
    override var x: Int = 200, //当前在X轴上的位置
    override var y: Int = 200, //当前在Y轴上的位置
    override var width: Dp = BIG_ENEMY_PLANE_SPRITE_SIZE.dp, //宽
    override var height: Dp = BIG_ENEMY_PLANE_SPRITE_SIZE.dp, //高
    override var state: EntityState = EntityState.DEATH //爆炸动画默认不显示
) : Entity()