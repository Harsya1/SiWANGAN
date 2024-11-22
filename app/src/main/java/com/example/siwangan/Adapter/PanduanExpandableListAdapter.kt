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

        // Set rotation based on the current expansion state
        dropdownIcon.clearAnimation()
        val rotationAngle = if (isExpanded) 180f else 0f
        dropdownIcon.rotation = rotationAngle

        // Check and update the group expansion status
        if (groupExpanded[groupPosition] != isExpanded) {
            groupExpanded[groupPosition] = isExpanded

            val rotateAnimation = RotateAnimation(
                if (isExpanded) 0f else 180f, // From degree
                if (isExpanded) 180f else 0f, // To degree
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = ROTATE_DURATION.toLong()
                fillAfter = true
            }
            dropdownIcon.startAnimation(rotateAnimation)
        }
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
