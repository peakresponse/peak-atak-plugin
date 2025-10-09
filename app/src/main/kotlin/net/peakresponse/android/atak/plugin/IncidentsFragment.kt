package net.peakresponse.android.atak.plugin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atak.plugins.impl.PluginLayoutInflater

import net.peakresponse.android.atak.plugin.R
import net.peakresponse.android.shared.PRAppData
import net.peakresponse.android.shared.PRSettings

class IncidentsFragment(
    private val pluginContext: Context
) : TabFragment() {
    override fun getTabTitle(): String {
        return pluginContext.getString(R.string.tab_mcis)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            PluginLayoutInflater.inflate(pluginContext, R.layout.incidents_layout, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        context?.let {
            val settings = PRSettings(it)
            settings.assignmentId?.let { assignmentId ->
                PRAppData.connectIncidents(it, assignmentId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PRAppData.disconnectIncidents()
    }
}
