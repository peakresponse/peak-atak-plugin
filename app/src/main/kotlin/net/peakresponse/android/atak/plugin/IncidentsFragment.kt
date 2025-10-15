package net.peakresponse.android.atak.plugin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atak.plugins.impl.PluginLayoutInflater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.peakresponse.android.atak.plugin.components.TabFragment

import net.peakresponse.android.shared.PRAppData
import net.peakresponse.android.shared.PRSettings
import net.peakresponse.android.shared.models.IncidentWithScene
import java.text.SimpleDateFormat
import java.util.Locale

val dateFormat = SimpleDateFormat("MMM, d, YYYY  hh:mm a", Locale.US)

class IncidentCellHolder(
    view: View
) : RecyclerView.ViewHolder(view) {
    companion object {
        const val TAG = "net.peakresponse.android.atak.plugin.IncidentCellHolder"
    }

    val contentView: View
    val incidentNumberLabel: TextView
    val addressLabel: TextView
    val timestampLabel: TextView

    init {
        contentView = view.findViewById<View>(R.id.contentView)
        incidentNumberLabel = view.findViewById<TextView>(R.id.incidentNumberLabel)
        addressLabel = view.findViewById<TextView>(R.id.addressLabel)
        timestampLabel = view.findViewById<TextView>(R.id.timestampLabel)
    }

    fun bind(item: IncidentWithScene) {
        incidentNumberLabel.text = "#${item.incident.number}"
        addressLabel.text = item.sceneWithAddress?.address ?: ""
        item.incident.createdAt?.let { createdAt ->
            timestampLabel.text = dateFormat.format(createdAt)
        } ?: run {
            timestampLabel.text = ""
        }
    }
}

class IncidentWithSceneDiffer : DiffUtil.ItemCallback<IncidentWithScene>() {
    override fun areItemsTheSame(oldItem: IncidentWithScene, newItem: IncidentWithScene): Boolean {
        return oldItem.incident.id == newItem.incident.id
    }

    override fun areContentsTheSame(
        oldItem: IncidentWithScene,
        newItem: IncidentWithScene
    ): Boolean {
        return oldItem.incident.updatedAt !== newItem.incident.updatedAt || oldItem.sceneWithAddress?.scene?.updatedAt !== newItem.sceneWithAddress?.scene?.updatedAt
    }
}

class IncidentsAdapter(
    val pluginContext: Context
) : ListAdapter<IncidentWithScene, IncidentCellHolder>(IncidentWithSceneDiffer()) {
    companion object {
        private const val TAG = "net.peakresponse.android.atak.plugin.IncidentsAdapter"
    }

    var onIncidentSelected: ((incidentId: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentCellHolder {
        val view = PluginLayoutInflater.inflate(
            pluginContext,
            R.layout.incidents_cell_layout,
            parent,
            false
        )
        return IncidentCellHolder(view)
    }

    override fun onBindViewHolder(holder: IncidentCellHolder, position: Int) {
        val incidentWithScene = getItem(position)
        val incidentId = incidentWithScene.incident.id
        holder.bind(incidentWithScene)
        holder.itemView.setOnClickListener { view ->
            onIncidentSelected?.invoke(incidentId)
        }
    }
}

class IncidentsFragment(
    private val pluginContext: Context
) : TabFragment() {
    companion object {
        private const val TAG = "net.peakresponse.android.atak.plugin.IncidentsFragment"
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: View
    private lateinit var incidentsAdapter: IncidentsAdapter
    private lateinit var recyclerView: RecyclerView

    var onIncidentSelected: ((incidentId: String) -> Unit)? = null

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

        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        emptyView = view.findViewById<TextView>(R.id.emptyView)

        incidentsAdapter = IncidentsAdapter(pluginContext)
        incidentsAdapter.onIncidentSelected = { incidentId ->
            onIncidentSelected?.invoke(incidentId)
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = incidentsAdapter
        return view
    }

    override fun onStart() {
        super.onStart()
        context?.let {
            val settings = PRSettings(it)
            settings.assignmentId?.let { assignmentId ->
                PRAppData.connectIncidents(it, assignmentId)
            }
            val db = PRAppData.getDb(it)
            val dao = db.getIncidentDao()
            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val incidentsFlow = dao.getActiveMciIncidentsWithScenes()
                        .distinctUntilChanged()
                        .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    incidentsFlow.collect { incidents ->
                        withContext(Dispatchers.Main) {
                            progressBar.visibility = View.GONE
                            if (incidents.size > 0) {
                                emptyView.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                            } else {
                                emptyView.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
                            }
                            incidentsAdapter.submitList(incidents)
                        }
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
