package net.peakresponse.android.atak.plugin

import android.content.Context
import android.widget.BaseAdapter
import com.atakmap.android.hierarchy.HierarchyListFilter
import com.atakmap.android.hierarchy.HierarchyListItem
import com.atakmap.android.hierarchy.items.MapGroupHierarchyListItem
import com.atakmap.android.maps.DeepMapItemQuery
import com.atakmap.android.maps.DefaultMapGroup
import com.atakmap.android.maps.MapGroup
import com.atakmap.android.maps.MapView
import com.atakmap.android.overlay.AbstractMapOverlay2
import com.atakmap.coremap.log.Log

class PluginMapOverlay(
    private val mapView: MapView,
    private val pluginContext: Context
) : AbstractMapOverlay2() {
    companion object {
        const val TAG = "net.peakresponse.android.atak.plugin.PluginMapOverlay"
    }

    private val group: DefaultMapGroup
    private lateinit var listModel: MapGroupHierarchyListItem

    init {
        group = DefaultMapGroup(pluginContext.getString(R.string.app_name))
        group.setMetaString(
            "iconUri",
            "android.resource://${pluginContext.getPackageName()}/${R.drawable.ic_launcher}"
        )
        for (group in mapView.rootGroup.childGroups) {
            Log.d(TAG, "group=${group.friendlyName}")
        }
        mapView.rootGroup.addGroup(group)
    }

    override fun getListModel(
        listener: BaseAdapter?,
        actions: Long,
        filter: HierarchyListFilter?
    ): HierarchyListItem? {
        if (!this::listModel.isInitialized) {
            listModel = MapGroupHierarchyListItem(null, mapView, group, filter, listener)
        } else {
            listModel.refresh(listener, filter)
        }
        return listModel
    }

    override fun getIdentifier(): String? {
        return TAG
    }

    override fun getName(): String? {
        return pluginContext.getString(R.string.app_name)
    }

    override fun getRootGroup(): MapGroup? {
        return group
    }

    override fun getQueryFunction(): DeepMapItemQuery? {
        return null
    }
}
