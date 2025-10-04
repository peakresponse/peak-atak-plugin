package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Event(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val venueId: String?,
    val name: String?,
    val desc: String?,
    val start: Date?,
    val end: Date?
)
