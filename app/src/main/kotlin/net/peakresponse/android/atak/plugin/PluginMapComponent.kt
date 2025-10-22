package net.peakresponse.android.atak.plugin

import android.content.Context
import android.content.Intent
import com.atakmap.android.dropdown.DropDownMapComponent
import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter
import com.atakmap.android.maps.DefaultMapGroup
import com.atakmap.android.maps.MapView
import com.atakmap.android.overlay.DefaultMapGroupOverlay
import net.peakresponse.android.shared.api.PRApiClient

class PluginMapComponent : DropDownMapComponent() {
    companion object {
        private const val TAG = "net.peakresponse.android.atak.plugin.PluginMapComponent"
    }

    private lateinit var pluginContext: Context
    private lateinit var dropDown: PluginDropDownReceiver
    private lateinit var defaultMapGroup: DefaultMapGroup
    private lateinit var defaultMapOverlay: DefaultMapGroupOverlay

    override fun onCreate(context: Context, intent: Intent?, mapView: MapView) {
        context.setTheme(R.style.ATAKPluginTheme)
        super.onCreate(context, intent, mapView)
        pluginContext = context

        PRApiClient.API_URL = BuildConfig.API_URL

        defaultMapGroup = DefaultMapGroup(pluginContext.getString(R.string.map_group_friendly_name))
        defaultMapOverlay = DefaultMapGroupOverlay(mapView, defaultMapGroup)

        mapView.rootGroup.addGroup(defaultMapGroup)
        mapView.mapOverlayManager.addOverlay(defaultMapOverlay)

        dropDown = PluginDropDownReceiver(mapView, pluginContext)
        val filter = DocumentedIntentFilter()
        filter.addAction(PluginDropDownReceiver.SHOW_PLUGIN, "Show the Plugin drop-down")
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