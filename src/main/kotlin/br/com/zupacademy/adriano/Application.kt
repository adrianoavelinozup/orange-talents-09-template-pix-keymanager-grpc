package br.com.zupacademy.adriano

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zupacademy.adriano")
		.start()
}

