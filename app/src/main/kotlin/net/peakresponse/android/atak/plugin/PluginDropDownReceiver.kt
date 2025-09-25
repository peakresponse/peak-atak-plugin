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
import kotlinx.coroutines.withContext

import net.peakresponse.android.shared.PRAppData
import net.peakresponse.atak.plugin.R

enum class PluginState {
    STATE_INIT,
    STATE_UNAUTHENTICATED,
    STATE_AUTHENTICATED,
}

class PluginDropDownReceiver(
    mapView: MapView,
    private val pluginContext: Context
) : DropDownReceiver(mapView), DropDown.OnStateListener {
    companion object {
        private const val TAG = "PluginDropDownReceiver"
        public const val SHOW_PLUGIN = "net.peakresponse.atak.SHOW_PLUGIN"
    }

    private var state: PluginState = PluginState.STATE_INIT
    private val view: View
    private val loginDropDown: LoginDropDownReceiver

    init {
        view = PluginLayoutInflater.inflate(pluginContext, R.layout.main_layout, null)
        loginDropDown = LoginDropDownReceiver(mapView, pluginContext)
    }

    override fun disposeImpl() {
        Log.d(TAG, "disposeImpl")
    }

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        if (action == null) return

        Log.d(TAG, "onReceive state=$state action=$action isClosed=$isClosed")

        when (action) {
            SHOW_PLUGIN -> {
                if (!isClosed) {
                    unhideDropDown()
                    return
                }
                when (state) {
                    PluginState.STATE_INIT -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = PRAppData.me()
                            if (response.isSuccessful) {
                                Log.d(TAG, "Success")
                            } else {
                                state = PluginState.STATE_UNAUTHENTICATED
                                Log.d(TAG, "Not logged in, code=" + response.code())
                                withContext(Dispatchers.Main) {
                                    loginDropDown.show()
                                }
                            }
                        }
                        showDropDown(
                            view,
                            THIRD_WIDTH,
                            FULL_HEIGHT,
                            FULL_WIDTH,
                            HALF_HEIGHT,
                            false,
                            this
                        )
                        setRetain(false)
                    }

                    PluginState.STATE_UNAUTHENTICATED -> {
                        loginDropDown.show()
                    }

                    PluginState.STATE_AUTHENTICATED -> {
                        // noop
                    }
                }
            }
        }
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
