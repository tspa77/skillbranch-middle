package ru.skillbranch.gameofthrones.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.skillbranch.gameofthrones.data.local.entities.Charter
import ru.skillbranch.gameofthrones.data.local.entities.House

@Database(
    entities = [
        Charter::class,
        House::class
    ], version = 1
)

@TypeConverters(StringConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun charterDao(): CharterDao
    abstract fun houseDao(): HouseDao
}