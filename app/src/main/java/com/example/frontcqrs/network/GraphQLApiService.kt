package com.example.frontcqrs.network

import com.example.frontcqrs.models.Persona
import com.example.frontcqrs.models.Amistad
import com.example.frontcqrs.models.AmistadRequest
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
    @PUT("personas_write/{cedula}")
    fun updatePersona(@Path("cedula") cedula: String, @Body body: HashMap<String, Any>): Call<Persona>

    // Eliminar una persona por ID
    @DELETE("personas_write/{cedula}")
    fun deletePersona(@Path("cedula") cedula: String): Call<Void>

    // Obtener todas las amistades
    @GET("amistades")
    fun getAmistades(): Call<List<Amistad>>

    // Obtener amistad por ID
    @GET("amistades/{id}")
    fun getAmistadById(@Path("id") id: String): Call<Amistad>

    // Crear una nueva amistad
    @Headers("Content-Type: application/json")
    @POST("amistades_write")
    fun createAmistad(@Body amistadRequest: AmistadRequest): Call<Amistad>

    // Actualizar una amistad por ID
    @Headers("Content-Type: application/json")
    @PUT("amistades_write/{id}")
    fun updateAmistad(@Path("id") id: String, @Body amistadRequest: AmistadRequest): Call<Amistad>

    // Eliminar una amistad por ID
    @HTTP(method = "DELETE", path = "amistades_write/{cedula_persona1}", hasBody = true)
    fun deleteAmistad(
        @Path("cedula_persona1") cedulaPersona1: String,
        @Body body: HashMap<String, String>
    ): Call<Void>

}
