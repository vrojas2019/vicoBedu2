package org.bedu.gr.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.bedu.gr.MainActivity
import org.bedu.gr.R
import org.bedu.gr.databinding.ActivityLoginBinding
import org.bedu.gr.databinding.ActivityMainBinding

const val ID_USER = "id"
const val MAIL = "mail"
const val USER_NAME = "name"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnAccess: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnAccess = binding.btnAccess
        txtUser = binding.txtUser
        txtPassword = binding.txtPassword

        val sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        checkLogIn(sharedPreferences)

        btnAccess.setOnClickListener {
            var user = txtUser.text.toString()
            var password = txtPassword.text.toString()
            if (!validEmpty(user,password)){
                //Valid vs BD

                if (validInput(user,password)) {

                    with(sharedPreferences.edit()){
                        putString("idUser","1")
                        putString("user",user)
                        putString("name","Victor Rojas")
                        putString("password",password)
                        putString("active","true")
                        apply()
                    }

                    Ingresar(sharedPreferences)
                }
                else{
                    Toast.makeText(this@LoginActivity, "Datos incorrectos", Toast.LENGTH_LONG).show()
                }
            }
            else{
                AlertDialog.Builder(this).apply {
                    setTitle("Alerta")
                    setMessage("Debes capturar el usuario y contraseña")
                    setPositiveButton("Aceptar") { _, _ ->
                    }
                    show()
                }
            }
        }

    }

    private fun checkLogIn(sharedPreferences: SharedPreferences) {
        if (sharedPreferences.getString("active","") == "true"){
            Ingresar(sharedPreferences)
        }
    }

    private fun Ingresar(sharedPreferences: SharedPreferences){
        val bundle = Bundle()
        bundle.putString(ID_USER,sharedPreferences.getString("idUser","").toString())
        bundle.putString(MAIL,sharedPreferences.getString("user","").toString())
        bundle.putString(USER_NAME,sharedPreferences.getString("name","").toString())
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtras(bundle)
        }
        startActivity(intent)
        finish()
    }

    private fun validEmpty(user: String, password: String) : Boolean {

        if (user.isNullOrEmpty() || password.isNullOrEmpty()){
            return true
        }
        return false
    }

    private fun validInput(user: String, password: String) : Boolean {

        if (user == "123@hotmail.com" && password == "123"){
            return true
        }
        return false
    }
}