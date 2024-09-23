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

class EditarPersona : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_persona)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiService = ApiClient.getClient().create(GraphQLApiService::class.java)

        val textBox = findViewById<TextInputLayout>(R.id.cedulaEditar)
        val textBox2 = findViewById<TextInputLayout>(R.id.nombre)

        val botonEditar = findViewById<Button>(R.id.botonEditar)

        botonEditar.setOnClickListener {
            val cedula = textBox.editText?.text.toString()
            val nombre = textBox2.editText?.text.toString()

            if (cedula.isNotEmpty() && nombre.isNotEmpty()) {
                val call = apiService.getPersonas()
                call.enqueue(object : Callback<List<Persona>> {
                    override fun onResponse(call: Call<List<Persona>>, response: Response<List<Persona>>) {
                        if (response.isSuccessful) {
                            val personas = response.body() ?: emptyList()

                            val personaAEditar = personas.find { it.cedula == cedula }

                            if (personaAEditar != null) {
                                val personaActualizada = Persona(personaAEditar.id, personaAEditar.cedula, nombre)
                                val personaRequest = PersonaRequest(personaActualizada)

                                val callUpdate = apiService.updatePersona(personaAEditar.id.toString(), personaRequest)

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
                                Toast.makeText(this@EditarPersona, "Persona no encontrada", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@EditarPersona, "Error al obtener personas: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Persona>>, t: Throwable) {
                        Toast.makeText(this@EditarPersona, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                if (cedula.isEmpty()) textBox.error = "Por favor ingrese la cédula"
                if (nombre.isEmpty()) textBox2.error = "Por favor ingrese el nombre"
            }
        }
    }
}
