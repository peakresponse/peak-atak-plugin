package net.peakresponse.android.shared.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

class AwsCredentials(
    val AccessKeyId: String,
    val SecretAccessKey: String,
    val SessionToken: String,
)

@Entity(
    indices = [Index(value = ["id"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val rowid: Int = 0,
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val position: String?,
    val iconUrl: String?,
    @Ignore val awsCredentials: AwsCredentials?
)
