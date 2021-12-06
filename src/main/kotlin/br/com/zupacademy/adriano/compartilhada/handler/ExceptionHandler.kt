package br.com.zupacademy.adriano.compartilhada.handler

import br.com.zupacademy.adriano.compartilhada.excecao.GrpcException
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import jakarta.inject.Singleton
import java.lang.Exception
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorHandler::class)
open class ExceptionHandler  : MethodInterceptor<Any, Any> {

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {

        try {
            return context.proceed()
        } catch (ex: Exception) {

            val responseObserver = context.parameterValues[1] as StreamObserver<*>

            val status = when(ex) {
                is ConstraintViolationException -> Status.INVALID_ARGUMENT
                    .withCause(ex)
                    .withDescription(ex.message)
                is GrpcException -> ex.status
                    .withDescription(ex.mensagem)
                else -> Status.UNKNOWN
                    .withCause(ex)
                    .withDescription("Ops, um erro inesperado ocorreu")
            }

            responseObserver.onError(status.asRuntimeException())
        }

        return null
    }

}