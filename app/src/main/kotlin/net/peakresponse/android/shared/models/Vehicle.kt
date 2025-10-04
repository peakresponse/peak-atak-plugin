package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Vehicle(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val number: String?,
    val callSign: String?,
    val type: String?,
    val createdByAgencyId: String?
)
