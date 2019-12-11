package ru.skillbranch.gameofthrones.data.local

import androidx.room.*
import ru.skillbranch.gameofthrones.data.local.entities.Charter
import ru.skillbranch.gameofthrones.data.local.entities.CharterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharterItem


@Dao
interface CharterDao {

//    @Query("SELECT id, houseId, name, titles, aliases FROM charter")
//    val getCharterItems: List<CharterItem>

    @Query(
        """
        SELECT c.id, c.name, h.words,c.born, 
                c.died, c.titles, c.aliases,
                h.name as house, c.father, c.mother
        FROM charter AS c 
        LEFT JOIN house AS h 
        ON c.houseId = h.id
        WHERE c.id = :id
    """)
    suspend fun getCharterFull(id: String): CharterFull

    @Query(
        """
        SELECT c.id, h.name as house, c.name, c.titles, c.aliases
        FROM charter AS c 
        LEFT JOIN house AS h 
        ON c.houseId = h.id
        WHERE house = :houseName
    """)
    suspend fun getCharterItemByHouseName(houseName: String): List<CharterItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(charter: List<Charter>)

    @Update
    suspend fun update(charter: Charter)

    @Query("DELETE FROM charter")
    suspend fun clearData()

    @Query("SELECT COUNT(*) FROM charter")
    suspend fun getCount(): Int

}