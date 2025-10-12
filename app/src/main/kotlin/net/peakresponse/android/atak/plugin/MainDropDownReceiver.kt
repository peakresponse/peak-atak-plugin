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
import net.peakresponse.android.shared.PRAppData

abstract class TabFragment : Fragment() {
    abstract fun getTabTitle(): String
}

class MainPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<TabFragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}

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
    private val tabLayout: TabLayout

    init {
        view = PluginLayoutInflater.inflate(pluginContext, R.layout.main_layout)
        toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener(this)
        tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val fragments = ArrayList<TabFragment>()
        fragments.add(IncidentsFragment(pluginContext))
        viewPager.adapter = MainPagerAdapter(mapView.context as FragmentActivity, fragments)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragments[position].getTabTitle()
        }.attach()
    }

    fun show() {
        showDropDown(view, THIRD_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT)
    }

    override fun disposeImpl() {

    }

    override fun onReceive(context: Context?, intent: Intent) {

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
