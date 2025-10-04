package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Responder(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val sceneId: String?,
    val role: String?,
    val agencyId: String?,
    val userId: String?,
    val vehicleId: String?,
    val unitNumber: String?,
    val callSign: String?,
    val capability: String?,
    val arrivedAt: Date?,
    val departedAt: Date?,
)
