package com.acamara.scanme.getStarted.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.RequestManager


class MainPagerAdapter(
    private val context: Context,
    private val imageUrls: List<Int>,
    requestManager: RequestManager
) :
    PagerAdapter() {
    private val requestManager: RequestManager
    private var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(imageUrls[position], container, false)
        container.addView(v)
        return v
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    init {
        this.requestManager = requestManager
    }
}