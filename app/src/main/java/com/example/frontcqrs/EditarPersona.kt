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

        // Configurar el listener para el botón
        botonEditar.setOnClickListener {
            val cedula = textBox.editText?.text.toString() // Obtener el texto de la cédula
            val nombre = textBox2.editText?.text.toString() // Obtener el texto del nombre

            // Validar que los campos no estén vacíos
            if (cedula.isNotEmpty() && nombre.isNotEmpty()) {
                // Crear un objeto Persona con los datos del formulario
                val persona = Persona(cedula, nombre)
                val personaRequest = PersonaRequest(persona)

                // Hacer la llamada a la API para actualizar la persona
                val call = apiService.updatePersona(cedula, personaRequest)
                call.enqueue(object : Callback<Persona> {
                    override fun onResponse(call: Call<Persona>, response: Response<Persona>) {
                        if (response.isSuccessful) {
                            // Mostrar un mensaje de éxito
                            Toast.makeText(this@EditarPersona, "Persona actualizada exitosamente", Toast.LENGTH_LONG).show()
                        } else {
                            // Mostrar mensaje de error si la respuesta no fue exitosa
                            Toast.makeText(this@EditarPersona, "Error al actualizar: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Persona>, t: Throwable) {
                        // Manejar errores de conexión
                        Toast.makeText(this@EditarPersona, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                // Mostrar mensaje de advertencia si algún campo está vacío
                if (cedula.isEmpty()) textBox.error = "Por favor ingrese la cédula"
                if (nombre.isEmpty()) textBox2.error = "Por favor ingrese el nombre"
            }
        }
    }
}