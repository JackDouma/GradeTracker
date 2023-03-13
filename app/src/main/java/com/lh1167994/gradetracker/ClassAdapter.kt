package com.lh1167994.gradetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClassAdapter(
    val context: Context,
    val classes: List<Class>,
    val itemListener: ClassItemListener
): RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {
    /**
     * allow connection in the layout file
     */
    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val classTextView = itemView.findViewById<TextView>(R.id.classTextView)
    }


    /**
     * connects layout file
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder
    {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.item_class, parent, false)
        return ClassViewHolder(view)
    }

    /**
     * populating data into the textview objects in the layout file
     */
    override fun onBindViewHolder(viewHolder: ClassViewHolder, position: Int)
    {
        val selectedClass = classes[position]

        with(viewHolder) {
            classTextView.text = selectedClass.className

            itemView.setOnClickListener {
                itemListener.classSelected(selectedClass)
            }
        }
    }

    override fun getItemCount(): Int {
        return classes.size
    }

    /**
     * create custom interface
     */
    interface ClassItemListener
    {
        fun classSelected(selectedClass: Class)
    }
}
