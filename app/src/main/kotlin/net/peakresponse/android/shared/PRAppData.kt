package net.peakresponse.android.shared

import android.content.Context
import com.atakmap.coremap.log.Log
import net.peakresponse.android.shared.api.Me
import net.peakresponse.android.shared.api.PRApiClient
import retrofit2.Response

object PRAppData {
    private const val TAG = "PRAppData"

    suspend fun me(context: Context): Response<Me> {
        val response = PRApiClient.getInstance(context).me()
        Log.d(TAG, "response=$response")
        return response
    }

    suspend fun login(context: Context, email: String, password: String): Response<Void> {
        val body = HashMap<String, String>()
        body["email"] = email
        body["password"] = password
        return PRApiClient.getInstance(context).login(body)
    }
}
