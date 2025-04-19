package com.studentofknowledge.mviarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.studentofknowledge.mviarchitecture.ui.QuestionScreen
import com.studentofknowledge.mviarchitecture.ui.theme.QuestionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuestionTheme {
                QuestionScreen(onDone = ::finish)
            }
        }
    }
}