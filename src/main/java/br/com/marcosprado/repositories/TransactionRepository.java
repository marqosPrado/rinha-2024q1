package br.com.marcosprado.repositories;

import br.com.marcosprado.domain.Transacao;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transacao> {
    public List<Transacao> listTransaction(Integer clienteId) {
       return find("clienteId", Sort.by("realizadaEm").descending(), clienteId)
               .page(Page.ofSize(10))
               .list();
    }
}