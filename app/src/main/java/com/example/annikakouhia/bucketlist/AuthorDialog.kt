package com.example.annikakouhia.bucketlist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.util.Log
import kotlinx.android.synthetic.main.author_dialog.view.*

class AuthorDialog : DialogFragment() {

    interface ItemHandler {
        fun authorAdded(item: String)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException(getString(R.string.error))
        }
    }

    private lateinit var givenAuthorName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.addnewauthor))

        val inflatedView = requireActivity().layoutInflater.inflate(R.layout.author_dialog, null)
        givenAuthorName = inflatedView.etAuthor

        builder.setView(inflatedView)

        builder.setPositiveButton(getString(R.string.confirmagain)) { dialog, witch ->
        }
        return builder.create()
    }


    override fun onResume() {
        super.onResume()
        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (givenAuthorName.text.isNotEmpty()) {
                add()
                dialog.dismiss()
            } else {
                givenAuthorName.error = getString(R.string.noempty)
            }
        }
    }


    private fun add() {
        itemHandler.authorAdded(
                givenAuthorName.text.toString()
        )
    }
}
