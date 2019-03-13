package com.example.annikakouhia.bucketlist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.util.Log
import kotlinx.android.synthetic.main.author_dialog.view.*
import kotlinx.android.synthetic.main.item_dialog.view.*

class AddItemInBucketDialog : DialogFragment() {

    interface ItemHandler {
        fun itemBucketAdded(item: String)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException(getString(R.string.exception))
        }
    }

    private lateinit var givenItemName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.addnewitem))
        val inflatedView = requireActivity().layoutInflater.inflate(R.layout.item_dialog, null)
        givenItemName = inflatedView.etItem
        builder.setView(inflatedView)
        builder.setPositiveButton(getString(R.string.confirm)) { dialog, witch -> }
        return builder.create()
    }


    override fun onResume() {
        super.onResume()
        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (givenItemName.text.isNotEmpty()) {
                add()
                dialog.dismiss()
            } else {
                givenItemName.error = getString(R.string.cantbeempty)
            }
        }
    }


    private fun add() {
        itemHandler.itemBucketAdded(
                givenItemName.text.toString()
        )
    }
}
