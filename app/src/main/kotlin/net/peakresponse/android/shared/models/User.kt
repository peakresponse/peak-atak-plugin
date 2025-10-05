package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

class AwsCredentials(
    val AccessKeyId: String,
    val SecretAccessKey: String,
    val SessionToken: String,
)

@Entity
data class User(
    @PrimaryKey val id: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val firstName: String?,
    val lastName: String?,
    val position: String?,
    val iconUrl: String?,
    @Ignore val awsCredentials: AwsCredentials?
) {
    constructor(
        id: String,
        createdAt: Date?,
        updatedAt: Date?,
        firstName: String?,
        lastName: String?,
        position: String?,
        iconUrl: String?
    ) : this(id, createdAt, updatedAt, firstName, lastName, position, iconUrl, null)
}
