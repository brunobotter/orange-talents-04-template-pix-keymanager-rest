package br.com.bruno.pix

import br.com.bruno.orange.pixdesafio.KeymanagerRegistraGrpc
import br.com.bruno.orange.pixdesafio.RegistraChavePixResponse
import br.com.bruno.pix.TipoDeChaveRequest.CPF
import br.com.bruno.pix.TipoDeChaveRequest.EMAIL
import br.com.bruno.pix.TipoDeContaRequest.CONTA_CORRENTE
import br.com.bruno.pix.shared.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChavePixControllerTest{


    @field:Inject
    lateinit var registraStub: KeymanagerRegistraGrpc.KeymanagerRegistraBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve registrar uma nova chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RegistraChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .build()

        given(registraStub.criar(Mockito.any())).willReturn(respostaGrpc)


        val novaChavePix = NovaChavePixRequest(tipoDeConta = CONTA_CORRENTE,
            chave = "950.258.910-60",
            tipoDeChave = CPF
        )

        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", novaChavePix)
        val response = client.toBlocking().exchange(request, NovaChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(KeymanagerRegistraGrpc.KeymanagerRegistraBlockingStub::class.java)
    }

}