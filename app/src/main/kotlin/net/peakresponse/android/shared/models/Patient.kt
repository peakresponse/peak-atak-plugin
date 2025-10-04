package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Patient(
    @PrimaryKey val id: String,
    val canonicalId: String?,
    val currentId: String?,
    val parentId: String?,
    val secondParentId: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val data: Map<String, Any>?,
    val firstName: String?,
    val lastName: String?,
    val gender: String?,
    val age: String?,
    val ageUnits: String?,
    val dob: String?,
    val location: String?,
    val lat: String?,
    val lng: String?,
    val priority: Int?,
)
