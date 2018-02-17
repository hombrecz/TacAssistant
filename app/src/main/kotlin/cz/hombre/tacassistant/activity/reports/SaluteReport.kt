package cz.hombre.tacassistant.activity.reports

import android.content.Intent
import android.os.Bundle
import cz.hombre.tacassistant.R
import cz.hombre.tacassistant.dto.ReportData
import cz.hombre.tacassistant.dto.ReportLine
import cz.hombre.tacassistant.layout.report.SaluteReportUI
import cz.hombre.tacassistant.services.REPORT_PROPERTY
import org.jetbrains.anko.setContentView

class SaluteReport : AbstractReport() {

    private var saluteReportUI = SaluteReportUI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saluteReportUI.setContentView(this)
        saluteReportUI.previewButton.setOnClickListener {
            val report = getReportData()
            val previewIntent = Intent(this, ReportPreviewActivity::class.java)
            previewIntent.putExtra(REPORT_PROPERTY, report)
            startActivity(previewIntent)
        }
        saluteReportUI.time.setValue(dateTimeService.getZuluDateTimeGroup())
        saluteReportUI.location.setValue(locationService.getCurrentMGRSLocation())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun getReportData(): ReportData {
        val size = ReportLine(saluteReportUI.size.getValue())
        val activity = ReportLine(saluteReportUI.activity.getValue())
        val location = ReportLine(saluteReportUI.location.getValue())
        val uniform = ReportLine(saluteReportUI.uniform.getValue())
        val time = ReportLine(saluteReportUI.time.getValue())
        val equipment = ReportLine(saluteReportUI.enemy.getValue())

        return ReportData(getString(R.string.title_activity_salute_report), arrayOf(size, activity, location, uniform, time, equipment))
    }
}
