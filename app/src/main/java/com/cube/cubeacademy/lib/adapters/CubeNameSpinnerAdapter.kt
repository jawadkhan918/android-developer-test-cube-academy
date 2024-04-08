package com.cube.cubeacademy.lib.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cube.cubeacademy.R
import com.cube.cubeacademy.lib.models.Nominee

class CubeNameSpinnerAdapter(
    context: Context,
    private val hintValue: String,
    private val options: List<Nominee>
) : ArrayAdapter<Nominee>(context, android.R.layout.simple_spinner_dropdown_item,options) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Check if this is the hint position
        val text = if (position == 0) hintValue else getItemFullName(getItem(position))
        return createItemView(R.layout.cube_name_spinner_item_selected, parent, text)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Check if this is the hint position
        return if (position == 0) {
            // Return an empty view for the hint position
            View(context)
        } else {
            // For other positions, use the custom dropdown view
            val text = getItemFullName(getItem(position))
            return createItemView(R.layout.cube_name_spinner_item_selected, parent, text)
        }
    }
    override fun isEnabled(position: Int): Boolean {
        // Disable selection for the hint position
        return position != 0
    }

    private fun createItemView(layoutResId: Int, parent: ViewGroup, text: String?): View {
        val view = LayoutInflater.from(context).inflate(layoutResId, parent, false)
        view.findViewById<TextView>(android.R.id.text1).text = text
        return view
    }
    private fun getItemFullName(nominee: Nominee?): String {
        return nominee?.let { "${it.firstName} ${it.lastName}" } ?: ""
    }
}