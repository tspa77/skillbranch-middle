package ru.skillbranch.gameofthrones.ui.splash

interface SplashContract{
    interface Presenter{
        fun init()
    }
    interface View{
        fun goToMain()
    }
}