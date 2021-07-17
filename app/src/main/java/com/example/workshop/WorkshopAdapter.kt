package com.example.workshop

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class WorkshopAdapter(private var context: Context, private var workshops: List<Workshop>)
    : RecyclerView.Adapter<WorkshopViewHolder>() {

    lateinit var onItemClick: ((Workshop, CardView) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkshopViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WorkshopViewHolder(inflater, parent, context, onItemClick)
    }

    override fun onBindViewHolder(holder: WorkshopViewHolder, position: Int) {
        val workshop: Workshop = workshops[position]
        holder.bind(workshop)
    }

    override fun getItemCount(): Int = workshops.size

    fun updateData(workshops: List<Workshop>) {
        this.workshops = workshops
        notifyDataSetChanged()
    }
}