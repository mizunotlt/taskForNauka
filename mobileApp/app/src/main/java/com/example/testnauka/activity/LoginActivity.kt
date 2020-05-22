package com.example.testnauka.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.testnauka.R
import com.example.testnauka.data.PostLoginResponse
import com.example.testnauka.di.annotation.ApplicationScope
import com.example.testnauka.di.annotation.LoginScope
import com.example.testnauka.di.annotation.RestApiRepositoryScope
import com.example.testnauka.di.module.RepositoryModule
import com.example.testnauka.models.LoginViewModel
import com.google.gson.Gson
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by inject()
    private lateinit var editTextLogin: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        injectDependencies()
        editTextLogin = findViewById(R.id.editTextLogin)
        loginViewModel.responseLiveData.observe(this, Observer<PostLoginResponse> {
            if(it.code == 400){
                createToast(it.message)
            }
            if(it.code == 200){
                createToast(it.message)
                loginViewModel.cancelAllRequests()
                KTP.closeScope(LoginScope::class.java)
                KTP.closeScope(RestApiRepositoryScope::class.java)
                val intent = Intent(this, MainApp::class.java )
                intent.putExtra("position", Gson().toJson(it.positions))
                startActivity(intent)
            }
        })
    }

    private fun injectDependencies() {

        KTP.openScopes(ApplicationScope::class.java)
            .openSubScope(LoginScope::class.java) { scope: Scope ->
                scope.installViewModelBinding<LoginViewModel>(this)
                    .closeOnViewModelCleared(this)
                    .installModules(
                        RepositoryModule()
                    )
            }
            .closeOnDestroy(this)
            .inject(this)

    }

    private fun createToast (text: String){
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.view.setBackgroundColor(Color.GRAY)
        toast.show()
    }

    fun buttonClickEnter(view: View) {
        if(editTextLogin.text.toString() == ""){
            createToast("Введите имя")
        }
        else{
            loginViewModel.postLoginRequest(editTextLogin.text.toString())
        }
    }

}
