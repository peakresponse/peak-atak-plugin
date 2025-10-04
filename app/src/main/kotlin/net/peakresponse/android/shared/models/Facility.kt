package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Facility(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val type: String?,
    val name: String?,
    val locationCode: String?,
    val unit: String?,
    val address: String?,
    val cityId: String?,
    val stateId: String?,
    val zip: String?,
    val county: String?,
    val lat: String?,
    val lng: String?,
    val inventory: Map<String, Any>?
)
