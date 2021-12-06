package br.com.zupacademy.adriano

import javax.validation.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UUIDValidator::class])
annotation class UUIDValido(
    val message: String = "Deve ser um clientId válido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

class UUIDValidator: ConstraintValidator<UUIDValido,Any> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        val chave: String = value as String
        return chave.matches("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$".toRegex())
    }
}
