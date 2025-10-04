package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Region(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val name: String?,
    val routedUrl: String?
)

@Entity
data class RegionAgency(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val regionId: String?,
    val agencyId: String?,
    val agencyName: String?,
    val position: Int?
)

@Entity
data class RegionFacility(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val regionId: String?,
    val facilityId: String?,
    val facilityName: String?,
    val position: Int?
)
