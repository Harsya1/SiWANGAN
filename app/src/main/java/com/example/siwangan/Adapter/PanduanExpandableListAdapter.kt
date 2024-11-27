package com.example.siwangan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class PanduanExpandableListAdapter(
    private val context: Context,
    private val listDataHeader: List<String>,
    private val listDataChild: HashMap<String, List<String>>
    ) : BaseExpandableListAdapter() {

    companion object {
        private const val ROTATE_DURATION = 300
    }

    private val groupExpanded: BooleanArray = BooleanArray(listDataHeader.size)

    fun isGroupExpanded(groupPosition: Int): Boolean = groupExpanded[groupPosition]

    override fun getGroupCount(): Int = listDataHeader.size

    override fun getChildrenCount(groupPosition: Int): Int =
        listDataChild[listDataHeader[groupPosition]]?.size ?: 0

    override fun getGroup(groupPosition: Int): Any = listDataHeader[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Any =
        listDataChild[listDataHeader[groupPosition]]?.get(childPosition) ?: ""

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val headerTitle = getGroup(groupPosition) as String
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_grup, parent, false)

        val lblListHeader = view.findViewById<TextView>(R.id.txtTitleList)
        lblListHeader.text = headerTitle

        val dropdownIcon = view.findViewById<ImageView>(R.id.btnDown)

        // Set rotation directly without animation
        val rotationAngle = if (isExpanded) 180f else 0f
        dropdownIcon.animate().rotation(rotationAngle).setDuration(ROTATE_DURATION.toLong()).start()

        // Update the group expanded status
        groupExpanded[groupPosition] = isExpanded

        return view
    }


    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val childText = getChild(groupPosition, childPosition) as String
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val txtListChild = view.findViewById<TextView>(R.id.txtListItem)
        txtListChild.text = childText

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}
