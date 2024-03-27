package net.deechael.esjzone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo(name = "keyword") val keyword: String,
    @ColumnInfo(name = "time") var time: String
)
