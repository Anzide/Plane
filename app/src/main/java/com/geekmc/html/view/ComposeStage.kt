package com.geekmc.html.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geekmc.html.model.GameAction
import com.geekmc.html.util.LogUtil
import com.geekmc.html.viewmodel.GameViewModel
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * 舞台
 */
@InternalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun Stage(gameViewModel: GameViewModel) {

    LogUtil.printLog(message = "Stage -------> ")

    //获取游戏状态
    val gameState by gameViewModel.gameStateFlow.collectAsState()

    //获取游戏分数
    val gameScore by gameViewModel.gameScoreStateFlow.collectAsState(0)

    //获取玩家飞机
    val playerPlane by gameViewModel.playerPlaneStateFlow.collectAsState()

    //获取所有子弹
    val bulletList by gameViewModel.bulletListStateFlow.collectAsState()

    //获取所有奖励
    val awardList by gameViewModel.awardListStateFlow.collectAsState()

    //获取所有敌军
    val enemyPlaneList by gameViewModel.enemyPlaneListStateFlow.collectAsState()

    //获取游戏动作函数
    val gameAction: GameAction = gameViewModel.onGameAction

    val modifier = Modifier.fillMaxSize()

    Box(modifier = modifier) {

        // 远景
        FarBackground(modifier)

        //游戏开始界面
        GameStart(gameState, playerPlane, gameAction)

        //玩家飞机
        PlayerPlaneSprite(
            gameState,
            playerPlane,
            gameAction
        )

        //玩家飞机出场飞入动画
        PlayerPlaneAnimIn(
            gameState,
            playerPlane,
            gameAction
        )

        //玩家飞机爆炸动画
        PlayerPlaneBombSprite(gameState, playerPlane, gameAction)

        //敌军飞机
        EnemyPlaneSprite(
            gameState,
            gameScore,
            enemyPlaneList,
            gameAction
        )

        //子弹
        BulletSprite(gameState, bulletList, gameAction)

        //奖励
        AwardSprite(gameState, awardList, gameAction)

        //爆炸道具
        BombAward(playerPlane, gameAction)

        //游戏得分
        GameScore(gameState, gameScore, gameAction)

        //游戏开始界面
        GameOver(gameState, gameScore, gameAction)

    }

}

@InternalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview()
@Composable
fun PreviewStage() {
    val gameViewModel: GameViewModel = viewModel()
    Stage(gameViewModel)
}
