package com.studentofknowledge.mviarchitecture.ui.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studentofknowledge.mviarchitecture.R
import com.studentofknowledge.mviarchitecture.ui.theme.QuestionTheme
import com.studentofknowledge.mviarchitecture.ui.util.supportWideScreen

@Composable
fun CheckBoxRow(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes img: Int?,
    selected: Boolean,
    onOptionSelected: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (selected) {
            QuestionTheme.colors.primary
        } else QuestionTheme.colors.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                QuestionTheme.colors.primaryContainer
            } else QuestionTheme.colors.outline
        ),
        modifier = modifier.clickable(onClick = onOptionSelected)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(16.dp)
        ) {
            if(img != null) {
                Image(
                    painter = painterResource(img),
                    contentDescription = stringResource(R.string.check_box_image),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(QuestionTheme.shapes.extraSmall)
                )
            }
            Text(text = text, modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(8.dp)) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = QuestionTheme.colors.primaryContainer,
                        checkmarkColor = QuestionTheme.colors.primary
                    )
                )
            }
        }
    }
}

@Composable
fun RadioButtonRow(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes img: Int?,
    selected: Boolean,
    onOptionSelected: () -> Unit
) {
    Surface(
        shape = QuestionTheme.shapes.small,
        color = if (selected) {
            QuestionTheme.colors.primary
        } else QuestionTheme.colors.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                QuestionTheme.colors.primaryContainer
            } else QuestionTheme.colors.outline
        ),
        modifier = modifier.clickable(onClick = onOptionSelected)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(16.dp)
        ) {
            if(img != null) {
                Image(
                    painter = painterResource(img),
                    contentDescription = stringResource(R.string.check_box_image),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(QuestionTheme.shapes.extraSmall)
                )
            }
            Text(text = text, modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(8.dp)) {
                RadioButton(
                    selected = selected,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = QuestionTheme.colors.primaryContainer,
                    )
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CheckBoxRowPreview() {
    QuestionTheme {
        Scaffold(modifier = Modifier.supportWideScreen()) {
            Column {
                CheckBoxRow(Modifier.padding(it), stringResource(id = R.string.spark), R.drawable.spark, true) {}
                RadioButtonRow(Modifier.padding(it), stringResource(id = R.string.spark),null, false) {}
            }
        }
    }
}