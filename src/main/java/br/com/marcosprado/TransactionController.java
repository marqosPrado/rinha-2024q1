package br.com.marcosprado;

import br.com.marcosprado.dto.RequestTransactionDTO;
import br.com.marcosprado.dto.ResponseTransactionDTO;
import br.com.marcosprado.exceptions.ClientNotFoundException;
import br.com.marcosprado.exceptions.InvalidArgumentException;
import br.com.marcosprado.services.TransactionService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/clientes/{id}/transacoes")
public class TransactionController {
    @Inject
    TransactionService transactionService;
    
    @POST
    @RunOnVirtualThread
    public RestResponse<ResponseTransactionDTO> create(@PathParam("id") String id, RequestTransactionDTO transaction) {
        Integer clientId = parseId(id);
        if (clientId < 0 || clientId > 5) throw new ClientNotFoundException();
        if (transaction.tipo() == 'c') return RestResponse.ok(transactionService.createCreditTransaction(clientId, transaction));
        return RestResponse.ok(transactionService.createDebitTransaction(clientId, transaction));
    }

    private Integer parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException();
        }
    }
}
