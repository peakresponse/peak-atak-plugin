package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class File(
    @PrimaryKey val id: String,
    val canonicalId: String?,
    val currentId: String?,
    val parentId: String?,
    val secondParentId: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val file: String?,
    val fileUrl: String?,
    val metadata: Map<String, Any>?,
    val data: Map<String, Any>?,
)
