# Recyclerview 相关

## 使用说明：
```
RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this)
    .setStyle(RecyclerViewDivider.Style.BETWEEN)
    .setDrawableRes(R.drawable.divider)
    .setMarginLeft(72)
    .setMarginRight(8)
    .build();
mRvList.addItemDecoration(divider);

RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this)
    .setOrientation(RecyclerViewDivider.VERTICAL)
    .setStyle(RecyclerViewDivider.Style.BETWEEN)
    .setColorRes(R.color.divider_gray)
    .setSize(1.5f)
    .setStartSkipCount(2)
    .setMarginLeft(72)
    .setMarginRight(8)
    .build();
```
