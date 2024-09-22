package com.example.frontcqrs.network

import com.example.frontcqrs.models.Persona
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface GraphQLApiService {
    @Headers("Content-Type: application/json")
    @POST("graphql") // El endpoint de GraphQL
    fun query(@Body query: GraphQLQuery): Call<GraphQLResponse>

    @GET("personas")
    fun getPersonas(): Call<List<Persona>>
}
