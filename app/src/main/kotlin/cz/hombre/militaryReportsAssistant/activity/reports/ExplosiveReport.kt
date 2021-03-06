package cz.hombre.militaryReportsAssistant.activity.reports

import android.content.Intent
import android.os.Bundle
import cz.hombre.militaryReportsAssistant.R
import cz.hombre.militaryReportsAssistant.dto.ReportData
import cz.hombre.militaryReportsAssistant.dto.ReportLine
import cz.hombre.militaryReportsAssistant.layout.reports.ExplosiveReportUI
import org.jetbrains.anko.setContentView

class ExplosiveReport : AbstractReport() {

    private var explosiveReportUI = ExplosiveReportUI(dateTimeService, locationService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        explosiveReportUI.setContentView(this)
        setTitle(R.string.title_activity_explosive_report)

        explosiveReportUI.previewButton.setOnClickListener {
            translateReport()
        }
        explosiveReportUI.line3.setFirstValue(preferencesService.getFrequency())
        explosiveReportUI.line3.setSecondValue(preferencesService.getCallSign())
        explosiveReportUI.line2.setCallSign(preferencesService.getCallSign())
        explosiveReportUI.line1.setValue(dateTimeService.getMilitaryDateTimeGroup())
        explosiveReportUI.line2.setLocation(locationService.getCurrentMGRSLocation())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            explosiveReportUI.line2.onLocationSelected(requestCode, resultCode, data)
        }
    }

    override fun getReportData(): ReportData {
        val line1 = ReportLine(getString(R.string.report_line) + " 1", explosiveReportUI.line1.getValue())
        val line2 = ReportLine(getString(R.string.report_line) + " 2", explosiveReportUI.line2.getValue())
        val line3 = ReportLine(getString(R.string.report_line) + " 3", explosiveReportUI.line3.getValue())
        val line4 = ReportLine(getString(R.string.report_line) + " 4", explosiveReportUI.line4.getValue())
        val line5 = ReportLine(getString(R.string.report_line) + " 5", explosiveReportUI.line5.getValue())
        val line6 = ReportLine(getString(R.string.report_line) + " 6", explosiveReportUI.line6.getValue())
        val line7 = ReportLine(getString(R.string.report_line) + " 7", explosiveReportUI.line7.getValue())
        val line8 = ReportLine(getString(R.string.report_line) + " 8", explosiveReportUI.line8.getValue())
        val line9 = ReportLine(getString(R.string.report_line) + " 9", explosiveReportUI.line9.getValue())

        return ReportData(getString(R.string.title_activity_explosive_report), arrayOf(line1, line2, line3, line4, line5, line6, line7, line8, line9))
    }
}