package tech.mulloni.recyclerviewsectionadapter

import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewSectionAdapter<VH : RecyclerView.ViewHolder?>: RecyclerView.Adapter<VH>() {

    /// Custom functions to manage sections

    abstract fun getSectionCount(): Int

    abstract fun getItemCount(section: Int): Int

    abstract fun onBindViewHolder(holder: VH, section: Int, position: Int)

    open fun getItemViewType(section: Int, position: Int): Int {
        return 0
    }

    /// Helper functions

    private fun splitPosition(rawPosition: Int): Pair<Int, Int> {
        var section = 0
        var position = rawPosition

        while (position >= getItemCount(section)) {
            position -= getItemCount(section)
            section += 1
        }

        return Pair(section, position)
    }

    fun notifyItemInserted(section: Int, position: Int) {
        val rawPosition = (0 until section).map { getItemCount(it) }.sum() + position
        super.notifyItemInserted(rawPosition)
    }

    fun notifyItemChanged(section: Int, position: Int) {
        val rawPosition = (0 until section).map { getItemCount(it) }.sum() + position
        super.notifyItemChanged(rawPosition)
    }

    fun notifyItemRemoved(section: Int, position: Int) {
        val rawPosition = (0 until section).map { getItemCount(it) }.sum() + position
        super.notifyItemRemoved(rawPosition)
    }

    /// Base Adapter functions implemented with sections

    final override fun getItemCount(): Int {
        return (0 until getSectionCount()).map { getItemCount(it) }.sum()
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        val splitPosition = splitPosition(position)
        onBindViewHolder(holder, splitPosition.first, splitPosition.second)
    }

    final override fun getItemViewType(position: Int): Int {
        val splitPosition = splitPosition(position)
        return getItemViewType(splitPosition.first, splitPosition.second)
    }
}