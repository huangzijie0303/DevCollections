package com.zenchn.buildingmonitor.wrapper.recyclerview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView


/**
 * @author:Hzj
 * @date :2019/8/14/014
 * recyclerView 垂直、水平分割线
 */
class ItemSpaceDecoration(
    private var spaceInPx: Int,
    @ColorInt private var decorColor: Int = Color.TRANSPARENT,
    //方向是否垂直，默认true
    private var vertical: Boolean = true
) : RecyclerView.ItemDecoration() {

    private val mPaint: Paint = Paint()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = decorColor
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        // 第一个item上面不需要分割线
        if (parent.getChildAdapterPosition(view) != 0) {
            if (vertical) {
                outRect.set(0, spaceInPx, 0, 0)
            } else {
                outRect.set(spaceInPx, 0, 0, 0)
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        //因为getItemOffsets是针对每一个ItemView，而onDraw方法是针对RecyclerView本身，所以需要循环遍历来设置
        loop@ for (i in 0..childCount) {
            val child = parent.getChildAt(i)
            child?.let {
                val index = parent.getChildAdapterPosition(child)
                //第一个ItemView不需要绘制
                if (index > 0) {
                    if (vertical) {
                        val dividerTop = child.top - spaceInPx
                        val dividerLeft = parent.paddingLeft
                        val dividerBottom = child.top
                        val dividerRight = parent.width - parent.paddingRight
                        c.drawRect(Rect(dividerLeft, dividerTop, dividerRight, dividerBottom), mPaint)
                    } else {
                        val dividerTop = child.top
                        val dividerLeft = parent.paddingLeft - spaceInPx
                        val dividerBottom = child.top
                        val dividerRight = parent.width - parent.paddingRight
                        c.drawRect(Rect(dividerLeft, dividerTop, dividerRight, dividerBottom), mPaint)
                    }
                }
            }
        }
    }
}