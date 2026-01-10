package pl.com.app.exchange.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pl.com.app.exchange.R

@Composable
fun ErrorDialog(positiveAction: () -> Unit) {
	val shape = RoundedCornerShape(8.dp)

	Dialog(
		onDismissRequest = {},
		properties = DialogProperties(
			dismissOnBackPress = false,
			dismissOnClickOutside = false
		)
	) {
		Column(
			modifier = Modifier
				.clip(shape)
				.border(width = 1.dp, color = Color.Black, shape = shape)
				.padding(vertical = 4.dp, horizontal = 8.dp)
		) {
			Text(
				text = stringResource(R.string.dialog_error_title),
				fontSize = 22.sp,
				color = Color.Black
			)

			Spacer(modifier = Modifier.height(8.dp))

			Text(
				text = stringResource(R.string.dialog_error_message),
				fontSize = 18.sp,
				color = Color.Black
			)

			Spacer(modifier = Modifier.height(16.dp))

			OutlinedButton(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp),
				onClick = positiveAction
			) {
				Text(
					text = stringResource(R.string.dialog_error_ok_button),
					fontSize = 18.sp,
					color = Color.Black
				)
			}
		}
	}
}

@Composable
@Preview
private fun Preview() {
	ErrorDialog(positiveAction = {})
}