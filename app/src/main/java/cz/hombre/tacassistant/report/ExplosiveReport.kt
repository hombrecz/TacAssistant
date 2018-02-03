package cz.hombre.tacassistant.report

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import cz.hombre.tacassistant.R
import cz.hombre.tacassistant.ReportPreviewActivity
import cz.hombre.tacassistant.dto.ReportData
import cz.hombre.tacassistant.dto.ReportLine
import kotlinx.android.synthetic.main.activity_explosive_report.*
import kotlinx.android.synthetic.main.content_explosive_report.*

class ExplosiveReport : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explosive_report)

        fab.setOnClickListener {
            val report = getReportData()
            val previewIntent = Intent(this, ReportPreviewActivity::class.java)
            previewIntent.putExtra("report", report)
            startActivity(previewIntent)
        }
        setDefaultPreferencesValues()
        setLineFiveCheckboxFunction()
        setHidingContent()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setDefaultPreferencesValues() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val frequency = prefs.getString("preference_frequency", "")
        explosive_value_line3_frequency.setText(frequency)

        val callSign = prefs.getString("preference_callsign", "")
        explosive_value_line2_callsign.setText(callSign)
        explosive_value_line3_callsign.setText(callSign)
    }


    private fun setLineFiveCheckboxFunction() {
        explosive_value_line5_observed.setOnClickListener {
            if (explosive_value_line5_observed.isChecked) {
                explosive_value_line5_details.visibility = View.VISIBLE
            } else {
                explosive_value_line5_details.visibility = View.GONE
            }
        }
    }

    private fun setHidingContent() {
        switchVisibility(explosive_label_line1, explosive_content_line1)
        switchVisibility(explosive_label_line2, explosive_content_line2)
        switchVisibility(explosive_label_line3, explosive_content_line3)
        switchVisibility(explosive_label_line4, explosive_content_line4)
        switchVisibility(explosive_label_line5, explosive_content_line5)
        switchVisibility(explosive_label_line6, explosive_content_line6)
        switchVisibility(explosive_label_line7, explosive_content_line7)
        switchVisibility(explosive_label_line8, explosive_content_line8)
        switchVisibility(explosive_label_line9, explosive_content_line9)
    }

    private fun switchVisibility(initiator: View, target: View) {
        initiator.setOnClickListener {
            if (target.visibility == View.VISIBLE) {
                target.visibility = View.GONE
            } else {
                target.visibility = View.VISIBLE
            }
        }
    }

    private fun getReportData(): ReportData {

        val line1 = ReportLine("Line 1", explosive_value_line1.text.toString())
        val line2 = ReportLine("Line 2", getSecondLine())
        val line3 = ReportLine("Line 3", getThirdLine())
        val line4 = ReportLine("Line 4", getFourthLine())
        val line5 = ReportLine("Line 5", getFifthLine())
        val line6 = ReportLine("Line 6", explosive_value_line6.text.toString())
        val line7 = ReportLine("Line 7", explosive_value_line7.text.toString())
        val line8 = ReportLine("Line 8", explosive_value_line8.text.toString())
        val line9 = ReportLine("Line 9", getNinthLineValue())

        return ReportData("UXO/IED 9-liner", arrayOf(line1, line2, line3, line4, line5, line6, line7, line8, line9))
    }

    private fun getSecondLine(): String {
        val sb = StringBuilder()
        sb.append(explosive_value_line2_unit.text.toString())
        if (explosive_value_line2_unit.text.toString().isNotBlank()) {
            sb.append(", ")
        }
        sb.appendln(explosive_value_line2_callsign.text.toString())
        sb.appendln(explosive_content_line2_location.text.toString())

        return sb.toString()
    }

    private fun getThirdLine(): String {
        val sb = StringBuilder()
        sb.append(explosive_value_line3_frequency.text.toString())
        if (explosive_value_line3_frequency.text.toString().isNotBlank()) {
            sb.append(", ")
        }
        sb.append(explosive_value_line3_callsign.text.toString())

        return sb.toString()
    }

    private fun getFourthLine(): String {
        val sb = StringBuilder()
        sb.appendln(getRadioButtonValue(explosive_radio_group_line4.checkedRadioButtonId))
        sb.append(explosive_value_line4_details.text.toString())

        return sb.toString()
    }

    private fun getFifthLine(): String {
        var result = "Negative"
        if (explosive_value_line5_observed.isChecked) {
            result = explosive_value_line5_details.text.toString()
        }

        return result
    }

    private fun getNinthLineValue(): String {
        val radioButton = findViewById<RadioButton>(explosive_content_line9.checkedRadioButtonId)
        return radioButton.text.toString()
    }

    private fun getRadioButtonValue(radioButtonGroupId: Int): String {
        val radioButton = findViewById<RadioButton>(radioButtonGroupId)
        return radioButton.text.toString()
    }

}

