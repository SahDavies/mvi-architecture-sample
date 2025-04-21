package com.studentofknowledge.mviarchitecture.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.studentofknowledge.mviarchitecture.R
import com.studentofknowledge.mviarchitecture.ui.component.SingleChoiceQuestion
import com.studentofknowledge.mviarchitecture.ui.theme.QuestionTheme
import com.studentofknowledge.mviarchitecture.ui.theme.stronglyDeemphasizedAlpha

private const val CONTENT_ANIMATION_DURATION = 300

@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    onDone: () -> Unit
) {
    BackHandler { /* Do nothing */ }
    val viewModel = QuestionViewModel(getQuestions())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuestionScreenContent(
        uiState = uiState,
        onClickNext = viewModel::next,
        onClickPrevious = viewModel::previous,
        onOptionSelected = viewModel::onOptionSelected,
        onDone = onDone
    )
}

@Composable
private fun QuestionScreenContent(
    uiState: UiState,
    onClickNext: () -> Unit,
    onClickPrevious: () -> Unit,
    onOptionSelected: (Option) -> Unit,
    onDone: () -> Unit
) {
    Scaffold(
        bottomBar = {
            QuestionBottomBar(
                hasPrevious = uiState.hasPrevious,
                hasNext = uiState.hasNext,
                hasMadeSelection = { uiState.userSelection != null }, // is used to enable or disable next button only if user has made a selection
                onClickNext = onClickNext,
                onClickPrevious = onClickPrevious,
                onDone = onDone
            )
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        Column(modifier = Modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()) {
            QuestionTopBar(
                count = uiState.questionCount,
                totalQuestions = uiState.totalQuestions,
                onClose = onDone
            )
            Spacer(
                modifier = Modifier
                    .height(18.dp)
                    .fillMaxWidth()
            )

            AnimatedContent(
                targetState = uiState.question to uiState.questionCount,
                transitionSpec = {
                    val animationSpec: TweenSpec<IntOffset> = tween(CONTENT_ANIMATION_DURATION)
                    val direction = with(AnimatedContentTransitionScope.SlideDirection) {
                        if (targetState.second > initialState.second)
                            Left
                        else
                            Right
                    }

                    slideIntoContainer(
                        towards = direction,
                        animationSpec = animationSpec
                    ) togetherWith slideOutOfContainer(
                        towards = direction,
                        animationSpec = animationSpec
                    )
                }
            ) { state ->
                val (question, options) = state.first
                SingleChoiceQuestion(
                    title = question,
                    instruction = R.string.select_one,
                    possibleAnswers = options,
                    onOptionSelected = onOptionSelected,
                    userSelection = uiState.userSelection,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun QuestionBottomBar(
    hasPrevious: Boolean,
    hasNext: Boolean,
    hasMadeSelection: () -> Boolean,
    onClickNext: () -> Unit,
    onClickPrevious: () -> Unit,
    onDone: () -> Unit
) {
    Surface(
        color = QuestionTheme.colors.background,
        shadowElevation = 18.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val modifier = Modifier.requiredSize(width = 172.dp, height = 55.dp)
            OutlinedButton(
                onClick = onClickPrevious,
                modifier = modifier,
                enabled = hasPrevious
            ) {
                Text(text = stringResource(id = R.string.previous))
            }
            Spacer(Modifier.size(36.dp))
            Button(
                onClick = if (hasNext) onClickNext else onDone,
                modifier = modifier,
                enabled = hasMadeSelection()
            ) {
                Text(text = if (hasNext) stringResource(id = R.string.next) else stringResource(id = R.string.done))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun QuestionTopBar(count: Int, totalQuestions: Int, onClose: () -> Unit) {
    Column {
        CenterAlignedTopAppBar(
            actions = {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close),
                    )
                }
            },
            title = {
                Text(
                    text = "$count of $totalQuestions",
                    style = QuestionTheme.typography.labelMedium,
                    color = QuestionTheme.colors.onSurface.copy(alpha = stronglyDeemphasizedAlpha)
                )
            }
        )
        val animatedProgress by animateFloatAsState(
            targetValue = count / totalQuestions.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(progress = animatedProgress, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun getQuestions() : List<Question> {
    return listOf(
        R.string.pick_superhero to listOf(
            Option(R.string.spark, R.drawable.spark),
            Option(R.string.bugchaos, R.drawable.bug_of_chaos),
            Option(R.string.lenz, R.drawable.lenz),
            Option(R.string.frag, R.drawable.frag),
        ),
        R.string.in_my_free_time to listOf(
            Option(R.string.read, null),
            Option(R.string.work_out, null),
            Option(R.string.play_games, null),
            Option(R.string.dance, null),
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SurveyScreenPreview() {
    QuestionTheme {
        Scaffold {
            val padding = it
            QuestionScreen {  }
        }
    }
}