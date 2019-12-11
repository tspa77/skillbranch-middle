package ru.skillbranch.gameofthrones.ui.splash

import android.os.Handler
import android.util.Log
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.repositories.RootRepository

class SplashPresenter(private val view: SplashContract.View) : SplashContract.Presenter {
    val repo = RootRepository
    private var splashDone = false
    private var requestDone = false

    override fun init() {
        Handler().postDelayed({
            splashDone = true
            goToMain()
        }, AppConfig.SPLASH_DURATION)

        repo.getNeedHouseWithCharters(
            houseNames = *NEED_HOUSES,
            result = {
                repo.insertHouses(it.map{ it.first }) {
                    Log.d("GOT", "Houses inserted")
                }
                it.forEach {
                    val (house, charList) = it
                    repo.insertCharters((charList)){
                        Log.d("GOT", "Characters inserted")
                    }
                }
                requestDone = true
                goToMain()
            })
    }

    private fun goToMain() {
        if(splashDone && requestDone) view.goToMain()
    }
}