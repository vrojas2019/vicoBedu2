package org.bedu.gr.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.bedu.gr.MainActivity
import org.bedu.gr.R

const val ID_USER = "id"
const val USER_NAME = "name"

class LoginActivity : AppCompatActivity() {
    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnAccess: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnAccess = findViewById(R.id.btnAccess)
        txtUser = findViewById(R.id.txtUser)
        txtPassword = findViewById(R.id.txtPassword)

        btnAccess.setOnClickListener {
            var user = txtUser.text.toString()
            var password = txtPassword.text.toString()
            if (!validEmpty(user,password)){
                //Valid vs BD
                if (validInput(user,password)) {
                    val bundle = Bundle()
                    bundle.putString(ID_USER,"1")
                    bundle.putString(USER_NAME,user)
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtras(bundle)
                    }
                    startActivity(intent)
                    finish()
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

    private fun validEmpty(user: String, password: String) : Boolean {

        if (user.isNullOrEmpty() || password.isNullOrEmpty()){
            return true
        }
        return false
    }

    private fun validInput(user: String, password: String) : Boolean {

        if (user == "vic" && password == "vic"){
            return true
        }
        return false
    }
}