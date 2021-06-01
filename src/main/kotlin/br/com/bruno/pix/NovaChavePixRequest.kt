package br.com.bruno.pix

import br.com.bruno.orange.pixdesafio.RegistraChavePixRequest
import br.com.bruno.orange.pixdesafio.TipoDaChave
import br.com.bruno.orange.pixdesafio.TipoDaConta
import br.com.bruno.pix.shared.validation.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import br.com.caelum.stella.validation.CPFValidator
import io.micronaut.validation.validator.constraints.EmailValidator

@ValidPixKey
@Introspected
class NovaChavePixRequest(@field:NotNull val tipoDeConta: TipoDeContaRequest?,
                          @field:Size(max = 77) val chave: String?,
                          @field:NotNull val tipoDeChave: TipoDeChaveRequest?) {

    fun paraModeloGrpc(clienteId: UUID): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoDaConta(tipoDeConta?.atributoGrpc ?: TipoDaConta.UNKNOWN_TIPO_CONTA)
            .setTipoDaChave(tipoDeChave?.atributoGrpc ?: TipoDaChave.UNKNOWN_TIPO_CHAVE)
            .setValorDaChave(chave ?: "")
            .build()
    }

    override fun toString(): String {
        return "NovaChavePixRequest(tipoDeConta=$tipoDeConta, chave=$chave, tipoDeChave=$tipoDeChave)"
    }


}

enum class TipoDeChaveRequest(val atributoGrpc: TipoDaChave) {

    CPF(TipoDaChave.CPF) {

        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return CPFValidator(false)
                .invalidMessagesFor(chave)
                .isEmpty()
        }

    },

    CELULAR(TipoDaChave.CELULAR) {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoDaChave.EMAIL) {

        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }

        }
    },

    ALEATORIA(TipoDaChave.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank() // não deve se preenchida
    };

    abstract fun valida(chave: String?): Boolean
}

enum class TipoDeContaRequest(val atributoGrpc: TipoDaConta) {

    CONTA_CORRENTE(TipoDaConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoDaConta.CONTA_POUPANCA)
}