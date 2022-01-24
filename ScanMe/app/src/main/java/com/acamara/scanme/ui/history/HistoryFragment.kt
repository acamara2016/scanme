package com.acamara.scanme.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.acamara.scanme.R
import com.acamara.scanme.adapters.TabPagerAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*


class HistoryFragment : Fragment() {

    //private lateinit var historyViewModel: historyViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var viewPager:ViewPager
    private lateinit var histViewModel : HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        histViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)
//          Handler().postDelayed({ load(root) }, 1500)
          root.toolbar.setNavigationOnClickListener { findNavController().navigate(R.id.action_nav_history_to_nav_home) }
        tabLayout = root.findViewById(R.id.history_tabs)
        viewPager = root.findViewById(R.id.viewpager)
        toolbar = root.findViewById(R.id.toolbar)
        configureTabLayout(root)
        return root
    }
    fun load(view: View){
        view.progressBar.visibility = View.GONE
        view.history_tabs.visibility = View.VISIBLE
    }

    private fun configureTabLayout(view:View) {
        histViewModel.tab_name.observe(viewLifecycleOwner, Observer { tab_name ->
            if (tab_name.size > 0) {
                history_tabs.visibility = View.VISIBLE
                no_history_feedback_view.visibility = View.GONE
                for (i in 0..tab_name.size - 1) {
                    tabLayout.addTab(tabLayout.newTab().setText(tab_name[i]))
                }
            }
            val adapter = TabPagerAdapter(
                    (activity as AppCompatActivity).supportFragmentManager,
                    tabLayout.tabCount,
                    tab_name)
            viewPager.adapter = adapter

            viewPager.addOnPageChangeListener(
                    TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.addOnTabSelectedListener(object :
                    TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }

            })
        })
    }



}