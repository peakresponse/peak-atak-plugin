package net.peakresponse.android.shared

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.runBlocking
import net.peakresponse.android.shared.api.PRPayload
import net.peakresponse.android.shared.api.PRApiClient
import net.peakresponse.android.shared.api.PRWebSocketListener
import net.peakresponse.android.shared.dao.PRAppDatabase
import okhttp3.WebSocket
import retrofit2.Response
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.memberProperties

object PRAppData {
    private const val TAG = "net.peakresponse.android.shared.PRAppData"

    private var db: RoomDatabase? = null

    private var incidentsSocket: WebSocket? = null

    fun getDb(context: Context): RoomDatabase {
        if (db == null) {
            db = Room.databaseBuilder(context, PRAppDatabase::class.java, TAG)
                .fallbackToDestructiveMigration(true)
                .build()
        }
        return db!!
    }

    private val MODELS = arrayOf(
        "Agency", "Assignment", "City", "Event", "Facility", "File", "Form", "History",
        "Medication", "Narrative", "Patient", "Procedure", "Region", "Response", "Situation",
        "State", "Time", "User", "Vehicle", "Venue", "Vital",
        "Disposition", "RegionAgency", "RegionFacility", "Responder", "Signature", "Scene",
        "Incident", "Dispatch", "Report"
    )
    private val PROPERTIES =
        MODELS.map { m -> PRPayload::class.memberProperties.first { it.name === m } }

    suspend fun handlePayload(context: Context, payload: PRPayload?) {
        payload?.let {
            val db = getDb(context)
            for (property in PROPERTIES) {
                val value = property.call(it)
                val getDaoFn =
                    db::class.members.first { it.name == "get${property.name}Dao" } as KFunction<*>
                val dao = getDaoFn.call(db)
                val insertManyFn =
                    dao!!::class.members.first { it.name == "insertMany" } as KFunction<*>
                insertManyFn.callSuspend(dao, value)
            }
        }
    }

    suspend fun me(context: Context): Response<PRPayload> {
        val response = PRApiClient.getInstance(context).me()
        val payload = response.body()
        handlePayload(context, payload)
        payload?.let {
            val settings = PRSettings(context)
            settings.userId = it.User?.first()?.id
            settings.regionId = it.Agency?.first()?.regionId
            settings.agencyId = it.Agency?.first()?.id
            settings.subdomain = it.Agency?.first()?.subdomain
            settings.assignmentId = it.Assignment?.first()?.id
            settings.vehicleId = it.Vehicle?.first()?.id
            settings.sceneId = it.Scene?.first()?.id
        }
        return response
    }

    suspend fun login(context: Context, email: String, password: String): Response<Void> {
        val body = HashMap<String, String>()
        body["email"] = email
        body["password"] = password
        return PRApiClient.getInstance(context).login(body)
    }

    fun connectIncidents(context: Context, assignmentId: String) {
        incidentsSocket?.cancel()
        incidentsSocket =
            PRApiClient.connectIncidents(context, assignmentId, object : PRWebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                    Log.d(TAG, "onOpen")
                }

                override fun onMessage(webSocket: WebSocket, payload: PRPayload?) {
                    runBlocking {
                        handlePayload(context, payload)
                    }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    Log.d(TAG, "onClosed")
                }

                override fun onFailure(
                    webSocket: WebSocket,
                    t: Throwable,
                    response: okhttp3.Response?
                ) {
                    Log.d(TAG, "onFailure t=$t")
                }
            })
    }

    fun disconnectIncidents() {
        incidentsSocket?.cancel()
        incidentsSocket = null
    }
}
