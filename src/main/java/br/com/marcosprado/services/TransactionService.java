package br.com.marcosprado.services;

import br.com.marcosprado.domain.Cliente;
import br.com.marcosprado.domain.Transacao;
import br.com.marcosprado.dto.RequestTransactionDTO;
import br.com.marcosprado.dto.ResponseTransactionDTO;
import br.com.marcosprado.exceptions.ClientNotFoundException;
import br.com.marcosprado.exceptions.InsufficientLimitException;
import br.com.marcosprado.exceptions.InvalidArgumentException;
import br.com.marcosprado.repositories.ClienteRepository;
import br.com.marcosprado.repositories.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class TransactionService {
    @Inject
    TransactionRepository transactionRepository;
    @Inject
    ClienteRepository clienteRepository;

    @Transactional
    public ResponseTransactionDTO createCreditTransaction(Integer clientId, RequestTransactionDTO transaction) {
        Cliente client = findClientById(clientId)
                .orElseThrow(ClientNotFoundException::new);

        client.setSaldo(client.getSaldo() + transaction.valor());
        clienteRepository.persist(client);

        Transacao transacao = createTransaction(clientId, transaction);
        transactionRepository.persist(transacao);
        return new ResponseTransactionDTO(client.getLimite(), client.getSaldo());
    }

    @Transactional
    public ResponseTransactionDTO createDebitTransaction(Integer clientId, RequestTransactionDTO transaction) {
        Cliente client = findClientById(clientId)
                .orElseThrow(ClientNotFoundException::new);

        client.setSaldo(client.getSaldo() - transaction.valor());

        if (client.getLimite() + client.getSaldo() < 0)
            throw new InsufficientLimitException();

        clienteRepository.persist(client);

        Transacao transacao = createTransaction(clientId, transaction);
        transactionRepository.persist(transacao);
        return new ResponseTransactionDTO(client.getLimite(), client.getSaldo());
    }

    private Optional<Cliente> findClientById(Integer clientId) {
        return clienteRepository.findByIdOptional(clientId.longValue(), LockModeType.PESSIMISTIC_WRITE);
    }

    private Transacao createTransaction(Integer customerId, RequestTransactionDTO transaction) {
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