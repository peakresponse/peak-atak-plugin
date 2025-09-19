package net.peakresponse.android.shared.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.http.GET

import net.peakresponse.android.shared.models.Agency
import net.peakresponse.android.shared.models.Assignment
import net.peakresponse.android.shared.models.Scene
import net.peakresponse.android.shared.models.User
import net.peakresponse.android.shared.models.Vehicle
import net.peakresponse.atak.plugin.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AwsCredentials(
    val AccessKeyId: String,
    val SecretAccessKey: String,
    val SessionToken: String,
)

class UserWithCredentials(
    firstName: String?,
    lastName: String?,
    position: String?,
    iconUrl: String?,
    val awsCredentials: AwsCredentials?
): User(firstName, lastName, position, iconUrl)

class Me(
    val User: UserWithCredentials,
    val Agency: Agency,
    val Assignment: Assignment,
    val Vehicle: Vehicle,
    val Scene: Scene
)

interface PRApiClientInterface {
    @GET("/api/users/me")
    suspend fun me(): Response<Me>
}

object PRApiClient {
    private var instance: PRApiClientInterface? = null

    fun getInstance(): PRApiClientInterface {
        if (instance == null) {
            val client = OkHttpClient.Builder()
                .addNetworkInterceptor({ chain ->
                    val request = chain.request()
                    if (request.header("Accept") != null) {
                        return@addNetworkInterceptor chain.proceed(request)
                    }
                    val newRequest = request.newBuilder()
                        .header("Accept", "application/json")
                        .method(request.method, request.body)
                        .build()
                    return@addNetworkInterceptor chain.proceed(newRequest)
                })
                .build()
            val moshi = Moshi.Builder()
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
