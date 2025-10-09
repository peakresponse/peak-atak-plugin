package net.peakresponse.android.shared.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import net.peakresponse.android.shared.models.Region
import net.peakresponse.android.shared.models.RegionAgency
import net.peakresponse.android.shared.models.RegionFacility

@Dao
abstract class RegionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMany(records: List<Region>)
}

@Dao
abstract class RegionAgencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMany(records: List<RegionAgency>)
}

@Dao
abstract class RegionFacilityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMany(records: List<RegionFacility>)
}
