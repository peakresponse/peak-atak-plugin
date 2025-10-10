package net.peakresponse.android.atak.plugin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atak.plugins.impl.PluginLayoutInflater
import com.atakmap.coremap.log.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import net.peakresponse.android.atak.plugin.R
import net.peakresponse.android.shared.PRAppData
import net.peakresponse.android.shared.PRSettings

class IncidentsCellHolder(
    view: View
): RecyclerView.ViewHolder(view) {
    val incidentNumberLabel: TextView
    val addressLabel: TextView
    val timestampLabel: TextView

    init {
        incidentNumberLabel = view.findViewById<TextView>(R.id.incidentNumberLabel)
        addressLabel = view.findViewById<TextView>(R.id.addressLabel)
        timestampLabel = view.findViewById<TextView>(R.id.timestampLabel)
    }
}

class IncidentsFragment(
    private val pluginContext: Context
) : TabFragment() {
    companion object {
        private const val TAG = "net.peakresponse.android.atak.plugin.IncidentsFragment"
    }

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

        val emptyView = view.findViewById<TextView>(R.id.emptyView)
        emptyView.visibility = View.GONE

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = object: RecyclerView.Adapter<IncidentsCellHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): IncidentsCellHolder {
                val view = PluginLayoutInflater.inflate(pluginContext, R.layout.incidents_cell_layout, parent, false)
                return IncidentsCellHolder(view)
            }

            override fun onBindViewHolder(
                holder: IncidentsCellHolder,
                position: Int
            ) {
                holder.incidentNumberLabel.text = "#0000$position"
            }

            override fun getItemCount(): Int {
                return 3
            }
        }
        recyclerView.visibility = View.VISIBLE
        return view
    }

    override fun onStart() {
        super.onStart()
        context?.let {
            val settings = PRSettings(it)
            settings.assignmentId?.let { assignmentId ->
                PRAppData.connectIncidents(it, assignmentId)
            }
            Log.d(TAG, "Getting db reference")
            val db = PRAppData.getDb(it)
            Log.d(TAG, "Getting dao reference")
            val dao = db.getIncidentDao()
            viewLifecycleOwner.lifecycleScope.launch {
                Log.d(TAG, "Querying incidents")
                val incidentsFlow = dao.queryIncidents()
                withContext(Dispatchers.IO) {
                    incidentsFlow.collect {
                        Log.d(TAG, "incidents=$it")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PRAppData.disconnectIncidents()
    }
}
