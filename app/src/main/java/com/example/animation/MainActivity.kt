package com.example.animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.animation.ui.theme.AnimationTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    /*
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                     */
                    Animation(m = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Animation(m: Modifier) {
    // State variables to control visibility, size, and animation state
    var appear by remember { mutableStateOf(true) }  // Background appear/disappear
    var expanded by remember { mutableStateOf(true) }  // Image size toggle (expanded or collapsed)
    var fly by remember { mutableStateOf(false) }  // Rocket flying state (unused here, but could be useful)
    val buttonAngle by animateFloatAsState(
        if (appear) 360f else 0f,
        animationSpec = tween(durationMillis = 2500)
    )
    val backgroundColor by animateColorAsState(
        if (appear) Color.Transparent else Color.Green,
        animationSpec = tween(2000, 500)
    )
    val rocketSize by animateDpAsState(
        if (fly) 75.dp else 150.dp,
        animationSpec = tween(2000)
    )
    val rocketOffset by animateOffsetAsState(
        if (fly) Offset(200f, -50f) else Offset(200f, 400f),
        animationSpec = tween(2000)
    )



    Column(Modifier.background(backgroundColor)) {

        Column {
            Button(
                onClick = { appear = !appear },
                modifier = Modifier
            ) {
                if (appear) Text(text = "星空背景圖消失")
                else Text(text = "星空背景圖出現")
            }


            // AnimatedVisibility for controlling the fade and slide of the image
            AnimatedVisibility(
                visible = appear,
                enter = fadeIn(
                    initialAlpha = 0.1f,
                    animationSpec = tween(durationMillis = 5000)
                ) + slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth / 3 }, // Slide from left
                    animationSpec = tween(durationMillis = 5000)
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 5000)) + slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 }, // Slide out to the left
                    animationSpec = tween(durationMillis = 5000)
                )
            ) {
                // Image with animation for size change on click
                Image(
                    painter = painterResource(id = R.drawable.sky),
                    contentDescription = "星空背景圖",
                    modifier = Modifier.rotate(buttonAngle)
                        .fillMaxWidth()
                        .height(if (expanded) 600.dp else 400.dp)  // Dynamic height based on expanded state
                        .clickable { expanded = !expanded }  // Toggle image size when clicked
                        .animateContentSize()  // Smooth size transition
                )
                Image(
                    painter = painterResource(id = R.drawable.rocket),
                    contentDescription = "火箭",
                    modifier = Modifier
                        .size(rocketSize)
                        .offset(rocketOffset.x.dp, rocketOffset.y.dp)
                        .clickable(
                        ) {
                            fly = !fly
                        }
                )

            }
        }
    }
}

