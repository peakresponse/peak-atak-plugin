package net.peakresponse.android.shared

import com.atakmap.coremap.log.Log
import net.peakresponse.android.shared.api.Me
import net.peakresponse.android.shared.api.PRApiClient
import retrofit2.Response

object PRAppData {
    private const val TAG = "PRAppData"

    suspend fun me(): Response<Me> {
        val response = PRApiClient.getInstance().me()
        Log.d(TAG, "response=$response")
        return response
    }

    suspend fun login(email: String, password: String): Response<Void> {
        val body = HashMap<String, String>()
        body["email"] = email
        body["password"] = password
        return PRApiClient.getInstance().login(body)
    }
}
