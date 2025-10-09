package net.peakresponse.android.shared.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.Date
import net.peakresponse.android.shared.models.Agency
import net.peakresponse.android.shared.models.Assignment
import net.peakresponse.android.shared.models.City
import net.peakresponse.android.shared.models.CodeList
import net.peakresponse.android.shared.models.CodeListItem
import net.peakresponse.android.shared.models.CodeListSection
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
import net.peakresponse.android.shared.models.Response
import net.peakresponse.android.shared.models.Scene
import net.peakresponse.android.shared.models.Signature
import net.peakresponse.android.shared.models.Situation
import net.peakresponse.android.shared.models.State
import net.peakresponse.android.shared.models.Time
import net.peakresponse.android.shared.models.User
import net.peakresponse.android.shared.models.Vehicle
import net.peakresponse.android.shared.models.Venue
import net.peakresponse.android.shared.models.Vital

class Converters {
    companion object {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val intListAdapter = moshi.adapter<List<Int>>(
            Types.newParameterizedType(
                List::class.java,
                Integer::class.java
            )
        )
        val stringListAdapter = moshi.adapter<List<String>>(
            Types.newParameterizedType(
                List::class.java,
                String::class.java
            )
        )
        val listAdapter = moshi.adapter<List<Map<String, Any>>>(
            Types.newParameterizedType(
                List::class.java,
                Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
            )
        )
        val mapAdapter = moshi.adapter<Map<String, Any>>(
            Types.newParameterizedType(
                Map::class.java,
                String::class.java,
                Any::class.java
            )
        )
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromJSONIntList(value: String?): List<Int>? {
        return intListAdapter.fromJson(value ?: "null")
    }

    @TypeConverter
    fun intListToJSON(list: List<Int>?): String? {
        return intListAdapter.toJson(list)
    }

    @TypeConverter
    fun fromJSONStringList(value: String?): List<String>? {
        return stringListAdapter.fromJson(value ?: "null")
    }

    @TypeConverter
    fun stringListToJSON(list: List<String>?): String? {
        return stringListAdapter.toJson(list)
    }

    @TypeConverter
    fun fromJSONObjectArray(value: String?): List<Map<String, Any>>? {
        return listAdapter.fromJson(value ?: "null")
    }

    @TypeConverter
    fun objectArrayToJSON(list: List<Map<String, Any>>?): String? {
        return listAdapter.toJson(list)
    }

    @TypeConverter
    fun fromJSONObject(value: String?): Map<String, Any>? {
        return mapAdapter.fromJson(value ?: "null")
    }

    @TypeConverter
    fun objectToJSON(obj: Map<String, Any>?): String? {
        return mapAdapter.toJson(obj)
    }
}

@Database(
    entities = [
        Agency::class,
        Assignment::class,
        City::class,
        CodeList::class,
        CodeListSection::class,
        CodeListItem::class,
        Dispatch::class,
        Disposition::class,
        Event::class,
        Facility::class,
        File::class,
        Form::class,
        History::class,
        Incident::class,
        Medication::class,
        Narrative::class,
        Patient::class,
        Procedure::class,
        Region::class,
        RegionAgency::class,
        RegionFacility::class,
        Report::class,
        Responder::class,
        Response::class,
        Scene::class,
        Signature::class,
        Situation::class,
        State::class,
        Time::class,
        User::class,
        Vehicle::class,
        Venue::class,
        Vital::class
    ], version = 3
)
@TypeConverters(Converters::class)
abstract class PRAppDatabase : RoomDatabase() {
    abstract fun getAgencyDao(): AgencyDao
    abstract fun getAssignmentDao(): AssignmentDao
    abstract fun getCityDao(): CityDao
    abstract fun getCodeListDao(): CodeListDao
    abstract fun getCodeListSectionDao(): CodeListSectionDao
    abstract fun getCodeListItemDao(): CodeListItemDao
    abstract fun getDispatchDao(): DispatchDao
    abstract fun getDispositionDao(): DispositionDao
    abstract fun getEventDao(): EventDao
    abstract fun getFacilityDao(): FacilityDao
    abstract fun getFileDao(): FileDao
    abstract fun getFormDao(): FormDao
    abstract fun getHistoryDao(): HistoryDao
    abstract fun getIncidentDao(): IncidentDao
    abstract fun getMedicationDao(): MedicationDao
    abstract fun getNarrativeDao(): NarrativeDao
    abstract fun getPatientDao(): PatientDao
    abstract fun getProcedureDao(): ProcedureDao
    abstract fun getRegionDao(): RegionDao
    abstract fun getRegionAgencyDao(): RegionAgencyDao
    abstract fun getRegionFacilityDao(): RegionFacilityDao
    abstract fun getReportDao(): ReportDao
    abstract fun getResponderDao(): ResponderDao
    abstract fun getResponseDao(): ResponseDao
    abstract fun getSceneDao(): SceneDao
    abstract fun getSignatureDao(): SignatureDao
    abstract fun getSituationDao(): SituationDao
    abstract fun getStateDao(): StateDao
    abstract fun getTimeDao(): TimeDao
    abstract fun getUserDao(): UserDao
    abstract fun getVehicleDao(): VehicleDao
    abstract fun getVenueDao(): VenueDao
    abstract fun getVitalDao(): VitalDao
}
