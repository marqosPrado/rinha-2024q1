package br.com.marcosprado.services;


import br.com.marcosprado.domain.Cliente;
import br.com.marcosprado.domain.Transacao;
import br.com.marcosprado.dto.AccountStatementDTO;
import br.com.marcosprado.dto.BalanceResponseDTO;
import br.com.marcosprado.dto.ResponseTransactionStatementDTO;
import br.com.marcosprado.exceptions.ClientNotFoundException;
import br.com.marcosprado.repositories.ClienteRepository;
import br.com.marcosprado.repositories.TransactionRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountStatementService {

    @Inject
    ClienteRepository clienteRepository;
    @Inject
    TransactionRepository transactionRepository;

    @Transactional
    public AccountStatementDTO getStatement(Integer customerId) {
        Cliente client = findClientById(customerId)
                .orElseThrow(ClientNotFoundException::new);

        List<Transacao> transacao = getLastTransactions(customerId);

        List<ResponseTransactionStatementDTO> statement =
                transacao.stream()
                        .map(Transacao::toStatement)
                        .toList();

        BalanceResponseDTO balance = new BalanceResponseDTO(
                client.getSaldo(),
                LocalDateTime.now(),
                client.getLimite()
        );

        return new AccountStatementDTO(
                balance,
                statement
        );
    }

    private Optional<Cliente> findClientById(Integer clientId) {
        return clienteRepository.findByIdOptional(clientId.longValue(), LockModeType.PESSIMISTIC_READ);
    }

    private List<Transacao> getLastTransactions(Integer clienteId) {
        return transactionRepository.listTransaction(clienteId);
    }
}
