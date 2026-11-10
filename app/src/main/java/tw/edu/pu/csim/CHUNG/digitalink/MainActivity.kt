package tw.edu.pu.csim.CHUNG.digitalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.imageResource
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

// v1：Canvas 進階 —— 加上直線、圖片、路徑
@Composable
fun TestCanvas(modifier: Modifier = Modifier) {
    // 載入圖片資源（小小兵）
    val minionsImage: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.minions)

    // 建立一個新的路徑物件（三角形）
    val path = Path().apply {
        moveTo(50f, 100f)   // 移動到起始點
        lineTo(200f, 500f)  // 畫線到 (200,500)
        lineTo(450f, 400f)  // 畫線到 (450,400)
        close()             // 閉合路徑
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        // 洋紅色實心圓，半徑 100，圓心 (100,100)
        drawCircle(Color.Magenta, 100f, Offset(100f, 100f))

        // 藍色空心矩形，左上角 (300,200)，寬高 200，外框粗 5
        drawRect(
            Color.Blue,
            Offset(300f, 200f),
            Size(200f, 200f),
            style = Stroke(width = 5f)
        )

        // 紅色直線，從 (200,800) 畫到畫布右下角，粗 10
        drawLine(
            Color.Red,
            Offset(200f, 800f),
            Offset(size.width, size.height),
            strokeWidth = 10f
        )

        // 圖片置中：(畫布寬高 - 圖片寬高) / 2f，先把原點平移過去再畫
        val offsetX = (size.width - minionsImage.width) / 2f
        val offsetY = (size.height - minionsImage.height) / 2f
        translate(offsetX, offsetY) {
            drawImage(minionsImage)
        }

        // 綠色路徑，不透明，線條粗 10
        drawPath(path, Color.Green, 1f, Stroke(width = 10f))
    }
}
