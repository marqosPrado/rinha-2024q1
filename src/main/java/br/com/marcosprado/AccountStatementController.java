package br.com.marcosprado;

import br.com.marcosprado.dto.AccountStatementDTO;
import br.com.marcosprado.exceptions.ClientNotFoundException;
import br.com.marcosprado.exceptions.InvalidArgumentException;
import br.com.marcosprado.services.AccountStatementService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/clientes/{id}/extrato")
public class AccountStatementController {

    @Inject
    AccountStatementService accountStatementService;

    @GET
    @RunOnVirtualThread
    public RestResponse<AccountStatementDTO> getStatement(@PathParam("id") String id) {
        Integer clientId = parseId(id);
        if (clientId < 0 || clientId > 5) throw new ClientNotFoundException();
        return RestResponse.ok(accountStatementService.getStatement(clientId));
    }

    private Integer parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException();
        }
    }
}
