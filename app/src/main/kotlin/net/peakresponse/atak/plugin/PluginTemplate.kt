package net.peakresponse.atak.plugin

import android.content.Context
import android.graphics.drawable.Drawable
import com.atak.plugins.impl.PluginContextProvider
import com.atak.plugins.impl.PluginLayoutInflater
import gov.tak.api.commons.graphics.Bitmap
import gov.tak.api.plugin.IPlugin
import gov.tak.api.plugin.IServiceController
import gov.tak.api.ui.IHostUIService
import gov.tak.api.ui.Pane
import gov.tak.api.ui.PaneBuilder
import gov.tak.api.ui.ToolbarItem
import gov.tak.api.ui.ToolbarItemAdapter
import gov.tak.platform.marshal.MarshalManager

class PluginTemplate(serviceController: IServiceController) : IPlugin {
    var serviceController: IServiceController?
    var pluginContext: Context? = null
    var uiService: IHostUIService?
    var toolbarItem: ToolbarItem?
    var templatePane: Pane? = null

    init {
        this.serviceController = serviceController
        val ctxProvider = serviceController
            .getService<PluginContextProvider?>(PluginContextProvider::class.java)
        if (ctxProvider != null) {
            pluginContext = ctxProvider.getPluginContext()
            pluginContext!!.setTheme(R.style.ATAKPluginTheme)
        }

        // obtain the UI service
        uiService = serviceController.getService<IHostUIService?>(IHostUIService::class.java)

        // initialize the toolbar button for the plugin

        // create the button
        toolbarItem = ToolbarItem.Builder(
            pluginContext!!.getString(R.string.app_name),
            MarshalManager.marshal<Bitmap?, Drawable?>(
                pluginContext!!.getResources().getDrawable(R.drawable.ic_launcher),
                Drawable::class.java,
                Bitmap::class.java
            )
        )
            .setListener(object : ToolbarItemAdapter() {
                override fun onClick(item: ToolbarItem?) {
                    showPane()
                }
            })
            .build()
    }

    override fun onStart() {
        // the plugin is starting, add the button to the toolbar
        if (uiService == null) return

        uiService!!.addToolbarItem(toolbarItem)
    }

    override fun onStop() {
        // the plugin is stopping, remove the button from the toolbar
        if (uiService == null) return

        uiService!!.removeToolbarItem(toolbarItem)
    }

    private fun showPane() {
        // instantiate the plugin view if necessary
        if (templatePane == null) {
            // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
            // In this case, using it is not necessary - but I am putting it here to remind
            // developers to look at this Inflator

            templatePane = PaneBuilder(
                PluginLayoutInflater.inflate(
                    pluginContext,
                    R.layout.main_layout, null
                )
            ) // relative location is set to default; pane will switch location dependent on
                // current orientation of device screen
                .setMetaValue(
                    Pane.RELATIVE_LOCATION,
                    Pane.Location.Default
                ) // pane will take up 50% of screen width in landscape mode
                .setMetaValue(
                    Pane.PREFERRED_WIDTH_RATIO,
                    0.5
                ) // pane will take up 50% of screen height in portrait mode
                .setMetaValue(Pane.PREFERRED_HEIGHT_RATIO, 0.5)
                .build()
        }

        // if the plugin pane is not visible, show it!
        if (!uiService!!.isPaneVisible(templatePane)) {
            uiService!!.showPane(templatePane, null)
        }
    }
}
