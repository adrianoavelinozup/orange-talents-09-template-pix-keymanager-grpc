package br.com.zupacademy.adriano.pix.salvar

import br.com.zupacademy.adriano.ChavePixGrpcServiceGrpc
import br.com.zupacademy.adriano.ChavePixRequest
import br.com.zupacademy.adriano.ChavePixResponse
import br.com.zupacademy.adriano.compartilhada.handler.ErrorHandler
import br.com.zupacademy.adriano.pix.NovaChavePixRequest
import br.com.zupacademy.adriano.pix.TipoDeConta
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
@ErrorHandler
class SalvarChavePixEndPoint(
    @Inject val service: SalvarChavePixService
): ChavePixGrpcServiceGrpc.ChavePixGrpcServiceImplBase() {
    override fun salvar(
        request: ChavePixRequest?,
        responseObserver: StreamObserver<ChavePixResponse>?,
    ) {

        val novaChavePixRequest = request!!.paraNovaChavePixRequest()
        val chavePix = service.salvar(novaChavePixRequest)

        val response = ChavePixResponse.newBuilder()
            .setPixId(chavePix.id!!)
            .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }
}

fun ChavePixRequest.paraNovaChavePixRequest(): NovaChavePixRequest {
    val tipoDeConta: TipoDeConta = TipoDeConta.valueOf(this.tipoDeConta.name)
    return NovaChavePixRequest(this.clienteId, this.chavePix, this.tipoDeChave.name, tipoDeConta)
}