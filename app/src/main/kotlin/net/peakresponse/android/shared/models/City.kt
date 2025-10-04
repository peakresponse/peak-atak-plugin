package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class City(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val featureName: String?,
    val stateNumeric: String?,
    val stateAlpha: String?,
    val primaryLatitude: Double?,
    val primaryLongitude: Double?
)
