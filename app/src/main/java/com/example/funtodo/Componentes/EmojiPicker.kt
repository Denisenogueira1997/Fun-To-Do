import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView

@Composable
fun EmojiPicker(onEmojiSelected: (String) -> Unit) {
    val context = LocalContext.current
    val themedContext = ContextThemeWrapper(context, android.R.style.Theme_Material_Light)

    Surface(
        color = Color.White,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp)
    ) {
        AndroidView(
            factory = {
                EmojiPickerView(themedContext).apply {
                    setOnEmojiPickedListener { emoji ->
                        onEmojiSelected(emoji.emoji)
                    }
                }
            }, modifier = Modifier.fillMaxSize()
        )
    }
}
