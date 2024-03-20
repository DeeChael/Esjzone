package net.deechael.esjzone.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import net.deechael.esjzone.database.entity.Cache

@Dao
interface CacheDao {

    @Query("SELECT * FROM cache")
    fun getAll(): List<Cache>

    @Query("SELECT * FROM cache WHERE cache_key LIKE :key LIMIT 1")
    fun findByKey(key: String): Cache

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg caches: Cache)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNotExists(vararg caches: Cache)

    @Update
    fun update(vararg caches: Cache)

    @Delete
    fun delete(vararg caches: Cache)

    @Query("SELECT EXISTS(SELECT * FROM cache WHERE cache_key = :key)")
    fun exists(key: String): Boolean

}