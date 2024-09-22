package com.example.frontcqrs.network

import com.example.frontcqrs.models.Persona
import com.example.frontcqrs.models.Amistad
import com.example.frontcqrs.models.PersonaRequest
import retrofit2.Call
import retrofit2.http.*

interface GraphQLApiService {

    // Obtener todas las personas
    @GET("personas")
    fun getPersonas(): Call<List<Persona>>

    // Obtener persona por ID
    @GET("personas/{id}")
    fun getPersonaById(@Path("id") id: String): Call<Persona>

    @Headers("Content-Type: application/json")
    @POST("personas_write")
    fun createPersona(@Body personaRequest: PersonaRequest): Call<Persona>

    // Actualizar una persona por ID
    @Headers("Content-Type: application/json")
    @PUT("personas_write/{id}")
    fun updatePersona(@Path("id") id: String, @Body persona: Persona): Call<Persona>

    // Eliminar una persona por ID
    @DELETE("personas_write/{id}")
    fun deletePersona(@Path("id") id: String): Call<Void>

    // Obtener todas las amistades
    @GET("amistades")
    fun getAmistades(): Call<List<Amistad>>

    // Obtener amistad por ID
    @GET("amistades/{id}")
    fun getAmistadById(@Path("id") id: String): Call<Amistad>

    // Crear una nueva amistad
    @Headers("Content-Type: application/json")
    @POST("amistades_write")
    fun createAmistad(@Body amistad: Amistad): Call<Amistad>

    // Actualizar una amistad por ID
    @Headers("Content-Type: application/json")
    @PUT("amistades_write/{id}")
    fun updateAmistad(@Path("id") id: String, @Body amistad: Amistad): Call<Amistad>

    // Eliminar una amistad por ID
    @DELETE("amistades_write/{id}")
    fun deleteAmistad(@Path("id") id: String): Call<Void>
}
