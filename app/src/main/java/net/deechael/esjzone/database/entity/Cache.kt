package net.deechael.esjzone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cache(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo(name = "cache_key") val key: String,
    @ColumnInfo(name = "cache_value") var value: String
)