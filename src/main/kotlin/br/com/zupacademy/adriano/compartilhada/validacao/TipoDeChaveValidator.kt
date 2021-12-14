package br.com.zupacademy.adriano.compartilhada.validacao

import br.com.zupacademy.adriano.compartilhada.excecao.GrpcException
import io.grpc.Status
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator

interface TipoDeChaveValidator {
    fun valida(chavePix: String)
}

class CPFValidator : TipoDeChaveValidator {
    override fun valida(chavePix: String) {
        val validator = org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator()
        validator.initialize(null)
        if (chavePix.isNullOrBlank() ||
            !chavePix.matches("^[0-9]{11}\$".toRegex()) ||
            !validator.isValid(chavePix, null)
        ) {
            throw GrpcException("Chave pix deve ser um CPF válido. Ex: 01234567890", Status.INVALID_ARGUMENT)
        }
    }
}

class CelularValidator : TipoDeChaveValidator {
    override fun valida(chavePix: String) {
        if (!chavePix.matches("^\\+[1-9][0-9]\\d{14}\$".toRegex())) {
            throw GrpcException("Chave pix deve ser um celular válido. Ex: +5585988714077", Status.INVALID_ARGUMENT)
        }
    }
}

class EmailValidator : TipoDeChaveValidator {
    override fun valida(chavePix: String) {

        if (chavePix.isNullOrBlank() || !EmailValidator().isValid(chavePix, null)) {
            throw GrpcException("Chave pix deve ser um email válido. Ex: email@email.com", Status.INVALID_ARGUMENT)
        }
    }
}

class AleatorioValidator : TipoDeChaveValidator {
    override fun valida(chavePix: String) {
        if (!chavePix.isNullOrBlank()) {
            throw GrpcException("Chave pix deve ser nula ou em branco para tipo de chave aleatória", Status.INVALID_ARGUMENT)
        }
    }
}