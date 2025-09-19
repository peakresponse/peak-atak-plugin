package net.peakresponse.android.atak.plugin

import android.content.Context
import android.content.Intent
import android.view.View
import com.atak.plugins.impl.PluginLayoutInflater
import com.atakmap.android.dropdown.DropDown
import com.atakmap.android.dropdown.DropDownReceiver
import com.atakmap.android.maps.MapView
import com.atakmap.coremap.log.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.peakresponse.android.shared.api.PRApiClient
import net.peakresponse.atak.plugin.R

class PluginDropDownReceiver(
    mapView: MapView,
    private val pluginContext: Context): DropDownReceiver(mapView), DropDown.OnStateListener {
    companion object {
        private const val TAG = "PluginDropDownReceiver"
        public const val SHOW_PLUGIN = "net.peakresponse.atak.SHOW_PLUGIN"
    }

    private val view: View

    init {
        view = PluginLayoutInflater.inflate(pluginContext, R.layout.main_layout, null)
    }

    override fun disposeImpl() {

    }

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        if (action == null) return

        when (action) {
            SHOW_PLUGIN -> {
                if (!isClosed) {
                    unhideDropDown()
                    return
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val response = PRApiClient.getInstance().me()
                    Log.d(TAG, "response=$response")
                }
                showDropDown(view, THIRD_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT, false, this)
            }
        }
    }

    override fun onDropDownSelectionRemoved() {
    }

    override fun onDropDownClose() {
    }

    override fun onDropDownSizeChanged(width: Double, height: Double) {
    }

    override fun onDropDownVisible(visible: Boolean) {
    }
}