package ru.skillbranch.gameofthrones.repositories

import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import ru.skillbranch.gameofthrones.AppConfig.MAX_REQUEST
import ru.skillbranch.gameofthrones.data.getId
import ru.skillbranch.gameofthrones.data.local.AppDB
import ru.skillbranch.gameofthrones.data.local.entities.CharterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharterItem
import ru.skillbranch.gameofthrones.data.remote.Api
import ru.skillbranch.gameofthrones.data.remote.res.CharterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.data.toLocalCharList
import ru.skillbranch.gameofthrones.data.toLocalHouseList

object RootRepository : KoinComponent {
    val api: Api by inject()
    val db: AppDB by inject()
    val charDao = db.charterDao()
    val housesDao = db.houseDao()
    val mainScope = CoroutineScope(Dispatchers.Main)

    /**
     * Получение данных о всех домах
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getAllHouses(result: (houses: List<HouseRes>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // need to coding
                // recursively request
            } catch (e: Exception){ Log.e("GOT", e.localizedMessage!!) }
        }
    }

    /**
     * Получение данных о требуемых домах по их полным именам (например фильтрация всех домов)
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouses(vararg houseNames: String, result: (houses: List<HouseRes>) -> Unit) {
        //TODO implement me
    }

    /**
     * Получение данных о требуемых домах по их полным именам и персонажах в каждом из домов
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о доме и персонажей в нем (Дом - Список Персонажей в нем)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouseWithCharters(
        vararg houseNames: String,
        result: (houses: List<Pair<HouseRes, List<CharterRes>>>) -> Unit) {
        mainScope.launch {
            try {
                result(
                    pullHouses(*houseNames)
                        .map { it to pullChar(it.swornMembers) }
                        .toList())
            } catch (e: Exception) { Log.e("GOT", e.localizedMessage!!) }
        }
    }

    //   below 2different realization
    private suspend fun pullHouses(vararg houseNames: String): List<HouseRes> = coroutineScope {
        houseNames
            .asFlow()
            .flatMapMerge(concurrency = MAX_REQUEST) { houseName ->
                flow { emit(api.getHouseByName(houseName).first()) } }
            .toList()
    }

    private suspend fun pullChar(charlist: List<String>) = coroutineScope {
            charlist
                .map{ charUrl -> async { api.getCharById(charUrl.getId()) } }
                .map { it.await() }
                .toList()
    }

    /**
     * Запись данных о домах в DB
     * @param houses - Список персонажей (модель HouseRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertHouses(houses: List<HouseRes>, complete: () -> Unit) {
        mainScope.launch {
            try {
                housesDao.insert(houses.toLocalHouseList())
                complete()
                Log.d("GOT", "Insert ${houses.size} houses")
            } catch (e: Exception) { Log.e("GOT", e.localizedMessage!!) }
        }

    }

    /**
     * Запись данных о пересонажах в DB
     * @param charters - Список персонажей (модель CharterRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertCharters(charters: List<CharterRes>, complete: () -> Unit) {
        mainScope.launch {
            try {
                charDao.insert(charters.toLocalCharList())
                Log.d("GOT", "Insert ${charters.size} chars")
                complete()
            } catch (e: Exception) { Log.e("GOT", e.localizedMessage!!) }
        }

    }

    /**
     * При вызове данного метода необходимо выполнить удаление всех записей в db
     * @param complete - колбек о завершении очистки db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun dropDb(complete: () -> Unit) {
        mainScope.launch {
            try {
                charDao.clearData()
                housesDao.clearData()
                complete()
            } catch (e: Exception) { Log.e("GOT", e.localizedMessage!!) }
        }
    }

    /**
     * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
     * дома - смотри модель CharterItem
     * @param name - краткое имя дома (его первычный ключ)
     * @param result - колбек содержащий в себе список краткой информации о персонажах дома
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findChartersByHouseName(name: String, result: (charters: List<CharterItem>) -> Unit) {
        mainScope.launch {
            try {
                result(charDao.getCharterItemByHouseName(name))
            } catch (e: Exception) { Log.e("GOT", e.localizedMessage!!) }
        }
    }

    /**
     * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
     * и его родственных отношения - смотри модель CharterFull
     * @param id - идентификатор персонажа
     * @param result - колбек содержащий в себе полную информацию о персонаже
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharterFullById(id: String, result: (charter: CharterFull) -> Unit) {
        mainScope.launch {
            try {
                result(charDao.getCharterFull(id))
            } catch (e: Exception) { Log.e("GOT", e.localizedMessage!!) }
        }
    }

    /**
     * Метод возвращет true если в базе нет ни одной записи, иначе false
     * @param result - колбек о завершении очистки db
     */
    fun isNeedUpdate(result: (isNeed: Boolean) -> Unit) {
        mainScope.launch {
            try {
                result(charDao.getCount() == 0 && housesDao.getCount() == 0)
            } catch (e: Exception) { Log.e("GOT", e.localizedMessage!!) }
        }
    }
}