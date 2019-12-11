package ru.skillbranch.gameofthrones.data.local

import androidx.room.*
import ru.skillbranch.gameofthrones.data.local.entities.House


@Dao
interface HouseDao {

    @Query("SELECT * FROM house WHERE id = :id")
    suspend fun getHouseById(id: String): House

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(houseList: List<House>)

    @Update
    suspend fun update(house: House)

    @Query("DELETE FROM house")
    suspend fun clearData()

    @Query("SELECT COUNT(*) FROM house")
    suspend fun getCount(): Int

}