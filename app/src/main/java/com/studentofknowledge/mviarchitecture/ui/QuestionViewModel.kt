package com.studentofknowledge.mviarchitecture.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class QuestionViewModel(private val questions: List<Question>) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            totalQuestions = questions.size,
            questionCount = 1,
            question = questions[0],
            userSelection = null
        )
    )

    private val currentState: UiState
        get() = _uiState.value

    private val hasNext: Boolean
        get() = currentState.questionCount < questions.size

    private val hasPrevious: Boolean
        get() = currentState.questionCount > 0

    private val questionIndex: Int
        get() = currentState.questionCount - 1

    private val userSelections: MutableList<Option?> = MutableList(questions.size) { null }

    val uiState: StateFlow<UiState> = _uiState

    fun next() {
        /*
        * When next is pressed, we update the following properties only:
        *  - questionCount
        *  - question
        *  - hasNext
        *  - hasPrevious
        * */
        if (hasNext.not()) return
        _uiState.update {
            it.copy(
                questionCount = it.questionCount + 1,
                question = questions[questionIndex + 1],
                userSelection = userSelections[questionIndex + 1]
            )
        }
    }
    fun previous() {
        /*
        * When previous is pressed, we update the following properties only:
        *  - questionCount
        *  - question
        *  - hasNext
        *  - hasPrevious
        * */
        if (hasPrevious.not()) return
        _uiState.update {
            it.copy(
                questionCount = it.questionCount - 1,
                question = questions[questionIndex - 1],
                userSelection = userSelections[questionIndex - 1]
            )
        }
    }

    fun onOptionSelected(selection: Option) {
        userSelections[questionIndex] = selection
        _uiState.update {
            it.copy(userSelection = selection)
        }
    }
}

data class UiState(
    val questionCount: Int,
    val totalQuestions: Int,
    val question: Question,
    val userSelection: Option?
) {
    val hasNext: Boolean = questionCount < totalQuestions
    val hasPrevious: Boolean = questionCount > 1
}

data class Option(
    @StringRes val text: Int,
    @DrawableRes val image: Int?
)

typealias Options = List<Option>
typealias Question = Pair<Int, Options>