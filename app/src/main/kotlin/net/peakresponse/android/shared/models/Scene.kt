package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["id"], unique = true)]
)
data class Scene(
    @PrimaryKey(autoGenerate = true) val rowid: Int = 0,
    val id: String,
    val name: String?,
    val desc: String?,
    val approxPatientsCount: Int?,
    val approxPriorityPatientsCounts: Array<Int>?,
    val patientsCount: Int?,
    val priorityPatientsCounts: Array<Int>?,
    val isActive: Boolean?,
    val isMCI: Boolean?,
)
