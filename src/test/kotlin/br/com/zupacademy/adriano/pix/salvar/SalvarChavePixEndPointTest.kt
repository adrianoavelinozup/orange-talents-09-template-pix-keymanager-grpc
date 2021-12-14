package br.com.zupacademy.adriano.pix.salvar

import br.com.zupacademy.adriano.ChavePixGrpcServiceGrpc
import br.com.zupacademy.adriano.ChavePixRequest
import br.com.zupacademy.adriano.compartilhada.client.ERPItauClient
import br.com.zupacademy.adriano.pix.ChavePixRepository
import br.com.zupacademy.adriano.pix.DadosDaContaResponse
import br.com.zupacademy.adriano.pix.InstituicaoResponse
import br.com.zupacademy.adriano.pix.TitularResponse
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import java.util.*

@MicronautTest(transactional = false)
internal class SalvarChavePixEndPointTest(
    @field:Inject val client: ERPItauClient,
    @field:Inject val grpcClient:ChavePixGrpcServiceGrpc.ChavePixGrpcServiceBlockingStub,
    @field:Inject val repository: ChavePixRepository
) {

    lateinit var  clientId: String

    lateinit var tipoDeChaveAleatorio: String

    @BeforeEach
    fun setup() {
        clientId = UUID.randomUUID().toString()
//        tipoDeChaveAleatorio = ChavePixRequest.TipoDeChave.values().map { it.name }.get(randomValue)
    }

    @Test
    fun `deve salvar uma nova chave pix`() {
        val titular = TitularResponse(
            id = clientId,
            nome = "Maria dos Santos",
            cpf = "01234567890"
        )

        val instituicao = InstituicaoResponse(
            nome = "ITAÚ UNIBANCO S.A.",
            ispb = "60701190"
        )

        val dadosDaContaReponse = DadosDaContaResponse(
            tipo = "CONTA_CORRENTE",
            instituicao = instituicao,
            agencia = "0001",
            numero = "291900",
            titular = titular
        )

        Mockito.`when`(client.pesquisarCliente(
            clientId,
            ChavePixRequest.TipoDeConta.CONTA_CORRENTE.name
        )).thenReturn(Optional.of(dadosDaContaReponse))

        val chavePixRequest = ChavePixRequest.newBuilder()
            .setClienteId(clientId)
            .setTipoDeChave(ChavePixRequest.TipoDeChave.CPF)
            .setTipoDeConta(ChavePixRequest.TipoDeConta.CONTA_CORRENTE)
            .setChavePix("01234567890")
            .build()

        val chavePixSalva = grpcClient.salvar(chavePixRequest)

        assertNotNull(chavePixSalva.pixId)
        assertTrue(repository.existsByChavePix("01234567890"))
    }

    @Test
    fun `deve exibir status de erro quando cadastrar nova chave pix com clientId inválido`() {
        val clientIdInvalido = "123qwe"
        val chavePixRequest = ChavePixRequest.newBuilder()
            .setClienteId(clientIdInvalido)
            .setTipoDeChave(ChavePixRequest.TipoDeChave.CPF)
            .setTipoDeConta(ChavePixRequest.TipoDeConta.CONTA_CORRENTE)
            .setChavePix("01234567890")
            .build()

        val error = Assertions.assertThrows(StatusRuntimeException::class.java) {
            grpcClient.salvar(chavePixRequest)
        }

        assertEquals(Status.INVALID_ARGUMENT.code, error.status.code)
        assertTrue(error.status.description!!.contains("Deve ser um clientId válido", true))
    }


    @ParameterizedTest
    @ValueSource(strings = ["00000000009","cpfinvalido"])
    @EmptySource
    fun `deve exibir status de erro quando cadastrar nova chave pix com cpf inválido`(cpf: String) {
        val chavePixRequest = ChavePixRequest.newBuilder()
            .setClienteId(clientId)
            .setTipoDeChave(ChavePixRequest.TipoDeChave.CPF)
            .setTipoDeConta(ChavePixRequest.TipoDeConta.CONTA_CORRENTE)
            .setChavePix(cpf)
            .build()

        val error = Assertions.assertThrows(StatusRuntimeException::class.java) {
            grpcClient.salvar(chavePixRequest)
        }

        assertEquals(Status.INVALID_ARGUMENT.code, error.status.code)
        assertTrue(error.status.description!!.contains("Chave pix deve ser um CPF válido. Ex: 01234567890", true))
    }

    @ParameterizedTest
    @ValueSource(strings = ["+55139972099999","5513997209999", "+5599788888"])
    @EmptySource
    fun `deve exibir status de erro quando cadastrar nova chave pix com telefone inválido`(celular: String) {
        val chavePixRequest = ChavePixRequest.newBuilder()
            .setClienteId(clientId)
            .setTipoDeChave(ChavePixRequest.TipoDeChave.CELULAR)
            .setTipoDeConta(ChavePixRequest.TipoDeConta.CONTA_CORRENTE)
            .setChavePix(celular)
            .build()

        val error = assertThrows(StatusRuntimeException::class.java) {
            grpcClient.salvar(chavePixRequest)
        }

        assertEquals(Status.INVALID_ARGUMENT.code, error.status.code)
        assertTrue(error.status.description!!.contains("Chave pix deve ser um celular válido. Ex: +5585988714077", true))
    }

    @ParameterizedTest
    @ValueSource(strings = ["email@email","@email.com", "email.email.com"])
    @EmptySource
    fun `deve exibir status de erro quando cadastrar nova chave pix com email inválido`(email: String) {
        val chavePixRequest = ChavePixRequest.newBuilder()
            .setClienteId(clientId)
            .setTipoDeChave(ChavePixRequest.TipoDeChave.CELULAR)
            .setTipoDeConta(ChavePixRequest.TipoDeConta.CONTA_CORRENTE)
            .setChavePix(email)
            .build()

        val error = assertThrows(StatusRuntimeException::class.java) {
            grpcClient.salvar(chavePixRequest)
        }

        assertEquals(Status.INVALID_ARGUMENT.code, error.status.code)
        assertTrue(error.status.description!!.contains("Chave pix deve ser um celular válido. Ex: +5585988714077", true))
    }

    @Factory
    class GrpcClient {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): ChavePixGrpcServiceGrpc.ChavePixGrpcServiceBlockingStub {
            return ChavePixGrpcServiceGrpc.newBlockingStub(channel)
        }
    }

    @MockBean(ERPItauClient::class)
    fun itauClientock(): ERPItauClient {
        return Mockito.mock(ERPItauClient::class.java)
    }
}