package br.com.zupacademy.adriano.compartilhada.excecao

import io.grpc.Status

class GrpcException(
    val mensagem: String,
    val status: Status,
): Exception()