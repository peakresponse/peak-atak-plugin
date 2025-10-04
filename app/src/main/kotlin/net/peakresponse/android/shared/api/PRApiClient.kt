package net.peakresponse.android.shared.api

import android.content.Context
import java.net.CookieManager
import java.util.Date
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import net.gotev.cookiestore.SharedPreferencesCookieStore
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

import net.peakresponse.android.atak.plugin.BuildConfig
import net.peakresponse.android.shared.models.Agency
import net.peakresponse.android.shared.models.Assignment
import net.peakresponse.android.shared.models.Scene
import net.peakresponse.android.shared.models.User
import net.peakresponse.android.shared.models.Vehicle
import net.peakresponse.android.shared.models.City
import net.peakresponse.android.shared.models.Dispatch
import net.peakresponse.android.shared.models.Disposition
import net.peakresponse.android.shared.models.Event
import net.peakresponse.android.shared.models.Facility
import net.peakresponse.android.shared.models.File
import net.peakresponse.android.shared.models.Form
import net.peakresponse.android.shared.models.History
import net.peakresponse.android.shared.models.Incident
import net.peakresponse.android.shared.models.Medication
import net.peakresponse.android.shared.models.Narrative
import net.peakresponse.android.shared.models.Patient
import net.peakresponse.android.shared.models.Procedure
import net.peakresponse.android.shared.models.Region
import net.peakresponse.android.shared.models.RegionAgency
import net.peakresponse.android.shared.models.RegionFacility
import net.peakresponse.android.shared.models.Report
import net.peakresponse.android.shared.models.Responder
import net.peakresponse.android.shared.models.Signature
import net.peakresponse.android.shared.models.Situation
import net.peakresponse.android.shared.models.State
import net.peakresponse.android.shared.models.Time
import net.peakresponse.android.shared.models.Venue
import net.peakresponse.android.shared.models.Vital
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.CookiePolicy

class PRPayload(
    @SingleOrList val Agency: List<Agency>?,
    @SingleOrList val Assignment: List<Assignment>?,
    @SingleOrList val City: List<City>?,
    @SingleOrList val Dispatch: List<Dispatch>?,
    @SingleOrList val Disposition: List<Disposition>?,
    @SingleOrList val Event: List<Event>?,
    @SingleOrList val Facility: List<Facility>?,
    @SingleOrList val File: List<File>?,
    @SingleOrList val Form: List<Form>?,
    @SingleOrList val History: List<History>?,
    @SingleOrList val Incident: List<Incident>?,
    @SingleOrList val Medication: List<Medication>?,
    @SingleOrList val Narrative: List<Narrative>?,
    @SingleOrList val Patient: List<Patient>?,
    @SingleOrList val Procedure: List<Procedure>?,
    @SingleOrList val Region: List<Region>?,
    @SingleOrList val RegionAgency: List<RegionAgency>?,
    @SingleOrList val RegionFacility: List<RegionFacility>?,
    @SingleOrList val Response: List<net.peakresponse.android.shared.models.Response>?,
    @SingleOrList val Responder: List<Responder>?,
    @SingleOrList val Report: List<Report>?,
    @SingleOrList val Scene: List<Scene>?,
    @SingleOrList val Signature: List<Signature>?,
    @SingleOrList val Situation: List<Situation>?,
    @SingleOrList val State: List<State>?,
    @SingleOrList val Time: List<Time>?,
    @SingleOrList val User: List<User>?,
    @SingleOrList val Vehicle: List<Vehicle>?,
    @SingleOrList val Venue: List<Venue>?,
    @SingleOrList val Vital: List<Vital>?,
)

interface PRApiClientInterface {
    @GET("/api/users/me")
    suspend fun me(): Response<PRPayload>

    @POST("/login")
    suspend fun login(@Body body: Map<String, String>): Response<Void>
}

object PRApiClient {
    private const val TAG = "net.peakresponse.android.shared.api.PRApiClient"
    private var client: OkHttpClient? = null
    private var instance: PRApiClientInterface? = null

    fun getClient(context: Context): OkHttpClient {
        if (client == null) {
            val cookieJar = JavaNetCookieJar(
                CookieManager(
                    SharedPreferencesCookieStore(context, TAG),
                    CookiePolicy.ACCEPT_ALL
                )
            )
            client = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addNetworkInterceptor({ chain ->
                    var request = chain.request()
                    if (request.header("Accept") == null) {
                        request = request.newBuilder()
                            .header("Accept", "application/json")
                            .method(request.method, request.body)
                            .build()
                    }
                    if (request.header("Content-Type") == null) {
                        request = request.newBuilder()
                            .header("Content-Type", "application/json")
                            .method(request.method, request.body)
                            .build()
                    }
                    return@addNetworkInterceptor chain.proceed(request)
                })
                .build()
        }
        return client!!
    }

    fun getInstance(context: Context): PRApiClientInterface {
        if (instance == null) {
            val moshi = Moshi.Builder()
                .add(SingleOrListAdapterFactory)
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val builder = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(getClient(context))
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            instance = builder.create(PRApiClientInterface::class.java)
        }
        return instance!!
    }

    fun connectIncidents(
        context: Context,
        assignmentId: String,
        listener: WebSocketListener
    ): WebSocket {
        val request = Request.Builder()
            .url(
                "${BuildConfig.API_URL}/incidents?assignmentId=${assignmentId}".replace(
                    "https://",
                    "wss://"
                )
            )
            .build()
        val client = getClient(context)
        return client.newWebSocket(request, listener)
    }
}
