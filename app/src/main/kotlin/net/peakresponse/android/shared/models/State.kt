package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class State(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val name: String?,
    val abbr: String?
)
