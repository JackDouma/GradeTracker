package com.lh1167994.gradetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GradeAdapter (
    val context: Context,
    val grades: List<Grade>,
    val itemListener: GradeItemListener): RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {
    /**
     * allow connection in the layout file
     */
    inner class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val gradeNameTextView = itemView.findViewById<TextView>(R.id.gradeNameTextView)
    }


    /**
     * connects layout file
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder
    {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.item_grade, parent, false)
        return GradeViewHolder(view)
    }

    /**
     * populating data into the textview objects in the layout file
     */
    override fun onBindViewHolder(viewHolder: GradeViewHolder, position: Int)
    {
        val selectedGrade = grades[position]

        with(viewHolder) {
            gradeNameTextView.text = selectedGrade.gradeName

            itemView.setOnClickListener {
                itemListener.gradeSelected(selectedGrade)
            }
        }
    }

    override fun getItemCount(): Int {
        return grades.size
    }

    /**
     * create custom interface
     */
    interface GradeItemListener
    {
        fun gradeSelected(selectedGrade: Grade)
    }
}