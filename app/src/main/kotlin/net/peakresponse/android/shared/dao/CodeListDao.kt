package net.peakresponse.android.shared.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import net.peakresponse.android.shared.models.CodeList
import net.peakresponse.android.shared.models.CodeListItem
import net.peakresponse.android.shared.models.CodeListSection

@Dao
abstract class CodeListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMany(records: List<CodeList>)
}

@Dao
abstract class CodeListSectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMany(records: List<CodeListSection>)
}

@Dao
abstract class CodeListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMany(records: List<CodeListItem>)
}
