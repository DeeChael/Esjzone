package net.deechael.esjzone.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import net.deechael.esjzone.database.entity.SearchHistory

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM searchhistory")
    fun getAll(): List<SearchHistory>

    @Query("SELECT * FROM searchhistory WHERE keyword LIKE :keyword LIMIT 1")
    fun findByKeyword(keyword: String): SearchHistory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg histories: SearchHistory)

    @Update
    fun update(vararg histories: SearchHistory)

    @Delete
    fun delete(vararg histories: SearchHistory)

    @Query("SELECT EXISTS(SELECT * FROM searchhistory WHERE keyword = :keyword)")
    fun exists(keyword: String): Boolean

}