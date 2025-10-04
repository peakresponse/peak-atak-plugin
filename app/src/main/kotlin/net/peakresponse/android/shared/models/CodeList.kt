package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class CodeList(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val fields: List<String>
)

@Entity
data class CodeListSection(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val listId: String?,
    val name: String?,
    val position: Int?,
)

@Entity
data class CodeListItem(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val listId: String?,
    val sectionId: String?,
    val system: String?,
    val code: String?,
    val name: String?
)
