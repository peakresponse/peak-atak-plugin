package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Venue(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val type: String?,
    val name: String?,
    val address1: String?,
    val address2: String?,
    val cityId: String?,
    val countyId: String?,
    val stateId: String?,
    val zipCode: String?,
    val regionId: String?
)
