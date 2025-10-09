package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Agency(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val subdomain: String?,
    val regionId: String?,
    val stateId: String?,
    val stateUniqueId: String?,
    val number: String?,
    val name: String?,
    val isEventsOnly: Boolean?
)
