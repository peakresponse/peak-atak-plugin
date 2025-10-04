package net.peakresponse.android.atak.plugin

import android.content.Context
import android.content.Intent
import com.atakmap.android.dropdown.DropDownMapComponent
import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter
import com.atakmap.android.maps.MapView
import net.peakresponse.android.atak.plugin.R

class PluginMapComponent : DropDownMapComponent() {
    companion object {
        private const val TAG = "net.peakresponse.android.atak.plugin.PluginMapComponent"
    }

    private var context: Context? = null
    private var dropDown: PluginDropDownReceiver? = null

    override fun onCreate(context: Context, intent: Intent?, mapView: MapView) {
        context.setTheme(R.style.ATAKPluginTheme)
        super.onCreate(context, intent, mapView)
        this.context = context

        dropDown = PluginDropDownReceiver(mapView, context)
        val filter = DocumentedIntentFilter()
        filter.addAction(PluginDropDownReceiver.SHOW_PLUGIN, "Show the Plugin drop-down")
        filter.addAction(
            LoginDropDownReceiver.SET_AUTHENTICATED,
            "Sets the Plugin state to AUTHENTICATED"
        )
        registerDropDownReceiver(dropDown, filter)
    }

    override fun onStart(context: Context?, view: MapView?) {
    }

    override fun onPause(context: Context?, view: MapView?) {
    }

    override fun onResume(context: Context?, view: MapView?) {
    }

    override fun onStop(context: Context?, view: MapView?) {
    }
}