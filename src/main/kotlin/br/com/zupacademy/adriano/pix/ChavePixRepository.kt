package br.com.zupacademy.adriano.pix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface ChavePixRepository: CrudRepository<ChavePix, Long?> {
    fun existsByChavePix(id: String): Boolean
}