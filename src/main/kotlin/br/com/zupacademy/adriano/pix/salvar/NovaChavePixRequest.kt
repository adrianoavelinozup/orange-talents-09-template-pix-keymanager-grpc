package br.com.zupacademy.adriano.pix

import br.com.zupacademy.adriano.TipoDeChaveValido
import br.com.zupacademy.adriano.UUIDValido
import br.com.zupacademy.adriano.compartilhada.validacao.*
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
data class NovaChavePixRequest(
    @field:UUIDValido val clienteId: String?,
    @field:Size(max = 77) val chavePix: String?,
    @field:TipoDeChaveValido @field:NotNull val tipoDeChave: String?,
    @field:NotNull val tipoDeConta: TipoDeConta?,
) {

    fun paraChavePix(conta: Conta): ChavePix {
        return ChavePix(
            clienteId = UUID.fromString(this.clienteId).toString(),
            tipoDeChave = TipoDeChave.valueOf(this.tipoDeChave!!),
            chavePix = (if (this.tipoDeChave == TipoDeChave.ALEATORIA.name) UUID.randomUUID()
                .toString() else this.chavePix)!!,
            tipoDeConta = TipoDeConta.valueOf(this.tipoDeConta!!.name),
            conta = conta
        )
    }

    fun validar() {
        TipoDeChave.valueOf(tipoDeChave.toString()).tipoDeChaveValidator.valida(this.chavePix!!)
    }
}

enum class TipoDeConta {
    CONTA_CORRENTE,
    CONTA_POUPANCA,
}

enum class TipoDeChave(val tipoDeChaveValidator: TipoDeChaveValidator) {
    CPF(CPFValidator()),
    CELULAR(CelularValidator()),
    EMAIL(EmailValidator()),
    ALEATORIA(AleatorioValidator())
}

