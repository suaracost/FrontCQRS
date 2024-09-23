package com.example.frontcqrs

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontcqrs.models.Amistad
import com.example.frontcqrs.network.ApiClient
import com.example.frontcqrs.network.GraphQLApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MostrarAmigos : AppCompatActivity() {

    private lateinit var contenedorAmigos: LinearLayout
    private lateinit var apiService: GraphQLApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_amigos)

        contenedorAmigos = findViewById(R.id.contenedorAmigos)

        val retrofit = ApiClient.getClient()
        apiService = retrofit.create(GraphQLApiService::class.java)

        obtenerAmigosAgregados()
    }

    private fun obtenerAmigosAgregados() {
        val call = apiService.getAmistades()
        call.enqueue(object : Callback<List<Amistad>> {
            override fun onResponse(call: Call<List<Amistad>>, response: Response<List<Amistad>>) {
                if (response.isSuccessful) {
                    val listaAmistades = response.body() ?: emptyList()

                    if (listaAmistades.isEmpty()) {
                        Toast.makeText(this@MostrarAmigos, "No se encontraron amigos agregados", Toast.LENGTH_SHORT).show()
                    } else {
                        mostrarAmigos(listaAmistades)
                    }
                } else {
                    Toast.makeText(this@MostrarAmigos, "Error al obtener amistades: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Amistad>>, t: Throwable) {
                Toast.makeText(this@MostrarAmigos, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarAmigos(listaAmistades: List<Amistad>) {
        for (amistad in listaAmistades) {
            agregarTarjetaAmigo(amistad.cedula_persona2)
        }
    }

    private fun agregarTarjetaAmigo(cedula: String) {
        val inflater = layoutInflater
        val cardView = inflater.inflate(R.layout.card_view_layout, contenedorAmigos, false)

        val tvAmigosName = cardView.findViewById<TextView>(R.id.tvAmigosName)
        tvAmigosName.text = cedula

        val btnAgregarAmigo = cardView.findViewById<Button>(R.id.btnAgregarAmigo)
        btnAgregarAmigo.visibility = View.GONE

        contenedorAmigos.addView(cardView)
    }

}
