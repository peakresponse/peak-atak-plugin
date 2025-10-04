package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Incident(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val createdById: String?,
    val eventId: String?,
    val psapId: String?,
    val sceneId: String?,
    val number: String?,
    val sort: Long?,
    val calledAt: Date?,
    val dispatchNotifiedAt: Date?,
    val reportsCount: Int?
)
