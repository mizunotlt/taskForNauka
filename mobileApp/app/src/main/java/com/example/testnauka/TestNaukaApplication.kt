package com.example.testnauka

import android.app.Application
import com.example.testnauka.di.annotation.ApplicationScope
import com.example.testnauka.di.module.RepositoryModule
import com.example.testnauka.repository.ApiRepository
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class TestNaukaApplication: Application(){

    private lateinit var scope: Scope

    override fun onCreate() {
        super.onCreate()

        scope = KTP.openScope(ApplicationScope::class.java)
            .installModules(module {
                bind<Application>().toInstance { this@TestNaukaApplication}
            })
            .openSubScope(ApiRepository::class.java){
                    scope: Scope ->
                scope.installModules(RepositoryModule())
            }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        scope.release()
    }
}