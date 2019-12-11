package ru.skillbranch.gameofthrones.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import ru.skillbranch.gameofthrones.AppConfig.DB_NAME
import ru.skillbranch.gameofthrones.data.local.AppDB
import ru.skillbranch.gameofthrones.data.remote.RetrofitFactory.Companion.getApi
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.splash.SplashContract
import ru.skillbranch.gameofthrones.ui.splash.SplashPresenter

val applicationModule = module(override = true) {
    factory<SplashContract.Presenter> { (view: SplashContract.View) -> SplashPresenter(view) }
//    factory<MainContract.Presenter> { (view: MainContract.View) -> MainPresenter(view) }
    single { getApi() }
    single { RootRepository }
    single { Room.databaseBuilder(androidContext(), AppDB::class.java, DB_NAME).build() }
//    single { (db: AppDB) -> db.charterDao() }
//    single { (db: AppDB) -> db.houseDao() }
}