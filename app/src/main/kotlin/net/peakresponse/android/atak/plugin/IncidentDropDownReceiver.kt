package net.peakresponse.android.atak.plugin

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.atak.plugins.impl.PluginLayoutInflater
import com.atakmap.android.dropdown.DropDown
import com.atakmap.android.dropdown.DropDownReceiver
import com.atakmap.android.maps.MapView
import com.atakmap.coremap.log.Log
import net.peakresponse.android.atak.plugin.components.TabFragment
import net.peakresponse.android.atak.plugin.components.TabFragmentPagerAdapter

class IncidentDropDownReceiver(
    mapView: MapView,
    val pluginContext: Context
) : DropDownReceiver(mapView), DropDown.OnStateListener {
    companion object {
        const val TAG = "net.peakresponse.android.atak.plugin.IncidentDropDownReceiver"
    }

    private val view: View
    private val toolbar: Toolbar
    private val viewPager: ViewPager2


    init {
        view = PluginLayoutInflater.inflate(pluginContext, R.layout.incident_layout)
        toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val fragments = ArrayList<TabFragment>()
        fragments.add(IncidentFragment(pluginContext))
        viewPager.adapter = TabFragmentPagerAdapter(mapView.context as FragmentActivity, fragments)
    }

    fun show(incidentId: String) {
        showDropDown(view, THIRD_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT)
    }

    override fun disposeImpl() {

    }

    override fun onReceive(context: Context?, intent: Intent?) {

    }

    override fun onDropDownSelectionRemoved() {
        Log.d(TAG, "onDropDownSelectionRemoved")
    }

    override fun onDropDownClose() {
        Log.d(TAG, "onDropDownClose")
    }

    override fun onDropDownSizeChanged(width: Double, height: Double) {
        Log.d(TAG, "onDropDownSizeChanged")
    }

    override fun onDropDownVisible(visible: Boolean) {
        Log.d(TAG, "onDropDownVisible")
    }
}