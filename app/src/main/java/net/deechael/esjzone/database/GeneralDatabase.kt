package net.deechael.esjzone.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.deechael.esjzone.database.dao.CacheDao
import net.deechael.esjzone.database.entity.Cache

@Database(entities = [Cache::class], version = 1)
abstract class GeneralDatabase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

}