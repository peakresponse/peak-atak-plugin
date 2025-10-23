package net.peakresponse.android.atak.plugin

import android.content.Context
import android.content.Intent
import com.atakmap.android.dropdown.DropDownMapComponent
import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter
import com.atakmap.android.maps.MapView
import com.atakmap.coremap.log.Log
import net.peakresponse.android.shared.api.PRApiClient

class PluginMapComponent : DropDownMapComponent() {
    companion object {
        private const val TAG = "net.peakresponse.android.atak.plugin.PluginMapComponent"
    }

    private lateinit var pluginContext: Context
    private lateinit var dropDownReceiver: PluginDropDownReceiver
    private lateinit var mapOverlay: PluginMapOverlay

    override fun onCreate(context: Context, intent: Intent?, mapView: MapView) {
        Log.d(TAG, "PluginMapComponent onCreate")
        context.setTheme(R.style.ATAKPluginTheme)
        super.onCreate(context, intent, mapView)
        pluginContext = context

        PRApiClient.API_URL = BuildConfig.API_URL

        mapOverlay = PluginMapOverlay(mapView, pluginContext)
        mapView.mapOverlayManager.addOverlay(mapOverlay)

        dropDownReceiver = PluginDropDownReceiver(mapView, mapOverlay, pluginContext)
        val filter = DocumentedIntentFilter()
        filter.addAction(PluginDropDownReceiver.SHOW_PLUGIN, "Show the Plugin drop-down")
        registerDropDownReceiver(dropDownReceiver, filter)
    }

    override fun onStart(context: Context?, view: MapView?) {
        Log.d(TAG, "PluginMapComponent onStart")
    }

    override fun onPause(context: Context?, view: MapView?) {
        Log.d(TAG, "PluginMapComponent onPause")
    }

    override fun onResume(context: Context?, view: MapView?) {
        Log.d(TAG, "PluginMapComponent onResume")
    }

    override fun onStop(context: Context?, view: MapView?) {
        Log.d(TAG, "PluginMapComponent onStop")
    }

    override fun onDestroyImpl(context: Context, mapView: MapView) {
        Log.d(TAG, "PluginMapComponent onDestroyImpl")
        super.onDestroyImpl(context, mapView)
        mapView.mapOverlayManager.removeOverlay(mapOverlay)
    }
}
