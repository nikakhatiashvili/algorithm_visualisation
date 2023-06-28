package com.example.algorithmvisualisation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.algorithmvisualisation.ui.theme.AlgorithmVisualisationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlgorithmVisualisationTheme {
                Home()
                // A surface container using the 'background' color from the theme
            }
        }
    }
}

@Composable
fun Home() {
    HomeScreen(viewModel = hiltViewModel())
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Screen(viewState, viewModel)
    }

}


@Composable
fun Screen(viewState: HomeContract.State, viewModel: HomeViewModel) {
    Column {
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Row {
            Box(
                modifier = Modifier
                    .padding(start = 10.dp, end = 60.dp)
                    .weight(1f)
            ) {
                AddItem(viewState) {
                    viewModel.onFilterChange(it)
                }
            }
            Button(
                modifier = Modifier,
                onClick = { }
            ) {
                Text(text = "add")
            }
        }
        Spacer(modifier = Modifier.padding(top = 40.dp))

        CustomBox()
    }
}

@Composable
fun CustomBox() {
    var scale by remember { mutableStateOf(1f) }
    var zoom by remember { mutableStateOf(1f) }
    Box(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 60.dp)
            .height(300.dp)
            .fillMaxWidth()
            .border(2.dp, Color.Black)

    ) {
        Box(modifier = Modifier
                .clipToBounds()
            .matchParentSize()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zooms, rotation ->
                    zoom = (zoom * zooms).coerceIn(1f, 4f)
                }
            }
            .graphicsLayer {
                scaleX = zoom
                scaleY = zoom
            }) {
            Canvas(modifier = Modifier.matchParentSize().clipToBounds()) {
                val lineSpacing = 5.dp.toPx()// Spacing between lines in pixels
                val strokeAlpha = 0.5f

                val lineCount = (size.width / lineSpacing).toInt()
                val horizontalLineCount = (size.height / lineSpacing).toInt()

                for (i in 0 until lineCount) {
                    val startX = i * lineSpacing
                    drawLine(
                        color = Color.Black.copy(strokeAlpha),
                        start = Offset(startX, 0f),
                        end = Offset(startX, size.height),
                        strokeWidth = 0.5f,
                    )
                }

                for (i in 0 until horizontalLineCount) {
                    val startY = i * lineSpacing
                    drawLine(
                        color = Color.Black.copy(strokeAlpha),
                        start = Offset(0f, startY),
                        end = Offset(size.width, startY),
                        strokeWidth = 0.5f,
                    )
                }
            }
        }
    }
}

@Composable
fun AddItem(
    state: HomeContract.State,
    onSearchChanged: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    MyTextField(
        modifier = Modifier
            .height(60.dp),
        labelResource = R.string.add_item,
        value = state.filter,
        leadingIcon = Icons.Default.Search,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        onValueChange = onSearchChanged
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlgorithmVisualisationTheme {
        Home()
    }
}


class HomeContract {

    data class State(
        val filter: String = "",
    ) {
        companion object {
            val Empty = State()
        }
    }

}

