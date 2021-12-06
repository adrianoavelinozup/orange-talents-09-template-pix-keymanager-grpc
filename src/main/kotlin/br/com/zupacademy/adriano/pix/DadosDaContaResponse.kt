package br.com.zupacademy.adriano.pix

data class DadosDaContaResponse(
    val tipo: String,
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse,
) {
    fun paraConta(): Conta {
        return Conta(
            this.instituicao.nome,
            this.agencia,
            this.numero,
            this.titular.nome,
            this.titular.cpf,
        )
    }
}

data class InstituicaoResponse(
    val nome: String,
    val ispb: String,
)

data class TitularResponse(
    val id: String,
    val nome: String,
    val cpf: String,
)