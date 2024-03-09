package br.com.marcosprado.services;



import br.com.marcosprado.dto.AccountStatementDTO;
import br.com.marcosprado.exceptions.ClientNotFoundException;
import br.com.marcosprado.repositories.ClienteRepository;
import br.com.marcosprado.repositories.TransactionRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;


@ApplicationScoped
public class AccountStatementService {
    @Inject
    ClienteRepository clienteRepository;
    @Inject
    TransactionRepository transactionRepository;

    @WithSession
    public Uni<AccountStatementDTO> getStatement(Long id) {
        return clienteRepository.findById(id)
                .onItem().ifNull().failWith(ClientNotFoundException::new)
                .chain(client -> {
                    var statement = new AccountStatementDTO();
                    statement.saldo = new AccountStatementDTO.Balance(client.getSaldo(), LocalDateTime.now(), client.getLimite());
                    return transactionRepository.listTransaction(id)
                            .onItem().ifNotNull().transform(transactions -> {
                                transactions.forEach(transaction -> {
                                    statement.ultimasTransacoes.add(new AccountStatementDTO.ResponseTransactionStatement(
                                            transaction.getValor(),
                                            transaction.getTipo(),
                                            transaction.getDescricao(),
                                            transaction.getRealizadaEm()));
                                });
                                return statement;
                            });
                });
    }
}
