package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Dispatch(
    @PrimaryKey val id: String,
    val canonicalId: String?,
    val currentId: String?,
    val parentId: String?,
    val secondParentId: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val incidentId: String?,
    val vehicleId: String?,
    val dispatchedAt: Date?,
    val acknowledgedAt: Date?,
)
