package br.com.marcosprado;

import br.com.marcosprado.dto.AccountStatementDTO;
import br.com.marcosprado.dto.RequestTransactionDTO;
import br.com.marcosprado.dto.ResponseTransactionDTO;
import br.com.marcosprado.exceptions.ClientNotFoundException;
import br.com.marcosprado.exceptions.InvalidArgumentException;
import br.com.marcosprado.services.AccountStatementService;
import br.com.marcosprado.services.TransactionService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/clientes")
public class ClientController {
    @Inject
    TransactionService transactionService;

    @Inject
    AccountStatementService accountStatementService;

    @Path("/{id}/extrato")
    @GET
    public Uni<AccountStatementDTO> getStatement(String id) {
        var idParsed = parseId(id);
        if (idParsed < 0 || idParsed > 5) throw new ClientNotFoundException();
        return accountStatementService.getStatement(idParsed);
    }

    @Path("/{id}/transacoes")
    @POST
    public Uni<RestResponse<ResponseTransactionDTO>> create(String id, RequestTransactionDTO transaction) {
        var idParsed = parseId(id);
        if (idParsed < 0 || idParsed > 5) {
            return Uni.createFrom().item(RestResponse.status(404));
        }
        if (transaction.tipo() != 'c' && transaction.tipo() != 'd') {
            return Uni.createFrom().item(RestResponse.status(422));
        }
        if (transaction.tipo() == 'c') return transactionService.createCreditTransaction(idParsed, transaction);
        return transactionService.createDebitTransaction(idParsed, transaction);
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ClientNotFoundException();
        }
    }

}
