package net.peakresponse.android.atak.plugin

import android.content.Context
import com.atak.plugins.impl.AbstractPluginTool
import gov.tak.api.util.Disposable
import net.peakresponse.atak.plugin.R

class PluginTool(context: Context) : AbstractPluginTool(
    context,
    context.getString(R.string.app_name),
    context.getString(R.string.app_desc),
    context.getDrawable(R.drawable.ic_launcher),
    PluginDropDownReceiver.SHOW_PLUGIN
), Disposable {

    companion object {
        private const val TAG = "PluginTool"
    }

    override fun dispose() {

    }
}
