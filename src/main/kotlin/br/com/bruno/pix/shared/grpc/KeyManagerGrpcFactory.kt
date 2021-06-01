package br.com.bruno.pix.shared.grpc


import br.com.bruno.orange.pixdesafio.KeymanagerConsultaGrpc
import br.com.bruno.orange.pixdesafio.KeymanagerDeletaGrpc
import br.com.bruno.orange.pixdesafio.KeymanagerListaGrpc
import br.com.bruno.orange.pixdesafio.KeymanagerRegistraGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager")val channel: ManagedChannel) {

    @Singleton
    fun registraChava() = KeymanagerRegistraGrpc.newBlockingStub(channel)

    @Singleton
    fun deletaChave() = KeymanagerDeletaGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = KeymanagerListaGrpc.newBlockingStub(channel)

    @Singleton
    fun carregaChave() = KeymanagerConsultaGrpc.newBlockingStub(channel)

}