package com.example.frontcqrs

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontcqrs.R
import com.example.frontcqrs.models.Persona
import com.example.frontcqrs.network.ApiClient
import com.example.frontcqrs.network.GraphQLApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearAmistad : AppCompatActivity() {

    private lateinit var contenedorAmigos: LinearLayout
    private lateinit var apiService: GraphQLApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_amistad)

        // Inicializamos el contenedor de las tarjetas
        contenedorAmigos = findViewById(R.id.contenedorAmigos)

        // Inicializamos Retrofit
        val retrofit = ApiClient.getClient()
        apiService = retrofit.create(GraphQLApiService::class.java)

        // Llamamos a la API para obtener las personas
        obtenerPersonasDesdeApi()
    }

    private fun obtenerPersonasDesdeApi() {
        val call = apiService.getPersonas()
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
        // Añadir cada persona como un CardView dinámico
        for (persona in listaPersonas) {
            agregarTarjetaPersona(persona)
        }
    }

    private fun agregarTarjetaPersona(persona: Persona) {
        val inflater = layoutInflater
        val cardView = inflater.inflate(R.layout.card_view_layout, contenedorAmigos, false)

        // Mostrar solo la cédula en el TextView
        val tvAmigosName = cardView.findViewById<TextView>(R.id.tvAmigosName)
        tvAmigosName.text = persona.cedula  // Solo mostrar la cédula

        contenedorAmigos.addView(cardView) // Añadir la tarjeta al contenedor
    }
}
