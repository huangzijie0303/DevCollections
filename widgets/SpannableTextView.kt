package com.zenchn.buildingmonitor.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author:Hzj
 * @date  :2019/10/12/012
 * desc  ：SpannableString样式封装
 * record：
 * 使用方法：
 * RoundedStrokeSpan strokeSpan = new RoundedStrokeSpan(this, R.color.colorAccent, R.color.colorPrimary);
 * strokeSpan.setPadding(8, -8, 10, 18);
 * <p>
 * SpannableTextView textView = (SpannableTextView) findViewById(R.id.textview);
 * textView.addText("你好世界", new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),new StrikethroughSpan())
 * .text("电话")
 * .setSpan(strokeSpan)
 * .setUrl("tel:0123456789")
 * .setFontColor(getResources().getColor(R.color.colorPrimary))
 * .add()
 * .addImage(getResources().getDrawable(R.drawable.icon_city))
 * .text("我的blog")
 * .setUrl("http://blog.yzapp.cn")
 * .setFontStyle(Typeface.BOLD_ITALIC)
 * .setFontColor(getResources().getColor(R.color.colorPrimary))
 * .add();
 */
class SpannableTextView : AppCompatTextView, ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var mSpannableString: SpannableString
    private var mLength: Int = 0
    //自动调整大小保证一行显示
    private var mAutoSizeSingleLine = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    init {
        //第一步：给TextVIew添加布局改变监听，以便当调用setText方法时收到通知
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        if (mAutoSizeSingleLine) {
            if (lineCount > 1) {
                var textSize = textSize
                textSize--
                //重新设置大小,该方法会立即触发onGlobalLayout()方法。这里相当于递归调用，直至文本行数小于1行为止。
                setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
        }
    }

    /**
     * 设置是否自动调整大小保证一行
     */
    fun setAutoSizeSingleLine(autoSize: Boolean): SpannableTextView {
        this.mAutoSizeSingleLine = autoSize
        return this
    }

    /**
     * 清空文本
     */
    fun clear() {
        text = null
        mLength = 0
    }

    /**
     * 在列表中使用时，要先调用此方法清空文本，避免列表刷新时重复添加文本
     */
    fun clearWhenUseInList(): SpannableTextView {
        text = null
        mLength = 0
        return this
    }
    
    /**
     * 设置文本
     *
     * @return SpannableTextView
     */
    fun text(text: CharSequence): SpannableTextView {
        mSpannableString = SpannableString(text)
        mLength = text.length
        return this
    }

    /**
     * 设置自定义样式
     *
     * @return SpannableTextView
     */
    fun setSpan(span: CharacterStyle): SpannableTextView {
        mSpannableString.setSpan(span, 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体颜色
     *
     * @return SpannableTextView
     */
    fun setFontColor(@ColorInt color: Int): SpannableTextView {
        mSpannableString.setSpan(ForegroundColorSpan(color), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置背景颜色
     *
     * @return SpannableTextView
     */
    fun setBackColor(@ColorInt color: Int): SpannableTextView {
        mSpannableString.setSpan(BackgroundColorSpan(color), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体大小
     *  @param size in sp
     * @return SpannableTextView
     */
    fun setFontSize(size: Int): SpannableTextView {
        mSpannableString.setSpan(AbsoluteSizeSpan(size, true), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体相对大小
     *
     * @return SpannableTextView
     */
    fun setFontRelativeSize(relativeSize: Float): SpannableTextView {
        mSpannableString.setSpan(RelativeSizeSpan(relativeSize), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置正常，粗体，斜体，粗斜体
     *
     * @param typeface android.graphics.Typeface.NORMAL\BOLD\ITALIC\BOLD_ITALIC
     * @return SpannableTextView
     */
    fun setFontStyle(typeface: Int): SpannableTextView {
        mSpannableString.setSpan(StyleSpan(typeface), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 下划线
     *
     * @return SpannableTextView
     */
    fun setFontUnderLine(): SpannableTextView {
        mSpannableString.setSpan(UnderlineSpan(), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 删除线
     *
     * @return SpannableTextView
     */
    fun setFontStrike(): SpannableTextView {
        mSpannableString.setSpan(StrikethroughSpan(), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 上标
     *
     * @return SpannableTextView
     */
    fun setFontSubscript(): SpannableTextView {
        mSpannableString.setSpan(SubscriptSpan(), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 下标
     *
     * @return SpannableTextView
     */
    fun setFontSuperscript(): SpannableTextView {
        mSpannableString.setSpan(SuperscriptSpan(), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 超级链接
     *
     * @param url tel:电话;
     * mailto:邮件;
     * http://网址;
     * sms:短信 或者smsto;
     * mms:彩信 或者mmsto;
     * geo:地图（geo:38.899533,-77.036476）
     * @return SpannableTextView
     */
    fun setUrl(url: String): SpannableTextView {
        mSpannableString.setSpan(URLSpan(url), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        movementMethod = LinkMovementMethod.getInstance()
        return this
    }

    /**
     * 在文本样式最后调用，添加设置好字体样式的文本
     *
     * @return SpannableTextView
     */
    fun add(): SpannableTextView {
        append(mSpannableString)
        return this
    }

    /**
     * 直接增加的方法
     *
     * @param text  文字
     * @param spans 文本样式们
     * @return SpannableTextView
     */
    fun addText(text: CharSequence, vararg spans: CharacterStyle): SpannableTextView {
        val spannableString = SpannableString(text)
        for (span in spans) {
            spannableString.setSpan(span, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        append(spannableString)
        return this
    }

    /**
     * 添加图片，更多图片样式请使用setSpan方法
     *
     * @param d Drawable
     * @return SpannableTextView
     */
    fun addImage(d: Drawable): SpannableTextView {
        val spanString = SpannableString(" ")
        d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        val span = ImageSpan(d, ImageSpan.ALIGN_BASELINE)
        spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        append(spanString)
        return this
    }
}
