package com.example.testnauka.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import com.example.testnauka.R
import com.example.testnauka.data.PostRegResponse
import com.example.testnauka.di.annotation.ApplicationScope
import com.example.testnauka.di.annotation.RegistrationScope
import com.example.testnauka.di.module.RepositoryModule
import com.example.testnauka.models.RegistrationViewModel
import com.example.testnauka.utils.Positions
import kotlinx.android.synthetic.main.activity_reg.*
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding

class RegActivity : AppCompatActivity() {

    private val registrationViewModel: RegistrationViewModel  by inject()
    private lateinit var spinnerReg: Spinner
    private lateinit var textEditLogin: EditText
    private var position: Int = 0
    private lateinit var adapterSpinner: ArrayAdapter<Positions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)
        injectDependencies()
        spinnerReg = findViewById(R.id.spinnerPosition)
        textEditLogin = findViewById(R.id.editTextLogin)

        adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, Positions.values())
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerReg.adapter = adapterSpinner
        spinnerReg.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                position = -1
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                position = p2
            }
        }

        registrationViewModel.responseLiveData.observe(this, Observer<PostRegResponse> {
            if(it.code == 400){
                createToast(it.message)
            }
            if(it.code == 200){
                createToast(it.message)
                registrationViewModel.cancelAllRequests()
                this.finish()
            }
        })

    }

    private fun injectDependencies() {

        KTP.openScopes(ApplicationScope::class.java)
            .openSubScope(RegistrationScope::class.java) { scope: Scope ->
                scope.installViewModelBinding<RegistrationViewModel>(this)
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

    fun buttonClickReg(view: View) {

        if(editTextLogin.text.toString() == ""){
            createToast("Введите имя")
        }
        if(position != -1){
            registrationViewModel.postRegistration(editTextLogin.text.toString(), position)
        }

    }

}
