package ru.skillbranch.gameofthrones.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.lang.reflect.Array
import java.util.stream.Collectors

class StringConverter{

    @RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun fromList(list: List<String>) = list.stream().collect(Collectors.joining(","))

    @RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun toList(string: String) = string.split(",")
}
