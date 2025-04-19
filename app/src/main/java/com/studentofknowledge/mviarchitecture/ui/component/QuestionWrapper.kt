package com.studentofknowledge.mviarchitecture.ui.component

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studentofknowledge.mviarchitecture.R
import com.studentofknowledge.mviarchitecture.ui.theme.slightlyDeemphasizedAlpha
import com.studentofknowledge.mviarchitecture.ui.theme.stronglyDeemphasizedAlpha
import com.studentofknowledge.mviarchitecture.ui.theme.QuestionTheme
import com.studentofknowledge.mviarchitecture.ui.util.supportWideScreen

@Composable
fun QuestionWrapper(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    @StringRes instruction: Int,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Text(
            text = stringResource(id = title),
            style = QuestionTheme.typography.titleMedium,
            color = QuestionTheme.colors.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = QuestionTheme.colors.inverseOnSurface,
                    shape = QuestionTheme.shapes.small
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
        Text(
            text = stringResource(id = instruction),
            color = QuestionTheme.colors.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            style = QuestionTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        content()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuestionWrapperPreview() {
    QuestionTheme {
        Scaffold(modifier = Modifier.supportWideScreen()) {
            QuestionWrapper(Modifier.padding(it), R.string.in_my_free_time, R.string.select_all) {}
        }
    }
}