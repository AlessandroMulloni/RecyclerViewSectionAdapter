package tech.mulloni.recyclerviewsectionadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

enum class SplitPositionType { header, footer, view }
data class SplitPosition(
    val type: SplitPositionType,
    val section: Int,
    val position: Int
)

abstract class RecyclerViewSectionAdapter<VH : RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    /// Custom functions to manage sections

    abstract fun getSectionCount(): Int

    abstract fun getItemCount(section: Int): Int

    open fun hasHeader(section: Int): Boolean {
        return false
    }

    open fun hasFooter(section: Int): Boolean {
        return false
    }

    open fun onCreateViewHolderHeader(parent: ViewGroup): VH {
        throw RuntimeException("Implement this method if you are using headers")
    }

    open fun onCreateViewHolderFooter(parent: ViewGroup): VH {
        throw RuntimeException("Implement this method if you are using footers")
    }

    abstract fun onCreateViewHolderView(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindViewHolder(holder: VH, section: Int, position: Int)

    open fun onBindHeader(holder: VH, section: Int) { }

    open fun onBindFooter(holder: VH, section: Int) { }

    open fun getItemViewType(section: Int, position: Int): Int {
        return 0
    }

    /// Notifications

    fun notifyItemInserted(section: Int, position: Int) {
        val rawPosition = (0 until section).map { getSectionItemCount(it) }.sum() + position
        super.notifyItemInserted(rawPosition)
    }

    fun notifyItemChanged(section: Int, position: Int) {
        val rawPosition = (0 until section).map { getSectionItemCount(it) }.sum() + position
        super.notifyItemChanged(rawPosition)
    }

    fun notifyItemRemoved(section: Int, position: Int) {
        val rawPosition = (0 until section).map { getSectionItemCount(it) }.sum() + position
        super.notifyItemRemoved(rawPosition)
    }

    /// Base Adapter functions implemented with sections

    val HEADER_VIEW_TYPE = 0x01000000
    val FOOTER_VIEW_TYPE = 0x02000000

    final override fun getItemCount(): Int {
        return (0 until getSectionCount()).map { getSectionItemCount(it) }.sum()
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        when (viewType) {
            HEADER_VIEW_TYPE -> return onCreateViewHolderHeader(parent)
            FOOTER_VIEW_TYPE -> return onCreateViewHolderFooter(parent)
            else -> return onCreateViewHolderView(parent, viewType)
        }
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        val splitPosition = computeSplitPosition(position)
        when (splitPosition.type) {
            SplitPositionType.header -> {
                onBindHeader(holder, splitPosition.section)
            }
            SplitPositionType.footer -> {
                onBindFooter(holder, splitPosition.section)
            }
            SplitPositionType.view -> {
                onBindViewHolder(holder, splitPosition.section, splitPosition.position)
            }
        }
    }

    final override fun getItemViewType(position: Int): Int {
        val splitPosition = computeSplitPosition(position)
        when (splitPosition.type) {
            SplitPositionType.header -> return HEADER_VIEW_TYPE
            SplitPositionType.footer -> return FOOTER_VIEW_TYPE
            SplitPositionType.view -> {
                val itemViewType = getItemViewType(splitPosition.section, splitPosition.position)
                if (itemViewType == HEADER_VIEW_TYPE || itemViewType == FOOTER_VIEW_TYPE) {
                    throw RuntimeException("Please do not use the IDs reserved for headers (${HEADER_VIEW_TYPE}) and footers ($FOOTER_VIEW_TYPE)")
                }
                return itemViewType
            }
        }
    }

    /// Helper functions

    fun getSectionItemCount(section: Int): Int {
        val headerCount = if (hasHeader(section)) 1 else 0
        val footerCount = if (hasFooter(section)) 1 else 0
        return headerCount + getItemCount(section) + footerCount
    }

    private fun computeSplitPosition(rawPosition: Int): SplitPosition {
        var section = 0
        var position = rawPosition

        while (position >= getSectionItemCount(section)) {
            position -= getSectionItemCount(section)
            section += 1
        }

        if (hasHeader(section) && (position == 0)) {
            return SplitPosition(SplitPositionType.header, section, 0)
        } else if (hasFooter(section) && (position == (getSectionItemCount(section) - 1))) {
            return SplitPosition(SplitPositionType.footer, section, 0)
        } else {
            val adjustedPosition = if (hasHeader(section)) (position - 1) else position
            return SplitPosition(SplitPositionType.view, section, adjustedPosition)
        }
    }
}