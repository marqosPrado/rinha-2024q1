package br.com.marcosprado.repositories;

import br.com.marcosprado.domain.Transacao;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transacao> {
    public Uni<List<Transacao>> listTransaction(Long clientId) {
        return this.find("clienteId", Sort.by("realizadaEm").descending(), clientId)
                .list();
    }
}