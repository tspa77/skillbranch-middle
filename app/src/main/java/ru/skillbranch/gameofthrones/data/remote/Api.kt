package ru.skillbranch.gameofthrones.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.skillbranch.gameofthrones.data.remote.res.CharterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

interface Api {

    @GET("houses")
    suspend fun getAllHouses(): List<HouseRes>

    @GET("houses")
    suspend fun getHouseByName(@Query("name") name: String): List<HouseRes>

    @GET("characters")
    suspend fun getAllCharacters(): List<CharterRes>

    @GET("characters/{id}")
    suspend fun getCharById(@Path("id") id: String): CharterRes
}