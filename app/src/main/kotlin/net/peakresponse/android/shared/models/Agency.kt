package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["id"], unique = true)]
)
data class Agency(
    @PrimaryKey(autoGenerate = true) val rowid: Int = 0,
    val id: String,
    val regionId: String?,
    val stateId: String?,
    val stateUniqueId: String?,
    val number: String?,
    val name: String?,
    val isEventsOnly: Boolean?
)
