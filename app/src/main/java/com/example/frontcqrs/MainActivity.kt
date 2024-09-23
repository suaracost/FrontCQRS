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
import com.example.frontcqrs.models.Amistad
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

        val apiService = ApiClient.getClient().create(GraphQLApiService::class.java)

        val textBox = findViewById<TextInputLayout>(R.id.loginCedula)

        // Botón para ver el nombre por cédula
        val verNombre = findViewById<Button>(R.id.boton1)
        verNombre.setOnClickListener {
            val cedula = textBox.editText?.text.toString()

            if (cedula.isNotEmpty()) {
                // Llamada a la API para obtener todas las personas
                val call = apiService.getPersonas()
                call.enqueue(object : Callback<List<Persona>> {
                    override fun onResponse(call: Call<List<Persona>>, response: Response<List<Persona>>) {
                        if (response.isSuccessful) {
                            val personas = response.body() ?: emptyList()

                            // Buscar la persona por cédula
                            val personaEncontrada = personas.find { it.cedula == cedula }

                            if (personaEncontrada != null) {
                                // Mostrar el nombre de la persona en un Toast
                                Toast.makeText(this@MainActivity, "Nombre: ${personaEncontrada.nombre}", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@MainActivity, "Persona no encontrada", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Error al obtener personas: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Persona>>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@MainActivity, "Por favor ingrese una cédula", Toast.LENGTH_LONG).show()
            }
        }

        // Eliminar persona basada en la cédula
        val eliminarPersona = findViewById<Button>(R.id.boton3)
        eliminarPersona.setOnClickListener {
            val cedulaPersona = textBox.editText?.text.toString()

            if (cedulaPersona.isNotEmpty()) {
                // Llamada a la API para obtener la persona por cédula
                val call = apiService.getPersonas()
                call.enqueue(object : Callback<List<Persona>> {
                    override fun onResponse(call: Call<List<Persona>>, response: Response<List<Persona>>) {
                        if (response.isSuccessful) {
                            val personas = response.body() ?: emptyList()

                            // Buscar la persona por cédula
                            val personaAEliminar = personas.find { it.cedula == cedulaPersona }

                            if (personaAEliminar != null) {
                                // Si la persona existe, la eliminamos
                                eliminarPersona(apiService, personaAEliminar.cedula)
                            } else {
                                // Si no se encuentra la persona
                                Toast.makeText(this@MainActivity, "Persona no encontrada", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Error al obtener personas", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Persona>>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                textBox.error = "Por favor ingrese una cédula válida"
            }
        }

        val crearPersona = findViewById<Button>(R.id.boton7)
        crearPersona.setOnClickListener {
            val intent = Intent(this, CrearPersona::class.java)
            startActivity(intent)
        }

        val editarPersona = findViewById<Button>(R.id.boton2)
        editarPersona.setOnClickListener {
            val intent = Intent(this, EditarPersona::class.java)
            startActivity(intent)
        }

        val verAmigos = findViewById<Button>(R.id.boton4)
        verAmigos.setOnClickListener {
            val intent = Intent(this, MostrarAmigos::class.java)
            startActivity(intent)
        }

        val crearAmistad = findViewById<Button>(R.id.boton5)
        crearAmistad.setOnClickListener {
            val intent = Intent(this, CrearAmistad::class.java)
            startActivity(intent)
        }

        val eliminarAmigo = findViewById<Button>(R.id.boton6) // Botón para eliminar amigo

        eliminarAmigo.setOnClickListener {
            val cedulaAmigo = textBox.editText?.text.toString() // Obtener la cédula del amigo a eliminar

            if (cedulaAmigo.isNotEmpty()) {
                // Llamada a la API para obtener todas las amistades
                val call = apiService.getAmistades()
                call.enqueue(object : Callback<List<Amistad>> {
                    override fun onResponse(call: Call<List<Amistad>>, response: Response<List<Amistad>>) {
                        if (response.isSuccessful) {
                            val amistades = response.body() ?: emptyList()

                            // Encontrar la amistad que tiene la cédula ingresada
                            val amistadAEliminar = amistades.find {
                                it.cedula_persona2 == cedulaAmigo
                            }

                            if (amistadAEliminar != null) {
                                // Si se encuentra la amistad, la eliminamos usando su ID
                                eliminarAmistad(apiService, amistadAEliminar.id)
                            } else {
                                // Si no se encuentra la amistad, mostrar mensaje
                                Toast.makeText(this@MainActivity, "Amistad no encontrada", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Error al obtener amistades", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Amistad>>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                textBox.error = "Por favor ingrese una cédula válida"
            }
        }
    }

    // Método para eliminar una amistad por su ID
    private fun eliminarAmistad(apiService: GraphQLApiService, idAmistad: Int) {
        val call = apiService.deleteAmistad(idAmistad.toString())
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Amistad eliminada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error al eliminar amistad", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Método para eliminar una persona por su cédula
    private fun eliminarPersona(apiService: GraphQLApiService, cedula: String) {
        val call = apiService.deletePersona(cedula)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Persona eliminada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    // Mostrar el código de error y el cuerpo de la respuesta para obtener más detalles
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@MainActivity, "Error al eliminar persona: ${response.code()} - $errorMessage", Toast.LENGTH_SHORT).show()

                    // Imprimir detalles en el Logcat
                    android.util.Log.e("EliminarPersona", "Error: ${response.code()}, Response: $errorMessage")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                android.util.Log.e("EliminarPersona", "Fallo en la conexión: ${t.message}")
            }
        })
    }
}