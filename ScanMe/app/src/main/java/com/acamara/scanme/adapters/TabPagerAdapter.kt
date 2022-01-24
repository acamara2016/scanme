package com.acamara.scanme.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.acamara.scanme.ui.history.HistoryCategoryFragment
/**
*This class is the adapter for the tab in the HistoryCategoryFragment
* 2020/12/06
*/
class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int, private var list: List<String>) :
        FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
    
        return HistoryCategoryFragment.newInstance(list[position])
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }
}
