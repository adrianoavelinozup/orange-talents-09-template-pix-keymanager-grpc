package br.com.zupacademy.adriano.pix

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class Conta(
    @field:NotBlank
    @Column(nullable = false)
    val nomeInstituicao: String,

    @field:NotBlank
    @Column(nullable = false)
    val agencia: String,

    @field:NotBlank
    @Column(nullable = false)
    val numeroConta: String,

    @field:NotBlank
    @Column(nullable = false)
    val nome: String,

    @field:NotBlank
    @Column(nullable = false)
    val cpf: String,
)
