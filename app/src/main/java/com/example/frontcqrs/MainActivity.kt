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

        val verNombre = findViewById<Button>(R.id.boton1)
        verNombre.setOnClickListener {
            val cedula = textBox.editText?.text.toString()

            if (cedula.isNotEmpty()) {
                val call = apiService.getPersonas()
                call.enqueue(object : Callback<List<Persona>> {
                    override fun onResponse(call: Call<List<Persona>>, response: Response<List<Persona>>) {
                        if (response.isSuccessful) {
                            val personas = response.body() ?: emptyList()

                            val personaEncontrada = personas.find { it.cedula == cedula }

                            if (personaEncontrada != null) {
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

        val eliminarPersona = findViewById<Button>(R.id.boton3)
        eliminarPersona.setOnClickListener {
            val cedulaPersona = textBox.editText?.text.toString()

            if (cedulaPersona.isNotEmpty()) {
                eliminarPersonaPorCedula(apiService, cedulaPersona) // Ahora llamamos a eliminar por cédula
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

        val eliminarAmigo = findViewById<Button>(R.id.boton6)
        eliminarAmigo.setOnClickListener {
            val cedulaPersona1 = "123456789"  // Cédula de la persona que está haciendo la solicitud
            val cedulaAmigo = textBox.editText?.text.toString()  // Cédula de la otra persona

            if (cedulaAmigo.isNotEmpty()) {
                eliminarAmistad(apiService, cedulaPersona1, cedulaAmigo)
            } else {
                textBox.error = "Por favor ingrese una cédula válida"
            }
        }

    }

    private fun eliminarAmistad(apiService: GraphQLApiService, cedulaPersona1: String, cedulaPersona2: String) {
        // Crear el cuerpo de la solicitud
        val body = HashMap<String, String>()
        body["cedula_persona2"] = cedulaPersona2

        // Llamar a la API de eliminación
        val call = apiService.deleteAmistad(cedulaPersona1, body)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Amistad eliminada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@MainActivity, "Error al eliminar amistad: ${response.code()} - $errorMessage", Toast.LENGTH_SHORT).show()
                    android.util.Log.e("EliminarAmistad", "Error: ${response.code()}, Response: $errorMessage")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                android.util.Log.e("EliminarAmistad", "Fallo en la conexión: ${t.message}")
            }
        })
    }







    private fun eliminarPersonaPorCedula(apiService: GraphQLApiService, cedulaPersona: String) {
        val call = apiService.deletePersona(cedulaPersona) // Llama a la ruta de escritura con la cédula
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Persona eliminada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@MainActivity, "Error al eliminar persona: ${response.code()} - $errorMessage", Toast.LENGTH_SHORT).show()
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
