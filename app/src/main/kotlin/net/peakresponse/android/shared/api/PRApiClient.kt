package net.peakresponse.android.shared.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

import net.peakresponse.android.shared.models.Agency
import net.peakresponse.android.shared.models.Assignment
import net.peakresponse.android.shared.models.Scene
import net.peakresponse.android.shared.models.User
import net.peakresponse.android.shared.models.Vehicle
import net.peakresponse.android.atak.plugin.BuildConfig
import okhttp3.JavaNetCookieJar
import retrofit2.http.Body
import java.net.CookieManager

class Me(
    @SingleOrList val User: List<User>,
    @SingleOrList val Agency: List<Agency>,
    @SingleOrList val Assignment: List<Assignment>,
    @SingleOrList val Vehicle: List<Vehicle>,
    @SingleOrList val Scene: List<Scene>
)

interface PRApiClientInterface {
    @GET("/api/users/me")
    suspend fun me(): Response<Me>

    @POST("/login")
    suspend fun login(@Body body: Map<String, String>): Response<Void>
}

object PRApiClient {
    private var instance: PRApiClientInterface? = null

    fun getInstance(): PRApiClientInterface {
        if (instance == null) {
            val cookieJar = JavaNetCookieJar(CookieManager())
            val client = OkHttpClient.Builder()
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
            val moshi = Moshi.Builder()
                .add(SingleOrListAdapterFactory)
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val builder = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            instance = builder.create(PRApiClientInterface::class.java)
        }
        return instance!!
    }
}
