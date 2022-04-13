# RecyclerViewSectionAdapter

RecyclerView adapter with support for sections (similar to UITableView on iOS)

## How to use

Simply extend RecyclerViewSectionAdapter as you would normally extend a RecyclerView.Adapter on Android.

## Basic usage

Implement the following methods to specify the number of sections and items per section:

    fun getSectionCount(): Int
    fun getItemCount(section: Int): Int

Implement the following methods to create view holders for your sections:

    fun onCreateView(parent: ViewGroup, viewType: Int): VH
    fun onBindView(holder: VH, section: Int, position: Int)
    fun getViewType(section: Int, position: Int): Int
    
## Advanced usage
    
Optionally, you can add headers:

    fun hasHeader(section: Int): Boolean 
    fun onCreateHeader(parent: ViewGroup, viewType: Int): VH
    fun onBindHeader(holder: VH, section: Int)
    fun getHeaderType(section: Int): Int

Optionally, you can add footers:

    fun hasFooter(section: Int): Boolean
    fun onCreateFooter(parent: ViewGroup, viewType: Int): VH
    fun onBindFooter(holder: VH, section: Int)
    fun getFooterType(section: Int): Int

## Notifications

You can use the following specialised functions for notifying section or item changes:

    fun notifyItemInserted(section: Int, position: Int)
    fun notifyItemChanged(section: Int, position: Int)
    fun notifyItemRemoved(section: Int, position: Int)
