package tech.mulloni.recyclerviewsectionadapter.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_cell.view.*

import tech.mulloni.RecyclerViewSectionAdapterApp.R
import tech.mulloni.recyclerviewsectionadapter.RecyclerViewSectionAdapter

import java.lang.Exception

open class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) { }

class MainViewHolderCell(itemView: View): MainViewHolder(itemView) { }

class MainAdapter: RecyclerViewSectionAdapter<MainViewHolder>() {
    val VIEW_TYPE_HEADER = 0
    val VIEW_TYPE_CELL = 1

    override fun getSectionCount(): Int {
        return 4
    }

    override fun getItemCount(section: Int): Int {
        when (section) {
            0 -> return 9
            1 -> return 7
            2 -> return 6
            3 -> return 8
        }

        return 0
    }

    override fun getItemViewType(section: Int, position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER
        } else {
            return VIEW_TYPE_CELL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        when (viewType) {
            VIEW_TYPE_HEADER -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_header, parent, false)
                return MainViewHolder(itemView)
            }
            VIEW_TYPE_CELL -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_cell, parent, false)
                return MainViewHolderCell(itemView)
            }
        }

        throw Exception()
    }

    override fun onBindViewHolder(holder: MainViewHolder, section: Int, position: Int) {
        if (holder is MainViewHolderCell) {
            holder.itemView.textView.text = "${position}"
        }
    }
}