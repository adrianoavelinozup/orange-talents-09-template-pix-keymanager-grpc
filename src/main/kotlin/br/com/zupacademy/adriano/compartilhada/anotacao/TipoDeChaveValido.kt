package br.com.zupacademy.adriano

import br.com.zupacademy.adriano.pix.TipoDeChave
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TipoChaveValidoValidator::class])
annotation class TipoDeChaveValido(
    val message: String = "Deve ser um tipo de chave válido. Ex: CPF, CELULAR, EMAIL ou ALEATORIA",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

class TipoChaveValidoValidator: ConstraintValidator<TipoDeChaveValido, Any> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return TipoDeChave.values().map { it.name }.contains(value)
    }

}
