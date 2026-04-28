package com.example.budgettracker

import android.os.Bundle
import android.widge.widget
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val etUsername=
            findViewById<EditText>(R.id.etUsername)

        val etPassword=
            findViewById<EditText>(R.id.etPassword)
        val btnLogin=
            findViewById<Buttontton>(R.id.btnLogin)

        btnLogin.setOnClickListener{
            val username=
                etUsername.text.toString()
            val password=
                etPassword.text.toString()

            if (username== "admin")
                    password == ("1234")
            {
                        Toast.makeText(this,"Login successful", Toast.Length_SHORT).show()
                   starActivity(Intent(this,MainActivity::class.java))
            finish(

         else {
                Toast.makeText(this, "Invalid Login", Toast.Length_SHORT).show()
            }
        }
        }



   enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}