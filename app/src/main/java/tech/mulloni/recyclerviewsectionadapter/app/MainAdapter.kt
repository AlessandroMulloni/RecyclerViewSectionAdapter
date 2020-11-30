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
    val VIEW_TYPE_CELL = 1

    override fun getSectionCount(): Int {
        return 5
    }

    override fun hasHeader(section: Int): Boolean {
        when (section) {
            0, 2 -> return true
            else -> return false
        }
    }

    override fun hasFooter(section: Int): Boolean {
        when (section) {
            1, 2, 4 -> return true
            else -> return false
        }
    }

    override fun getItemCount(section: Int): Int {
        when (section) {
            0 -> return 9
            1 -> return 7
            2 -> return 6
            3 -> return 8
            4 -> return 3
        }

        return 0
    }

    override fun getItemViewType(section: Int, position: Int): Int {
        return VIEW_TYPE_CELL
    }

    override fun onCreateViewHolderHeader(parent: ViewGroup): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_header, parent, false)
        return MainViewHolder(itemView)
    }

    override fun onCreateViewHolderFooter(parent: ViewGroup): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_footer, parent, false)
        return MainViewHolder(itemView)
    }

    override fun onCreateViewHolderView(parent: ViewGroup, viewType: Int): MainViewHolder {
        when (viewType) {
            VIEW_TYPE_CELL -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_cell, parent, false)
                return MainViewHolderCell(itemView)
            }
        }

        throw Exception()
    }

    override fun onBindViewHolder(holder: MainViewHolder, section: Int, position: Int) {
        if (holder is MainViewHolderCell) {
            holder.itemView.textView.text = "${section} - ${position}"
        }
    }
}