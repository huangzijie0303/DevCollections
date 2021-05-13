package com.zenchn.bdmessage.wrapper;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author:Hzj
 * @date :2021/5/13
 * desc  ：
 * record：
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount; //列数
    private int spacingInPx; //间隔
    private boolean includeEdge; //是否包含边缘

    public GridItemDecoration(int spanCount, int spacingInPx) {
        this(spanCount,spacingInPx,false);
    }

    public GridItemDecoration(int spanCount, int spacingInPx, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacingInPx = spacingInPx;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //这里是关键，需要根据你有几列来判断
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = spacingInPx - column * spacingInPx / spanCount;
            outRect.right = (column + 1) * spacingInPx / spanCount;

            if (position < spanCount) {
                outRect.top = spacingInPx;
            }
            outRect.bottom = spacingInPx;
        } else {
            outRect.left = column * spacingInPx / spanCount;
            outRect.right = spacingInPx - (column + 1) * spacingInPx / spanCount;
            if (position >= spanCount) {
                outRect.top = spacingInPx;
            }
        }
    }
}
