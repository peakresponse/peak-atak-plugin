package net.peakresponse.android.shared.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date
import net.peakresponse.android.shared.models.Agency

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

@Database(
    entities = [
        Agency::class
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class PRAppDatabase : RoomDatabase() {
    abstract fun getAgencyDao(): AgencyDao
}
