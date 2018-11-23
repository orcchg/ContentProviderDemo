package com.orcchg.contentproviderdemo.hostapp.presentation

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orcchg.contentproviderdemo.hostapp.R
import com.orcchg.contentproviderdemo.hostapp.domain.Model
import kotlinx.android.synthetic.main.rv_item_layout.view.*

class ModelAdapter : ListAdapter<Model, ModelViewHolder>(DiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = getItem(position).id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_layout, parent, false)
        return ModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(model: Model) {
        itemView.apply {
            tv_name.text = model.name
            tv_description.text = model.description
            tv_temperature.text = "${model.temperature} C"
            tv_season.text = model.season
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Model>() {

    override fun areItemsTheSame(p0: Model, p1: Model): Boolean = p0.id == p1.id
    override fun areContentsTheSame(p0: Model, p1: Model): Boolean = p0 == p1
}
