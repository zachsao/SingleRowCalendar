package com.michalsvec.singlerowcalendar.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * @author Michal Švec - misosvec01@gmail.com
 * @since v1.0.0
 */

class SingleRowCalendarAdapter(
    private var calendarViewManager: CalendarViewManager
) : ListAdapter<Date, SingleRowCalendarAdapter.CalendarViewHolder>(DateDiffUtil()) {

    lateinit var selectionTracker: SelectionTracker<Long>

    init {
        setHasStableIds(true)
    }

    inner class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }

    }


    override fun getItemViewType(position: Int): Int {
        return if (selectionTracker.isSelected(position.toLong())) {
            // when item is selected,position will have negative value + 1
            val selectedPosition = position + 1
            -selectedPosition
        } else
            position
    }


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CalendarViewHolder {

        val viewId = if (position < 0)
        // when position is negative, item is selected and then we have to take position back to original state
            calendarViewManager.setCalendarViewResourceId(
                (position * -1) - 1,
                getItem((position * -1) - 1),
                true
            )
        else
            calendarViewManager.setCalendarViewResourceId(
                position,
                getItem(position),
                false
            )


        val itemView = LayoutInflater.from(parent.context).inflate(viewId, parent, false)

        return CalendarViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) =
        calendarViewManager.bindDataToCalendarView(
            holder,
            getItem(position),
            position,
            selectionTracker.isSelected(position.toLong())
        )

    override fun getItemId(position: Int): Long = position.toLong()
}

class DateDiffUtil : DiffUtil.ItemCallback<Date>() {
    override fun areItemsTheSame(oldItem: Date, newItem: Date): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Date, newItem: Date): Boolean {
        return oldItem.time == newItem.time
    }
}