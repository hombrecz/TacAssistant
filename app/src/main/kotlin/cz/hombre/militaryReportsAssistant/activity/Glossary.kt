package cz.hombre.militaryReportsAssistant.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import cz.hombre.militaryReportsAssistant.R
import cz.hombre.militaryReportsAssistant.database.model.GlossaryEntry
import cz.hombre.militaryReportsAssistant.layout.GlossaryUI
import cz.hombre.militaryReportsAssistant.layout.adapter.GlossaryAdapter
import cz.hombre.militaryReportsAssistant.services.DatabaseService
import cz.hombre.militaryReportsAssistant.services.PreferencesService
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.customView
import org.jetbrains.anko.dip
import org.jetbrains.anko.editText
import org.jetbrains.anko.hintResource
import org.jetbrains.anko.longToast
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.selector
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent
import org.koin.android.ext.android.inject

class Glossary : AppCompatActivity() {

    private val databaseService: DatabaseService by inject()
    private val preferencesService: PreferencesService by inject()
    private val glossary = ArrayList<GlossaryEntry>()

    private lateinit var glossaryUI: GlossaryUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preferencesService.isGlossaryInitialised() == 0) {
            databaseService.addDefaultGlossaryEntries()
            preferencesService.setGlossaryInitialised(true)
        }

        glossary.addAll(databaseService.getAllGlossaryEntries())

        val adapter = GlossaryAdapter(glossary)

        glossaryUI = GlossaryUI(adapter)
        glossaryUI.setContentView(this)
        setTitle(R.string.title_activity_glossary)

        glossaryUI.addButton.setOnClickListener {
            addEntry(adapter)
        }
        glossaryUI.glossaryList.setOnItemLongClickListener { _, _, i, _ ->
            val options = listOf(getString(R.string.glossary_entry_options_delete), getString(R.string.glossary_entry_options_delete_all), getString(R.string.glossary_entry_options_add_all_default))
            selector(getString(R.string.glossary_entry_options), options) { _, position ->
                when (position) {
                    0 -> {
                        databaseService.deleteGlossaryEntry(adapter.getItem(i))
                        adapter.delete(i)
                        toggleHintView()
                        longToast(getString(R.string.glossary_entry_deleted))
                    }
                    1 -> {
                        databaseService.getAllGlossaryEntries().forEach(databaseService::deleteGlossaryEntry)
                        adapter.deleteAll()
                        toggleHintView()
                        longToast(getString(R.string.glossary_entry_deleted_all))
                    }
                    2 -> {
                        val addedEntries = databaseService.addDefaultGlossaryEntries()
                        adapter.addAll(addedEntries)
                        longToast(getString(R.string.glossary_entry_added_all_default))
                    }
                }
            }
            true
        }

        toggleHintView()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addEntry(adapter: GlossaryAdapter) {
        alert {
            customView {
                verticalLayout {
                    toolbar {
                        id = R.id.add_dialog_toolbar
                        lparams(width = matchParent, height = wrapContent)
                        backgroundColor = ContextCompat.getColor(ctx, R.color.secondaryDarkColor)
                        title = getString(R.string.glossary_add_entry_dialog_title)
                        setTitleTextColor(ContextCompat.getColor(ctx, R.color.primaryLightColor))
                    }
                    val name = editText {
                        hintResource = R.string.glossary_add_entry_dialog_name
                        padding = dip(20)
                    }
                    val entry = editText {
                        hintResource = R.string.glossary_add_entry_dialog_value
                        padding = dip(20)
                    }
                    positiveButton(R.string.glossary_add_entry_dialog_add_button) {
                        when {
                            name.text.toString().isEmpty() -> toast(R.string.glossary_add_entry_dialog_name_missing)
                            entry.text.toString().isEmpty() -> toast(R.string.glossary_add_entry_dialog_value_missing)
                            else -> {
                                val newGlossaryEntry = GlossaryEntry(adapter.count, name.text.toString(), entry.text.toString())
                                databaseService.addGlossaryEntry(newGlossaryEntry)
                                adapter.add(newGlossaryEntry)
                                toggleHintView()
                            }
                        }
                    }
                }
            }
        }.show()
    }

    private fun toggleHintView() {
        if (glossaryUI.glossaryList.adapter.count > 0) {
            glossaryUI.hintView.visibility = View.GONE
        } else {
            glossaryUI.hintView.visibility = View.VISIBLE
        }
    }
}