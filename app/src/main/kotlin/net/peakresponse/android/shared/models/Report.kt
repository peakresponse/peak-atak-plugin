package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Report(
    @PrimaryKey val id: String,
    val canonicalId: String?,
    val currentId: String?,
    val parentId: String?,
    val secondParentId: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val data: Map<String, Any>?,
    val incidentId: String?,
    val filterPriority: String?,
    val pin: String?,
    val sceneId: String?,
    val responseId: String?,
    val timeId: String?,
    val patientId: String?,
    val situationId: String?,
    val historyId: String?,
    val dispositionId: String?,
    val narrativeId: String?,
    val medicationIds: List<String>?,
    val vitalIds: List<String>?,
    val procedureIds: List<String>?,
    val fileIds: List<String>?,
    val signatureIds: List<String>?,
    val predictions: Map<String, Any>?,
    val ringdownId: String?,
    val deletedAt: Date?
)
