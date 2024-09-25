package com.example.frontcqrs

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frontcqrs.models.Persona
import com.example.frontcqrs.models.PersonaRequest
import com.example.frontcqrs.network.ApiClient
import com.example.frontcqrs.network.GraphQLApiService
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearPersona : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_persona)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiServiceWrite = ApiClient.getService2().create(GraphQLApiService::class.java)

        val textBox = findViewById<TextInputLayout>(R.id.cedulaCreate)
        val textBox2 = findViewById<TextInputLayout>(R.id.nombre)

        val botonCrear = findViewById<Button>(R.id.botonCrear)

        botonCrear.setOnClickListener {
            val cedula = textBox.editText?.text.toString()
            val nombre = textBox2.editText?.text.toString()

            if (cedula.isNotEmpty() && nombre.isNotEmpty()) {
                val persona = Persona(cedula = cedula, nombre = nombre)
                val personaRequest = PersonaRequest(persona)

                val call = apiServiceWrite.createPersona(personaRequest)
                call.enqueue(object : Callback<Persona> {
                    override fun onResponse(call: Call<Persona>, response: Response<Persona>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@CrearPersona, "Persona creada exitosamente", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@CrearPersona, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Persona>, t: Throwable) {
                        Toast.makeText(this@CrearPersona, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                if (cedula.isEmpty()) textBox.error = "Por favor ingrese la cédula"
                if (nombre.isEmpty()) textBox2.error = "Por favor ingrese el nombre"
            }
        }
    }
}
