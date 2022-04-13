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

    /// Create and manage views

    abstract fun onCreateView(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindView(holder: VH, section: Int, position: Int)

    open fun getViewType(section: Int, position: Int): Int {
        return 0
    }

    /// Create and manage headers

    open fun hasHeader(section: Int): Boolean {
        return false
    }

    open fun onCreateHeader(parent: ViewGroup, viewType: Int): VH {
        throw RuntimeException("Implement this method if you are using headers")
    }

    open fun onBindHeader(holder: VH, section: Int) { }

    open fun getHeaderType(section: Int): Int {
        return Int.MAX_VALUE - 1
    }

    /// Create and manage footers

    open fun hasFooter(section: Int): Boolean {
        return false
    }

    open fun onCreateFooter(parent: ViewGroup, viewType: Int): VH {
        throw RuntimeException("Implement this method if you are using footers")
    }

    open fun onBindFooter(holder: VH, section: Int) { }

    open fun getFooterType(section: Int): Int {
        return Int.MAX_VALUE
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

    val viewTypesHeader: MutableSet<Int> = mutableSetOf()
    val viewTypesFooter: MutableSet<Int> = mutableSetOf()
    val viewTypesView: MutableSet<Int> = mutableSetOf()

    final override fun getItemCount(): Int {
        return (0 until getSectionCount()).map { getSectionItemCount(it) }.sum()
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        when {
            viewTypesHeader.contains(viewType) -> return onCreateHeader(parent, viewType)
            viewTypesFooter.contains(viewType) -> return onCreateFooter(parent, viewType)
            else -> return onCreateView(parent, viewType)
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
                onBindView(holder, splitPosition.section, splitPosition.position)
            }
        }
    }

    final override fun getItemViewType(position: Int): Int {
        val splitPosition = computeSplitPosition(position)
        when (splitPosition.type) {
            SplitPositionType.header -> {
                val type = getHeaderType(splitPosition.section)
                viewTypesHeader.add(type)
                validateViewTypeCollisions()
                return type
            }
            SplitPositionType.footer -> {
                val type = getFooterType(splitPosition.section)
                viewTypesFooter.add(type)
                validateViewTypeCollisions()
                return type
            }
            SplitPositionType.view -> {
                val type = getViewType(splitPosition.section, splitPosition.position)
                viewTypesView.add(type)
                validateViewTypeCollisions()
                return type
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

    // This function makes sure that there are no view types used by headers, footers and views
    // at the same time. Every view type should only be used by either headers, footers or views.
    // In case there is a conflict, the function will throw and exception
    private fun validateViewTypeCollisions() {
        if (viewTypesHeader.intersect(viewTypesFooter).isNotEmpty() or
            viewTypesHeader.intersect(viewTypesView).isNotEmpty() or
            viewTypesFooter.intersect(viewTypesView).isNotEmpty())
        {
            throw RuntimeException("Please do not use the same ID for headers, footers or views at the same time. Every view type should only be used by either headers, footers or views.")
        }
    }
}