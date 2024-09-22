package com.example.frontcqrs.network

import com.example.frontcqrs.models.Persona

class GraphQLResponse {
    var data: Data? = null

    inner class Data {
        var personas: List<Persona>? = null
    }
}
