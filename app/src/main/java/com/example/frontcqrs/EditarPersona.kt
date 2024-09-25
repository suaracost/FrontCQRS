package com.example.frontcqrs

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.frontcqrs.R
import com.example.frontcqrs.models.Persona
import com.example.frontcqrs.network.ApiClient
import com.example.frontcqrs.network.GraphQLApiService
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarPersona : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_persona)

        val apiService = ApiClient.getClient().create(GraphQLApiService::class.java)

        val textBox = findViewById<TextInputLayout>(R.id.cedulaEditar)
        val textBox2 = findViewById<TextInputLayout>(R.id.nombre)

        val botonEditar = findViewById<Button>(R.id.botonEditar)

        botonEditar.setOnClickListener {
            val cedula = textBox.editText?.text.toString()
            val nombreNuevo = textBox2.editText?.text.toString()

            if (cedula.isNotEmpty() && nombreNuevo.isNotEmpty()) {
                // Crear el body con el nuevo nombre
                val body = HashMap<String, Any>()
                val personaBody = HashMap<String, String>()
                personaBody["nombre"] = nombreNuevo
                body["persona"] = personaBody

                // Llamar al API para actualizar la persona
                val callUpdate = apiService.updatePersona(cedula, body)
                callUpdate.enqueue(object : Callback<Persona> {
                    override fun onResponse(call: Call<Persona>, response: Response<Persona>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EditarPersona, "Persona actualizada exitosamente", Toast.LENGTH_LONG).show()
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                            Toast.makeText(this@EditarPersona, "Error al actualizar: ${response.code()} - $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Persona>, t: Throwable) {
                        Toast.makeText(this@EditarPersona, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                if (cedula.isEmpty()) textBox.error = "Por favor ingrese la cédula"
                if (nombreNuevo.isEmpty()) textBox2.error = "Por favor ingrese el nombre"
            }
        }
    }
}
