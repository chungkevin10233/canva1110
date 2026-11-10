package tw.edu.pu.csim.CHUNG.digitalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import tw.edu.pu.csim.CHUNG.digitalink.ui.theme.DigitalInkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigitalInkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestCanvas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TestCanvas(modifier: Modifier = Modifier) {
    val minionsImage: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.minions)
    val textMeasurer = rememberTextMeasurer()
    var path by remember { mutableStateOf(Path()) }
    var running by remember { mutableStateOf(true) }
    var x by remember { mutableFloatStateOf(0f) }
    var y by remember { mutableFloatStateOf(0f) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(running) {
        while (running) {
            withFrameNanos {
                x += 4f
                y += 6f

                if (x > canvasSize.width || y > canvasSize.height) {
                    x = 0f
                    y = 0f
                }
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { canvasSize = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        if (offset.x in 300f..500f && offset.y in 200f..400f) {
                            running = !running
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        path.reset()
                        path.moveTo(offset.x, offset.y)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        path.lineTo(change.position.x, change.position.y)

                        val tempPath = Path()
                        tempPath.addPath(path)
                        path = tempPath
                    },
                    onDragEnd = { }
                )
            }
    ) {
        drawCircle(Color.Magenta, 100f, Offset(100f, 100f))

        drawRect(
            Color.Blue,
            Offset(300f, 200f),
            Size(200f, 200f),
            style = Stroke(width = 5f)
        )

        drawLine(
            Color.Red,
            Offset(200f, 800f),
            Offset(size.width, size.height),
            strokeWidth = 10f
        )

        translate(x, y) {
            drawImage(minionsImage)
        }

        drawPath(path, Color.Green, 1f, Stroke(width = 10f))

        val label = if (running) "停止" else "開始"
        val layout = textMeasurer.measure(label, TextStyle(fontSize = 28.sp, color = Color.Blue))
        drawText(
            layout,
            topLeft = Offset(
                300f + (200f - layout.size.width) / 2f,
                200f + (200f - layout.size.height) / 2f
            )
        )
    }
}
