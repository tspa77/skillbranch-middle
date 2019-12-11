package ru.skillbranch.gameofthrones.data

import ru.skillbranch.gameofthrones.data.local.entities.Charter
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.data.remote.res.CharterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

fun CharterRes.toLocal(): Charter {
    return with(this) {
        Charter(
            url.substringAfterLast("/"),
            name,
            gender,
            culture,
            born,
            died,
            titles,
            aliases,
            father,
            mother,
            spouse,
            allegiances.first().getId()
        )
    }
}

fun List<CharterRes>.toLocalCharList(): List<Charter> = this.map { it.toLocal() }

fun HouseRes.toLocal(): House {
    return with(this) {
        House(
            url.substringAfterLast("/"),
            name,
            region,
            coatOfArms,
            words,
            titles,
            seats,
            currentLord,
            heir,
            overlord,
            founded,
            founder,
            diedOut,
            ancestralWeapons
        )
    }
}

fun List<HouseRes>.toLocalHouseList(): List<House> = this.map { it.toLocal() }

fun String.getId(): String = this.substringAfterLast("/")