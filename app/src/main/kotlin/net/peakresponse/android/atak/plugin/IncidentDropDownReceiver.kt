package net.peakresponse.android.atak.plugin

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.atak.plugins.impl.PluginLayoutInflater
import com.atakmap.android.dropdown.DropDown
import com.atakmap.android.dropdown.DropDownReceiver
import com.atakmap.android.maps.MapView
import com.atakmap.android.maps.Marker
import com.atakmap.android.util.IconUtilities
import com.atakmap.coremap.log.Log
import com.atakmap.coremap.maps.coords.GeoPoint
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.peakresponse.android.atak.plugin.components.TabFragment
import net.peakresponse.android.atak.plugin.components.TabFragmentPagerAdapter
import net.peakresponse.android.shared.PRAppData
import net.peakresponse.android.shared.models.ReportWithPatient
import net.peakresponse.android.shared.models.Scene

val MARKER_DRAWABLES = listOf(
    R.drawable.marker_immediate,
    R.drawable.marker_delayed,
    R.drawable.marker_minor,
    R.drawable.marker_expectant,
    R.drawable.marker_dead
)

class IncidentDropDownReceiver(
    mapView: MapView,
    val pluginContext: Context
) : DropDownReceiver(mapView), DropDown.OnStateListener, View.OnAttachStateChangeListener {
    companion object {
        const val TAG = "net.peakresponse.android.atak.plugin.IncidentDropDownReceiver"
    }

    private val view: View
    private val toolbar: Toolbar
    private val tabLayout: TabLayout
    private val fragments: ArrayList<TabFragment>
    private val viewPager: ViewPager2
    private lateinit var incidentId: String

    init {
        view = PluginLayoutInflater.inflate(pluginContext, R.layout.incident_layout)
        toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        view.addOnAttachStateChangeListener(this)
        toolbar.setNavigationOnClickListener {
            closeDropDown()
        }

        fragments = ArrayList<TabFragment>()
        fragments.add(SceneOverviewFragment(pluginContext))
        viewPager.adapter = TabFragmentPagerAdapter(mapView.context as FragmentActivity, fragments)

        tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragments[position].getTabTitle()
        }.attach()
    }

    fun show(incidentId: String) {
        this.incidentId = incidentId
        showDropDown(view, THIRD_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT, false, this)
    }

    override fun disposeImpl() {

    }

    override fun onReceive(context: Context?, intent: Intent?) {

    }

    override fun onViewAttachedToWindow(view: View) {
        Log.d(TAG, "onViewAttachedToWindow lifecycleOwner=${view.findViewTreeLifecycleOwner()}")
        val db = PRAppData.getDb(mapView.context)
        val incidentDao = db.getIncidentDao()
        val sceneDao = db.getSceneDao()
        val reportDao = db.getReportDao()
        view.findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
            Log.d(TAG, "found view lifecycleOwner $lifecycleOwner")
            lifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val incidentFlow = incidentDao.getIncident(incidentId)
                        .distinctUntilChanged()
                        .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    var sceneFlow: Flow<Scene>? = null
                    var reportsFlow: Flow<List<ReportWithPatient>>? = null
                    incidentFlow.collect { incident ->
                        Log.d(TAG, "incident=$incident")
                        withContext(Dispatchers.Main) {
                            toolbar.title = "#${incident.number ?: ""}"
                        }
                        if (sceneFlow == null) {
                            incident.sceneId?.let { sceneId ->
                                PRAppData.connectScene(mapView.context, sceneId)
                                sceneFlow = sceneDao.getScene(sceneId)
                                    .distinctUntilChanged()
                                    .flowWithLifecycle(
                                        lifecycleOwner.lifecycle,
                                        Lifecycle.State.STARTED
                                    )
                                launch {
                                    sceneFlow.collect { scene ->
                                        Log.d(TAG, "scene=$scene")
                                        withContext(Dispatchers.Main) {
                                            (fragments[viewPager.currentItem] as? SceneOverviewFragment)?.setScene(
                                                scene
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (reportsFlow == null) {
                            reportsFlow = reportDao.getReportsWithPatients(incidentId)
                                .distinctUntilChanged()
                                .flowWithLifecycle(
                                    lifecycleOwner.lifecycle,
                                    Lifecycle.State.STARTED
                                )
                            launch {
                                reportsFlow.collect { reports ->
                                    Log.d(TAG, "reports=$reports")
                                    withContext(Dispatchers.Main) {
                                        val mapGroup =
                                            mapView.rootGroup.findMapGroup(pluginContext.getString(R.string.map_group_friendly_name))
                                        for (reportWithPatient in reports) {
                                            var point: GeoPoint? = null
                                            reportWithPatient.patient?.lat?.let { lat ->
                                                reportWithPatient.patient?.lng?.let { lng ->
                                                    point = GeoPoint(lat.toDouble(), lng.toDouble())
                                                }
                                            }
                                            point?.let { point ->
                                                var marker =
                                                    mapGroup.deepFindUID(reportWithPatient.report.id) as? Marker
                                                if (marker == null) {
                                                    marker =
                                                        Marker(point, reportWithPatient.report.id)
                                                    mapGroup.addItem(marker)
                                                } else {
                                                    marker.point = point
                                                }
                                                marker.title = "#${reportWithPatient.report.pin}"
                                                Log.d(
                                                    TAG,
                                                    "report priority=${reportWithPatient.report.priority}"
                                                )
                                                reportWithPatient.report.priority?.let { priority ->
                                                    IconUtilities.setIcon(
                                                        pluginContext,
                                                        marker,
                                                        MARKER_DRAWABLES[priority],
                                                        false
                                                    )
                                                }
                                            } ?: run {
                                                val marker =
                                                    mapGroup.deepFindUID(reportWithPatient.report.id) as? Marker
                                                marker?.let { marker ->
                                                    mapGroup.removeItem(marker)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewDetachedFromWindow(view: View) {
        Log.d(TAG, "onViewDetachedFromWindow")
        PRAppData.disconnectScene()
    }

    override fun onDropDownSelectionRemoved() {
        Log.d(TAG, "onDropDownSelectionRemoved=$this")
    }

    override fun onDropDownClose() {
        Log.d(TAG, "onDropDownClose=$this")
    }

    override fun onDropDownSizeChanged(width: Double, height: Double) {
        Log.d(TAG, "onDropDownSizeChanged=$this, $width, $height")
    }

    override fun onDropDownVisible(visible: Boolean) {
        Log.d(TAG, "onDropDownVisible=$this, $visible")
        Log.d(TAG, "view lifecycleOwner=${this.viewPager.findViewTreeLifecycleOwner()}")
    }
}