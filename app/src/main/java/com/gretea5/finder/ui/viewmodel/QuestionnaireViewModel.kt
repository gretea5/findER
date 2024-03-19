package com.gretea5.finder.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.gretea5.finder.ui.model.QuestionnaireInfo

class QuestionnaireViewModel : ViewModel() {
    var questionnaireInfo = QuestionnaireInfo()

    fun resetQuestionnaireInfo() {
        questionnaireInfo = QuestionnaireInfo()
    }
}