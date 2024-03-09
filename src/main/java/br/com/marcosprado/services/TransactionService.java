package br.com.marcosprado.services;

import br.com.marcosprado.domain.Transacao;
import br.com.marcosprado.dto.RequestTransactionDTO;
import br.com.marcosprado.dto.ResponseTransactionDTO;
import br.com.marcosprado.exceptions.ClientNotFoundException;
import br.com.marcosprado.exceptions.InsufficientLimitException;
import br.com.marcosprado.exceptions.InvalidArgumentException;
import br.com.marcosprado.repositories.ClienteRepository;
import br.com.marcosprado.repositories.TransactionRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.LocalDateTime;

@ApplicationScoped
public class TransactionService {
    @Inject
    TransactionRepository transactionRepository;
    @Inject
    ClienteRepository clienteRepository;

    @WithTransaction
    public Uni<RestResponse<ResponseTransactionDTO>> createCreditTransaction(Long clientId,
                                                                    RequestTransactionDTO transaction) {
        return clienteRepository.findById(clientId, LockModeType.PESSIMISTIC_WRITE)
                .onItem().ifNull().failWith(ClientNotFoundException::new)
                .call(client -> {
                    var transacao = createTransaction(clientId, transaction);
                    client.setSaldo(client.getSaldo() + transaction.valor());
                    return transactionRepository.persist(transacao);
                })
                .map(client -> {
                    var response = new ResponseTransactionDTO(client.getLimite(), client.getSaldo());
                    return RestResponse.ResponseBuilder.ok(response).build();
                });
    }

    @WithTransaction
    public Uni<RestResponse<ResponseTransactionDTO>> createDebitTransaction(Long clientId,
                                                              RequestTransactionDTO transaction) {
        return clienteRepository.findById(clientId, LockModeType.PESSIMISTIC_WRITE)
                .onItem().ifNull().failWith(ClientNotFoundException::new)
                .call(Unchecked.function(client -> {
                    var transacao = createTransaction(clientId, transaction);
                    if (client.getSaldo() - transaction.valor() < -client.getLimite()) {
                        throw new InsufficientLimitException();
                    }
                    client.setSaldo(client.getSaldo() - transaction.valor());
                    return transactionRepository.persist(transacao);
                }))
                .map(client -> {
                    var response = new ResponseTransactionDTO(client.getLimite(), client.getSaldo());
                    return RestResponse.ResponseBuilder.ok(response).build();
                });
    }

    private Transacao createTransaction(Long customerId, RequestTransactionDTO transaction) {
        validTransaction(transaction);
        Transacao transacao = new Transacao();
        transacao.setValor(transaction.valor());
        transacao.setClienteId(customerId);
        transacao.setTipo(transaction.tipo());
        transacao.setDescricao(transaction.descricao());
        transacao.setRealizadaEm(LocalDateTime.now());
        return transacao;
    }

    private void validTransaction(RequestTransactionDTO transaction) {
        if (transaction.valor() <= 0)
            throw new InvalidArgumentException();
        if (transaction.tipo() != 'c' && transaction.tipo() != 'd')
            throw new InvalidArgumentException();
        if (transaction.descricao().isEmpty())
            throw new InvalidArgumentException();
        if (transaction.descricao().length() > 10)
            throw new InvalidArgumentException();
        if (transaction.descricao().isBlank())
            throw new InvalidArgumentException();
    }
}