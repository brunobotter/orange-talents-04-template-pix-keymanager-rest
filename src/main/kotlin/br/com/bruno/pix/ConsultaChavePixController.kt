package br.com.bruno.pix

import br.com.bruno.orange.pixdesafio.ConsultaChavePixRequest
import br.com.bruno.orange.pixdesafio.KeymanagerConsultaGrpc
import br.com.bruno.orange.pixdesafio.KeymanagerListaGrpc
import br.com.bruno.orange.pixdesafio.ListaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class ConsultaChavePixController (
    val consultaChavePixClient: KeymanagerConsultaGrpc.KeymanagerConsultaBlockingStub,
    val listaChavesPixClient: KeymanagerListaGrpc.KeymanagerListaBlockingStub)  {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun carrega(clienteId: UUID,
                pixId: UUID) : HttpResponse<Any> {

        LOGGER.info("[$clienteId] carrega chave pix por id: $pixId")
        val chaveResponse = consultaChavePixClient.carrega(ConsultaChavePixRequest.newBuilder()
            .setPixId(ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                .setClienteId(clienteId.toString())
                .setPixId(pixId.toString())
                .build()).
            build())

        return HttpResponse.ok(DetalheChavePixResponse(chaveResponse))
    }

    @Get("/pix/")
    fun lista(clienteId: UUID) : HttpResponse<Any> {

        LOGGER.info("[$clienteId] listando chaves pix")
        val pix = listaChavesPixClient.lista(ListaChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .build())

        val chaves = pix.chavesList.map { ChavePixResponse(it) }
        return HttpResponse.ok(chaves)
    }
}