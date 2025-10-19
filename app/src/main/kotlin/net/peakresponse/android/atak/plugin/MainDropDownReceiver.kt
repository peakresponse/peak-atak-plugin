package net.peakresponse.android.atak.plugin

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.atak.plugins.impl.PluginLayoutInflater
import com.atakmap.android.dropdown.DropDownReceiver
import com.atakmap.android.maps.MapView
import com.atakmap.coremap.log.Log
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import net.peakresponse.android.atak.plugin.R
import net.peakresponse.android.atak.plugin.components.TabFragment
import net.peakresponse.android.atak.plugin.components.TabFragmentPagerAdapter
import net.peakresponse.android.shared.PRAppData

class MainDropDownReceiver(
    mapView: MapView,
    private val pluginContext: Context
) : DropDownReceiver(mapView), Toolbar.OnMenuItemClickListener {

    companion object {
        const val TAG = "net.peakresponse.android.atak.plugin.MainDropDownReceiver"
    }

    public var onLogout: (() -> Unit)? = null

    private val view: View
    private val toolbar: Toolbar
    private val viewPager: ViewPager2

    //    private val tabLayout: TabLayout
    private val incidentDropDown: IncidentDropDownReceiver

    init {
        view = PluginLayoutInflater.inflate(pluginContext, R.layout.main_layout)
        toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener(this)
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.isSaveEnabled = false
        incidentDropDown = IncidentDropDownReceiver(mapView, pluginContext)

        val fragments = ArrayList<TabFragment>()
        val incidentsFragment = IncidentsFragment(pluginContext)
        incidentsFragment.onIncidentSelected = { incidentId ->
            Log.d(TAG, "incident id=${incidentId} selected")
            setRetain(true)
            incidentDropDown.show(incidentId)
        }
        fragments.add(incidentsFragment)
        viewPager.adapter = TabFragmentPagerAdapter(mapView.context as FragmentActivity, fragments)

//        tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = fragments[position].getTabTitle()
//        }.attach()
    }

    fun show() {
        showDropDown(view, THIRD_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT)
    }

    override fun disposeImpl() {
        Log.d(TAG, "disposeImpl")
    }

    override fun onReceive(context: Context?, intent: Intent) {
        Log.d(TAG, "onReceive context=$context, intent=$intent")
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {
                PRAppData.logout(mapView.context)
                onLogout?.invoke()
                return true
            }
        }
        return false
    }
}
