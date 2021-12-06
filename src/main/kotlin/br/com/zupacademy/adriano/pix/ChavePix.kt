package br.com.zupacademy.adriano.pix

import br.com.zupacademy.adriano.UUIDValido
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "chaves_pix")
class ChavePix(
    @UUIDValido
    @Column(nullable = false)
    val clienteId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDeChave: TipoDeChave,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDeConta: TipoDeConta,

    @Column(nullable = false, unique = true, length = 77)
    @field:NotBlank val chavePix: String,

    @field:Valid
    @field:Embedded val conta: Conta
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}
