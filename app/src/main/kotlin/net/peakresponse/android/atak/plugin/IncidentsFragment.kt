package net.peakresponse.android.atak.plugin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atak.plugins.impl.PluginLayoutInflater

import net.peakresponse.android.atak.plugin.R

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
    }
}