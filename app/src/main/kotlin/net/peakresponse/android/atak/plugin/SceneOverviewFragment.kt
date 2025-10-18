package net.peakresponse.android.atak.plugin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atak.plugins.impl.PluginLayoutInflater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.peakresponse.android.atak.plugin.components.TabFragment
import net.peakresponse.android.shared.models.Scene

val COUNT_TEXTVIEW_IDS = arrayOf(
    arrayOf(
        R.id.approxPatientCount,
        R.id.approxPatientCountRed,
        R.id.approxPatientCountYellow,
        R.id.approxPatientCountGreen,
        null,
        R.id.approxPatientCountZebra
    ),
    arrayOf(
        R.id.patientCount,
        R.id.patientCountRed,
        R.id.patientCountYellow,
        R.id.patientCountGreen,
        null,
        R.id.patientCountZebra
    ),
    arrayOf(
        R.id.transpPatientCount,
        R.id.transpPatientCountRed,
        R.id.transpPatientCountYellow,
        R.id.transpPatientCountGreen,
        null,
        R.id.transpPatientCountZebra
    ),
)

class SceneOverviewFragment(
    private val pluginContext: Context
) : TabFragment() {
    override fun getTabTitle(): String {
        return pluginContext.getString(R.string.tab_patient_counts)
    }

    private lateinit var countTextViews: Array<Array<TextView?>>
    private var scene: Scene? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            PluginLayoutInflater.inflate(
                pluginContext,
                R.layout.scene_overview_layout,
                container,
                false
            )

        val countTextViews = Array<Array<TextView?>>(3) { arrayOfNulls(6) }
        for ((index, ids) in COUNT_TEXTVIEW_IDS.withIndex()) {
            val textViews = countTextViews[index]
            for ((index, id) in ids.withIndex()) {
                id?.let { id ->
                    textViews[index] = view.findViewById<TextView>(id)
                }
            }
        }
        this.countTextViews = countTextViews
        scene?.let { scene ->
            setScene(scene)
        }

        return view
    }

    fun setScene(scene: Scene) {
        if (::countTextViews.isInitialized) {
            countTextViews[0][0]?.text = "${scene.approxPatientsCount ?: "-"}"
            scene.approxPriorityPatientsCounts?.let { approxPriorityPatientsCounts ->
                for ((index, value) in approxPriorityPatientsCounts.withIndex()) {
                    if (index < 5) {
                        countTextViews[0][index + 1]?.text = "$value"
                    }
                }
            }
            countTextViews[1][0]?.text = "${scene.patientsCount ?: "-"}"
            scene.priorityPatientsCounts?.let { priorityPatientsCounts ->
                for ((index, value) in priorityPatientsCounts.withIndex()) {
                    if (index < 5) {
                        countTextViews[1][index + 1]?.text = "$value"
                    }
                }
            }
            this.scene = null
        } else {
            this.scene = scene
        }
    }
}
