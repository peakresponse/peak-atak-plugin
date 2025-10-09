package net.peakresponse.android.shared.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import net.peakresponse.android.shared.models.City

@Dao
abstract class CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMany(records: List<City>)
}
