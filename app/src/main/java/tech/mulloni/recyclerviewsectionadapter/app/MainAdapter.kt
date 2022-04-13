package tech.mulloni.recyclerviewsectionadapter.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import tech.mulloni.recyclerviewsectionadapter.RecyclerViewSectionAdapter
import tech.mulloni.recyclerviewsectionadapter.app.databinding.ViewCellBinding

import java.lang.Exception

open class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) { }

class MainViewHolderCell(itemView: View): MainViewHolder(itemView) {
    val binding = ViewCellBinding.bind(itemView)
}

class MainAdapter: RecyclerViewSectionAdapter<MainViewHolder>() {
    val VIEW_TYPE_CELL = 1
    val VIEW_TYPE_HEADER_1 = 2
    val VIEW_TYPE_HEADER_2 = 3
    val VIEW_TYPE_FOOTER_1 = 4
    val VIEW_TYPE_FOOTER_2 = 5

    override fun getSectionCount(): Int {
        return 5
    }

    override fun hasHeader(section: Int): Boolean {
        when (section) {
            0, 2 -> return true
            else -> return false
        }
    }

    override fun getHeaderType(section: Int): Int {
        when (section) {
            0 -> return VIEW_TYPE_HEADER_1
            else -> return VIEW_TYPE_HEADER_2
        }
    }

    override fun hasFooter(section: Int): Boolean {
        when (section) {
            1, 2, 4 -> return true
            else -> return false
        }
    }

    override fun getFooterType(section: Int): Int {
        when (section) {
            1, 4 -> return VIEW_TYPE_FOOTER_1
            else -> return VIEW_TYPE_FOOTER_2
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

    override fun getViewType(section: Int, position: Int): Int {
        return VIEW_TYPE_CELL
    }

    override fun onCreateHeader(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = when (viewType) {
            VIEW_TYPE_HEADER_1 -> LayoutInflater.from(parent.context).inflate(R.layout.view_header_1, parent, false)
            VIEW_TYPE_HEADER_2 -> LayoutInflater.from(parent.context).inflate(R.layout.view_header_2, parent, false)
            else -> throw Exception()
        }
        return MainViewHolder(itemView)
    }

    override fun onCreateFooter(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = when (viewType) {
            VIEW_TYPE_FOOTER_1 -> LayoutInflater.from(parent.context).inflate(R.layout.view_footer_1, parent, false)
            VIEW_TYPE_FOOTER_2 -> LayoutInflater.from(parent.context).inflate(R.layout.view_footer_2, parent, false)
            else -> throw Exception()
        }
        return MainViewHolder(itemView)
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): MainViewHolder {
        when (viewType) {
            VIEW_TYPE_CELL -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_cell, parent, false)
                return MainViewHolderCell(itemView)
            }
        }

        throw Exception()
    }

    override fun onBindView(holder: MainViewHolder, section: Int, position: Int) {
        if (holder is MainViewHolderCell) {
            holder.binding.textView.text = "${section} - ${position}"
        }
    }
}