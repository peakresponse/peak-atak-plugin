package net.peakresponse.android.shared

import android.content.Context
import com.atakmap.coremap.log.Log
import net.peakresponse.android.shared.api.PRPayload
import net.peakresponse.android.shared.api.PRApiClient
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Response

object PRAppData {
    private const val TAG = "net.peakresponse.android.shared.PRAppData"
    private var incidentsSocket: WebSocket? = null

    suspend fun me(context: Context): Response<PRPayload> {
        val response = PRApiClient.getInstance(context).me()
        Log.d(TAG, "response=$response, body=${response.body()}")
        response.body()?.let {
            Log.d(TAG, "${it.User}")
            Log.d(TAG, "${it.Agency}")
            Log.d(TAG, "${it.Assignment}")
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
            PRApiClient.connectIncidents(context, assignmentId, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {

                }

                override fun onMessage(webSocket: WebSocket, text: String) {

                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {

                }

                override fun onFailure(
                    webSocket: WebSocket,
                    t: Throwable,
                    response: okhttp3.Response?
                ) {

                }
            })
    }
}
