package br.com.zupacademy.adriano.pix.salvar

import br.com.zupacademy.adriano.compartilhada.client.ERPItauClient
import br.com.zupacademy.adriano.compartilhada.excecao.GrpcException
import br.com.zupacademy.adriano.pix.ChavePix
import br.com.zupacademy.adriano.pix.ChavePixRepository
import br.com.zupacademy.adriano.pix.DadosDaContaResponse
import br.com.zupacademy.adriano.pix.NovaChavePixRequest
import io.grpc.Status
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class SalvarChavePixService (
    @Inject val erpItauClient: ERPItauClient,
    @Inject val repository: ChavePixRepository
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun salvar(@Valid request: NovaChavePixRequest): ChavePix {
        request.validar()
        logger.info("Valida Chave Pix {}, Cliente {}", request.chavePix, request.clienteId)

        val dadosDaContaResponse = getContaCliente(request)
        logger.info("Encontra conta do Cliente {}", request.clienteId)

        if (repository.existsByChavePix(request.chavePix!!)) {
            throw GrpcException("Chave Pix repetida, escolha outra", Status.ALREADY_EXISTS)
        }

        val chavePix = repository.save(request.paraChavePix(dadosDaContaResponse!!.paraConta()))
        logger.info("Salva Chave Pix {}, Cliente {}", chavePix.chavePix, chavePix.clienteId)
        return repository.save(chavePix)
    }

    fun getContaCliente(request: NovaChavePixRequest): DadosDaContaResponse? {
        return erpItauClient
            .pesquisarCliente(request.clienteId!!, request.tipoDeConta.toString())
            .orElseThrow {
                logger.warn("Conta do Cliente {} não encontrada", request.clienteId)
                GrpcException(
                    mensagem = "Conta do Cliente não encontrada",
                    status = Status.NOT_FOUND
                ) }
    }
}
