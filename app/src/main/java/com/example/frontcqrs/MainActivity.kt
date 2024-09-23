package com.example.frontcqrs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import com.example.frontcqrs.models.Persona
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.frontcqrs.network.ApiClient
import com.example.frontcqrs.network.GraphQLApiService
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiService = ApiClient.getClient().create(GraphQLApiService::class.java)

        val textBox = findViewById<TextInputLayout>(R.id.loginCedula)

        val verNombre = findViewById<Button>(R.id.boton1)

        // Configurar el listener del botón ver nombre
        verNombre.setOnClickListener {
            val id = textBox.editText?.text.toString() // Obtener el ID de la persona del TextInputLayout
2
            // Validar que el ID no esté vacío
            if (id.isNotEmpty()) {
                // Llamada a la API para obtener la persona por ID
                val call = apiService.getPersonaById(id)
                call.enqueue(object : Callback<Persona> {
                    override fun onResponse(call: Call<Persona>, response: Response<Persona>) {
                        if (response.isSuccessful) {
                            // Obtener el objeto Persona de la respuesta
                            val persona = response.body()
                            persona?.let {
                                // Mostrar el nombre de la persona en un Toast
                                Toast.makeText(this@MainActivity, "Nombre: ${it.nombre}", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            // Mostrar mensaje de error si la respuesta no fue exitosa
                            Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Persona>, t: Throwable) {
                        // Manejar errores de conexión
                        Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                // Mostrar mensaje de advertencia si el ID está vacío
                Toast.makeText(this@MainActivity, "Por favor ingrese un ID", Toast.LENGTH_LONG).show()
            }
        }

        val eliminarPersona = findViewById<Button>(R.id.boton3)

        // Configurar el listener del botón eliminar persona
        eliminarPersona.setOnClickListener {
            val id = textBox.editText?.text.toString() // Obtener el ID de la persona del TextInputLayout

            // Validar que el ID no esté vacío
            if (id.isNotEmpty()) {
                // Llamada a la API para eliminar la persona por ID
                val call = apiService.deletePersona(id)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            // Mostrar un mensaje indicando que la persona fue eliminada
                            Toast.makeText(this@MainActivity, "Persona eliminada con éxito", Toast.LENGTH_SHORT).show()
                        } else {
                            // Mostrar mensaje de error si la respuesta no fue exitosa
                            Toast.makeText(this@MainActivity, "Error al eliminar: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Manejar errores de conexión
                        Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Mostrar mensaje de advertencia si el ID está vacío
                textBox.error = "Por favor ingrese un ID válido"
            }
        }

        val crearPersona = findViewById<Button>(R.id.boton7)

        // Configurar el listener para el botón crearPersona
        crearPersona.setOnClickListener {
            // Crear un intent para iniciar la actividad CrearPersona
            val intent = Intent(this, CrearPersona::class.java)
            startActivity(intent)  // Iniciar la actividad
        }

        val editarPersona = findViewById<Button>(R.id.boton2)

        // Configurar el listener para el botón editarPersona
        editarPersona.setOnClickListener {
            // Crear un intent para iniciar la actividad editarPersona
            val intent = Intent(this, EditarPersona::class.java)
            startActivity(intent)  // Iniciar la actividad
        }

        //val verAmigos = findViewById<Button>(R.id.boton4)

        //verAmigos.setOnClickListener{
            //val intent = Intent(this, VerAmigos::class.java)
            //startActivity(intent)
        //}

        val crearAmistad = findViewById<Button>(R.id.boton5)

        // Listener para crear amistad
        crearAmistad.setOnClickListener {
            val intent = Intent(this, CrearAmistad::class.java)
            startActivity(intent)
        }

    }
}

