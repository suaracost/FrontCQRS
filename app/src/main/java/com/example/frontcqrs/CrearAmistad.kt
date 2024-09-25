package com.example.frontcqrs

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontcqrs.models.Amistad
import com.example.frontcqrs.models.AmistadRequest
import com.example.frontcqrs.models.Persona
import com.example.frontcqrs.network.ApiClient
import com.example.frontcqrs.network.GraphQLApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearAmistad : AppCompatActivity() {

    private lateinit var contenedorAmigos: LinearLayout
    private lateinit var apiServiceRead: GraphQLApiService
    private lateinit var apiServiceWrite: GraphQLApiService
    private val cedulaUsuarioActual = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_amistad)

        contenedorAmigos = findViewById(R.id.contenedorAmigos)

        apiServiceRead = ApiClient.getService1().create(GraphQLApiService::class.java)
        apiServiceWrite = ApiClient.getService2().create(GraphQLApiService::class.java)

        obtenerPersonasDesdeApi()
    }

    private fun obtenerPersonasDesdeApi() {
        val call = apiServiceRead.getPersonas()
        call.enqueue(object : Callback<List<Persona>> {
            override fun onResponse(call: Call<List<Persona>>, response: Response<List<Persona>>) {
                if (response.isSuccessful) {
                    val listaPersonas = response.body() ?: emptyList()
                    mostrarPersonas(listaPersonas)
                } else {
                    Toast.makeText(this@CrearAmistad, "Error al obtener personas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Persona>>, t: Throwable) {
                Toast.makeText(this@CrearAmistad, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarPersonas(listaPersonas: List<Persona>) {
        for (persona in listaPersonas) {
            agregarTarjetaPersona(persona)
        }
    }

    private fun agregarTarjetaPersona(persona: Persona) {
        val inflater = layoutInflater
        val cardView = inflater.inflate(R.layout.card_view_layout, contenedorAmigos, false)

        val tvAmigosName = cardView.findViewById<TextView>(R.id.tvAmigosName)
        tvAmigosName.text = persona.cedula

        val btnAgregarAmigo = cardView.findViewById<Button>(R.id.btnAgregarAmigo)
        btnAgregarAmigo.setOnClickListener {
            agregarAmistad(persona.cedula)
        }

        contenedorAmigos.addView(cardView)
    }

    private fun agregarAmistad(cedulaPersona2: String) {
        val call = apiServiceRead.getAmistades()
        call.enqueue(object : Callback<List<Amistad>> {
            override fun onResponse(call: Call<List<Amistad>>, response: Response<List<Amistad>>) {
                if (response.isSuccessful) {
                    val listaAmistades = response.body() ?: emptyList()

                    // Verificamos si ya existe una amistad con esa persona
                    val amistadYaExiste = listaAmistades.any {
                        it.cedula_persona1 == cedulaUsuarioActual && it.cedula_persona2 == cedulaPersona2
                    }

                    if (amistadYaExiste) {
                        Toast.makeText(this@CrearAmistad, "Ya has agregado a esta persona", Toast.LENGTH_SHORT).show()
                    } else {
                        // Si no existe, la agregamos
                        crearNuevaAmistad(cedulaPersona2)
                    }
                }
            }

            override fun onFailure(call: Call<List<Amistad>>, t: Throwable) {
                Toast.makeText(this@CrearAmistad, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun crearNuevaAmistad(cedulaPersona2: String) {
        val amistad = Amistad(cedulaUsuarioActual, cedulaPersona2)
        val amistadRequest = AmistadRequest(amistad)

        val call = apiServiceWrite.createAmistad(amistadRequest)
        call.enqueue(object : Callback<Amistad> {
            override fun onResponse(call: Call<Amistad>, response: Response<Amistad>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CrearAmistad, "Amigo agregado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CrearAmistad, "Error al agregar amigo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Amistad>, t: Throwable) {
                Toast.makeText(this@CrearAmistad, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
