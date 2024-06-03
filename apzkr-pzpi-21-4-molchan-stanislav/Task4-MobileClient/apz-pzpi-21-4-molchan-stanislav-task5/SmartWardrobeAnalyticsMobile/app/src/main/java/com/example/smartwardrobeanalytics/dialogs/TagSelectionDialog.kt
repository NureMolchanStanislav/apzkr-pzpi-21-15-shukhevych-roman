package com.example.smartwardrobeanalytics.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.TagDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.TagServiceImpl

class TagSelectionDialog : DialogFragment() {

    private lateinit var tagSpinner: Spinner
    private val tagService = TagServiceImpl()
    private lateinit var onTagSelectedListener: (String) -> Unit

    fun setOnTagSelectedListener(listener: (String) -> Unit) {
        onTagSelectedListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_tag_selection, null)

        tagSpinner = view.findViewById(R.id.tag_spinner)
        loadTags()

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
            .setTitle("Select Tag")
            .setPositiveButton("Confirm") { _, _ ->
                val selectedTag = tagSpinner.selectedItem as String
                onTagSelectedListener(selectedTag)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }

    private fun loadTags() {
        tagService.getTags(object : ApiCallback<List<TagDto>> {
            override fun onSuccess(result: List<TagDto>) {
                val tagIds = result.map { it.tagId }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tagIds)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tagSpinner.adapter = adapter
            }

            override fun onError(error: String) {
                Log.e("TagSelectionDialog", "Failed to load tags: $error")
            }
        })
    }
}
