package com.studentofknowledge.mviarchitecture.ui.component

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.studentofknowledge.mviarchitecture.R
import com.studentofknowledge.mviarchitecture.ui.Option
import com.studentofknowledge.mviarchitecture.ui.theme.QuestionTheme

@Composable
fun SingleChoiceQuestion(
    @StringRes title: Int,
    @StringRes instruction: Int,
    possibleAnswers: List<Option>,
    userSelection: Option?,
    onOptionSelected: (Option) -> Unit,
    modifier: Modifier = Modifier
) {
    QuestionWrapper(title = title, instruction = instruction, modifier = modifier.selectableGroup()) {
        possibleAnswers.forEach { option ->
            val selected = option == userSelection
            RadioButtonRow(
                text = stringResource(id = option.text),
                img = option.image,
                selected = selected
            ) { onOptionSelected(option) }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SingleChoiceQuestionPreview() {
    QuestionTheme {
        Scaffold { padding ->
            SingleChoiceQuestion(
                title = R.string.pick_superhero,
                instruction = R.string.select_all,
                possibleAnswers = listOf(
                    Option(text = R.string.spark, image = R.drawable.spark),
                    Option(text = R.string.bugchaos, image = R.drawable.bug_of_chaos),
                ),
                onOptionSelected = {},
                userSelection = Option(text = R.string.frag, image = R.drawable.frag)
            )
            val x = padding
        }
    }
}