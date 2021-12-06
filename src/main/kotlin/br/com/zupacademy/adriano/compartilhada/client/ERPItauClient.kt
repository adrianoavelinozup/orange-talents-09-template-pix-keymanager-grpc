package br.com.zupacademy.adriano.compartilhada.client

import br.com.zupacademy.adriano.pix.DadosDaContaResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import java.util.*

@Client("http://localhost:9091//api/v1/clientes")
interface ERPItauClient {
    @Get("/{clienteId}/contas")
    fun pesquisarCliente(
        @PathVariable clienteId: String,
        @QueryValue tipo: String,
    ): Optional<DadosDaContaResponse>
}