/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fhir.datacapture.views

import androidx.recyclerview.widget.RecyclerView
import com.google.android.fhir.datacapture.displayString
import org.hl7.fhir.r4.model.Attachment
import org.hl7.fhir.r4.model.BooleanType
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.DateType
import org.hl7.fhir.r4.model.DecimalType
import org.hl7.fhir.r4.model.IntegerType
import org.hl7.fhir.r4.model.Quantity
import org.hl7.fhir.r4.model.Questionnaire
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.model.StringType
import org.hl7.fhir.r4.model.TimeType
import org.hl7.fhir.r4.model.UriType

/**
 * Item for [QuestionnaireResponseItemViewHolder] in [RecyclerView] containing
 * @param questionnaireItem [Questionnaire.QuestionnaireItemComponent](a particular question that is
 * part of the questionnaire)
 *
 * @param questionnaireResponseItem [QuestionnaireResponse.QuestionnaireResponseItemComponent]
 * (answer item from the original questionnaire for which answers are provided)
 */
data class QuestionnaireResponseItemViewItem(
  val questionnaireItem: Questionnaire.QuestionnaireItemComponent,
  val questionnaireResponseItem: QuestionnaireResponse.QuestionnaireResponseItemComponent,
) {

  internal val answerString: String
    get() {
      if (!questionnaireResponseItem.hasAnswer()) return NOT_ANSWERED
      val answerList = mutableListOf<String>()
      questionnaireResponseItem.answer.forEach {
        answerList.add(
          when (it?.value) {
            is BooleanType ->
              when (it.valueBooleanType?.value) {
                true -> "Yes"
                false -> "No"
                null -> NOT_ANSWERED
              }
            is StringType -> it.valueStringType?.valueAsString ?: NOT_ANSWERED
            is IntegerType -> it.valueIntegerType?.valueAsString ?: NOT_ANSWERED
            is DecimalType -> it.valueDecimalType?.valueAsString ?: NOT_ANSWERED
            is DateType -> it.valueDateType?.valueAsString ?: NOT_ANSWERED
            is DateTimeType -> it.valueDateTimeType?.valueAsString ?: NOT_ANSWERED
            is TimeType -> it.valueTimeType?.valueAsString ?: NOT_ANSWERED
            is Quantity -> it.valueQuantity?.value?.toString() ?: NOT_ANSWERED
            is UriType -> it.valueUriType?.valueAsString ?: NOT_ANSWERED
            is Attachment -> it.valueAttachment?.url ?: NOT_ANSWERED
            is Coding -> it.displayString ?: NOT_ANSWERED
            else -> NOT_ANSWERED
          }
        )
      }
      return answerList.joinToString()
    }

  companion object {
    const val NOT_ANSWERED = "Not Answered"
  }
}