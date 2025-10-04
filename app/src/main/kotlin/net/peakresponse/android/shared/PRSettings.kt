package net.peakresponse.android.shared

import android.content.Context
import androidx.core.content.edit

class PRSettings(
    private val context: Context
) {
    companion object {
        const val TAG = "net.peakresponse.android.shared.PRSettings"
    }

    private val prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)

    var userId: String?
        get() = prefs.getString("userId", null)
        set(newValue) {
            prefs.edit {
                putString("userId", newValue)
            }
        }
    var regionId: String?
        get() = prefs.getString("regionId", null)
        set(newValue) {
            prefs.edit {
                putString("regionId", newValue)
            }
        }
    var agencyId: String?
        get() = prefs.getString("agencyId", null)
        set(newValue) {
            prefs.edit {
                putString("agencyId", newValue)
            }
        }
    var assignmentId: String?
        get() = prefs.getString("assignmentId", null)
        set(newValue) {
            prefs.edit {
                putString("assignmentId", newValue)
            }
        }
    var vehicleId: String?
        get() = prefs.getString("vehicleId", null)
        set(newValue) {
            prefs.edit {
                putString("vehicleId", newValue)
            }
        }
    var eventId: String?
        get() = prefs.getString("eventId", null)
        set(newValue) {
            prefs.edit {
                putString("eventId", newValue)
            }
        }
    var sceneId: String?
        get() = prefs.getString("sceneId", null)
        set(newValue) {
            prefs.edit {
                putString("sceneId", newValue)
            }
        }
    var subdomain: String?
        get() = prefs.getString("subdomain", null)
        set(newValue) {
            prefs.edit {
                putString("subdomain", newValue)
            }
        }
    var routedUrl: String?
        get() = prefs.getString("routedUrl", null)
        set(newValue) {
            prefs.edit {
                putString("routedUrl", newValue)
            }
        }
}
