package com.zenchn.buildingmonitor.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.AttributeSet
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
 * SuperTextView textView = (SuperTextView) findViewById(R.id.textview);
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
class SuperTextView : AppCompatTextView {

    private lateinit var mSpannableString: SpannableString
    private var mLength: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )


    /**
     * 清空文本
     */
    fun clear() {
        text = null
        mLength = 0
    }

    /**
     * 设置文本
     *
     * @return SuperTextView
     */
    fun text(text: CharSequence): SuperTextView {
        mSpannableString = SpannableString(text)
        mLength = text.length
        return this
    }

    /**
     * 设置自定义样式
     *
     * @return SuperTextView
     */
    fun setSpan(span: CharacterStyle): SuperTextView {
        mSpannableString.setSpan(span, 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体颜色
     *
     * @return SuperTextView
     */
    fun setFontColor(@ColorInt color: Int): SuperTextView {
        mSpannableString.setSpan(ForegroundColorSpan(color), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置背景颜色
     *
     * @return SuperTextView
     */
    fun setBackColor(@ColorInt color: Int): SuperTextView {
        mSpannableString.setSpan(BackgroundColorSpan(color), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体大小
     *  @param size in sp
     * @return SuperTextView
     */
    fun setFontSize(size: Int): SuperTextView {
        mSpannableString.setSpan(AbsoluteSizeSpan(size, true), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体相对大小
     *
     * @return SuperTextView
     */
    fun setFontRelativeSize(relativeSize: Float): SuperTextView {
        mSpannableString.setSpan(RelativeSizeSpan(relativeSize), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置正常，粗体，斜体，粗斜体
     *
     * @param typeface android.graphics.Typeface.NORMAL\BOLD\ITALIC\BOLD_ITALIC
     * @return SuperTextView
     */
    fun setFontStyle(typeface: Int): SuperTextView {
        mSpannableString.setSpan(StyleSpan(typeface), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 下划线
     *
     * @return SuperTextView
     */
    fun setFontUnderLine(): SuperTextView {
        mSpannableString.setSpan(UnderlineSpan(), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 删除线
     *
     * @return SuperTextView
     */
    fun setFontStrike(): SuperTextView {
        mSpannableString.setSpan(StrikethroughSpan(), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 上标
     *
     * @return SuperTextView
     */
    fun setFontSubscript(): SuperTextView {
        mSpannableString.setSpan(SubscriptSpan(), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 下标
     *
     * @return SuperTextView
     */
    fun setFontSuperscript(): SuperTextView {
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
     * @return SuperTextView
     */
    fun setUrl(url: String): SuperTextView {
        mSpannableString.setSpan(URLSpan(url), 0, mLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        movementMethod = LinkMovementMethod.getInstance()
        return this
    }

    /**
     * 在文本样式最后调用，添加设置好字体样式的文本
     *
     * @return SuperTextView
     */
    fun add(): SuperTextView {
        append(mSpannableString)
        return this
    }

    /**
     * 直接增加的方法
     *
     * @param text  文字
     * @param spans 文本样式们
     * @return SuperTextView
     */
    fun addText(text: CharSequence, vararg spans: CharacterStyle): SuperTextView {
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
     * @return SuperTextView
     */
    fun addImage(d: Drawable): SuperTextView {
        val spanString = SpannableString(" ")
        d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        val span = ImageSpan(d, ImageSpan.ALIGN_BASELINE)
        spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        append(spanString)
        return this
    }
}