package cz.hombre.tacassistant.activity.reports

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import cz.hombre.tacassistant.R
import cz.hombre.tacassistant.dto.ReportData
import cz.hombre.tacassistant.services.REPORT_PROPERTY

import kotlinx.android.synthetic.main.activity_report_preview.*
import kotlinx.android.synthetic.main.content_report_preview.*

class ReportPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_preview)

        fabTop.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        fabBottom.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val report = intent?.getSerializableExtra(REPORT_PROPERTY) as ReportData

        title = report.name
        setReportPreview(report)
    }

    private fun setReportPreview(report: ReportData) {
        preview_content.setText(report.formattedReport())
    }

}