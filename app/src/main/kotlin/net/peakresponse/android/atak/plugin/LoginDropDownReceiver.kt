package net.peakresponse.android.atak.plugin

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.atak.plugins.impl.PluginLayoutInflater
import com.atakmap.android.dropdown.DropDownReceiver
import com.atakmap.android.ipc.AtakBroadcast
import com.atakmap.android.maps.MapView
import com.atakmap.coremap.log.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import net.peakresponse.android.shared.PRAppData
import net.peakresponse.android.atak.plugin.R

class LoginDropDownReceiver(
    mapView: MapView,
    private val pluginContext: Context
) : DropDownReceiver(mapView), View.OnClickListener {

    companion object {
        const val TAG = "LoginDropDownReceiver"
        public const val SET_AUTHENTICATED = "net.peakresponse.android.atak.SET_AUTHENTICATED"
    }

    private val view: View
    private val emailField: EditText
    private val passwordField: EditText
    private val submitButton: Button
    private val spinner: ProgressBar

    init {
        view = PluginLayoutInflater.inflate(pluginContext, R.layout.login_layout, null)
        emailField = view.findViewById<EditText>(R.id.emailField)
        passwordField = view.findViewById<EditText>(R.id.passwordField)
        submitButton = view.findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener(this)
        spinner = view.findViewById<ProgressBar>(R.id.progressBar)
    }

    fun show() {
        showDropDown(view, THIRD_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT)
    }

    override fun disposeImpl() {

    }

    override fun onReceive(context: Context?, intent: Intent) {

    }

    override fun onClick(view: View?) {
        Log.d(TAG, "onClick view=${view.toString()}")
        if (view == submitButton) {
            Log.d(TAG, "Submit clicked with ${emailField.text}, ${passwordField.text}")
            emailField.error = null
            passwordField.error = null
            submitButton.visibility = View.GONE
            spinner.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val response =
                    PRAppData.login(emailField.text.toString(), passwordField.text.toString())
                if (response.isSuccessful) {
                    val response = PRAppData.me()
                    if (response.isSuccessful) {
                        Log.d(TAG, "Success Response = $response")
                        AtakBroadcast.getInstance().sendBroadcast(
                            Intent()
                                .setAction(SET_AUTHENTICATED)
                        )
                        return@launch
                    }
                }
                withContext(Dispatchers.Main) {
                    submitButton.visibility = View.VISIBLE
                    spinner.visibility = View.GONE
                    emailField.error = pluginContext.getString(R.string.invalid_credentials)
                    passwordField.error = pluginContext.getString(R.string.invalid_credentials)
                }
            }
        }
    }
}
