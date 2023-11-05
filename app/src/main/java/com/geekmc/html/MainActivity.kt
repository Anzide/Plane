package com.geekmc.html

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geekmc.html.model.GameState
import com.geekmc.html.theme.ComposePlaneTheme
import com.geekmc.html.util.LogUtil
import com.geekmc.html.util.StatusBarUtil
import com.geekmc.html.view.Stage
import com.geekmc.html.viewmodel.GameViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @InternalCoroutinesApi
    private val gameViewModel: GameViewModel by viewModels()

    @InternalCoroutinesApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //启动页
        installSplashScreen()

        StatusBarUtil.transparentStatusBar(this)
        gameViewModel.onSoundPoolInit()

        lifecycle.addObserver(GameLifecycleObserver(gameViewModel))

        //观察游戏状态
        lifecycleScope.launch {
            gameViewModel.gameStateFlow.collect {
                LogUtil.printLog(message = "lifecycleScope gameState $it")
                //退出app
                if (GameState.Waiting == it) {
                    gameViewModel.onGameInit()
                }
                if (GameState.Exit == it) {
                    finish()
                }
            }
        }

        //绘制界面
        setContent {
            ComposePlaneTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Stage(gameViewModel)
                }
            }
        }
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