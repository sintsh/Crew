package com.example.crew.app.ui.dialogs

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.crew.R


class EmployeeActionDialogFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_employee_action_dialog, container, false)
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val width = (Resources.getSystem().displayMetrics.widthPixels * 0.80).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)
        }
    }

}