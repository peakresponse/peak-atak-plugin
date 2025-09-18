package net.peakresponse.atak.plugin

import com.atak.plugins.impl.AbstractPlugin
import com.atak.plugins.impl.PluginContextProvider
import gov.tak.api.plugin.IServiceController

class PluginLifecycle(serviceController: IServiceController): AbstractPlugin(
    serviceController,
    PluginTool(serviceController.getService<PluginContextProvider?>(PluginContextProvider::class.java).getPluginContext()),
    PluginMapComponent()
)
