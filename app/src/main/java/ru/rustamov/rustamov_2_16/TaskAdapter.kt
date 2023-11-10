package ru.rustamov.rustamov_2_16

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class TaskAdapter(private val context: Context, private val tasks: ArrayList<Task>) : BaseAdapter() {
    override fun getCount(): Int {
        return tasks.size
    }

    override fun getItem(position: Int): Task {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val task = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = task.toString()
        return view
    }
}