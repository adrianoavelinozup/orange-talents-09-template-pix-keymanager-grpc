package br.com.zupacademy.adriano.compartilhada.handler

import io.micronaut.aop.Around

@Target(AnnotationTarget.CLASS,AnnotationTarget.FIELD,AnnotationTarget.TYPE)
@Around
@MustBeDocumented
annotation class ErrorHandler
