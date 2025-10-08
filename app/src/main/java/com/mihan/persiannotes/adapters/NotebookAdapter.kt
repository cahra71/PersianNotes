package com.mihan.persiannotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mihan.persiannotes.NotebookDisplay
import com.mihan.persiannotes.R

class NotebookAdapter(private val items: List<NotebookDisplay>, private val onClick: (NotebookDisplay) -> Unit)
    : RecyclerView.Adapter<NotebookAdapter.VH>() {

    inner class VH(view: View): RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notebook, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.tvTitle.text = it.title
        holder.tvDate.text = it.createdAt
        holder.itemView.setOnClickListener { onClick(it) }
    }

    override fun getItemCount(): Int = items.size
}
