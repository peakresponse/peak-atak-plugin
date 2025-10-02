package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["id"], unique = true)]
)
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val rowid: Int = 0,
    val id: String,
    val number: String?,
    val callSign: String?,
    val type: String?,
    val createdByAgencyId: String?
)
