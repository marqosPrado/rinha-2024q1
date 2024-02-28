package br.com.marcosprado.domain;

import br.com.marcosprado.dto.ResponseTransactionStatementDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer valor;
    @Column(name = "cliente_id")
    private Integer clienteId;
    @Column
    private char tipo;
    @Column
    private String descricao;
    @Column(name = "realizada_em")
    private LocalDateTime realizadaEm;

    public Transacao(
            Integer valor,
            Integer clienteId,
            char tipo,
            String descricao,
            LocalDateTime realizadaEm
    ) {
        this.valor = valor;
        this.clienteId = clienteId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizadaEm = realizadaEm;
    }
    public Transacao() {}

    public static ResponseTransactionStatementDTO toStatement(Transacao transacao) {
        return new ResponseTransactionStatementDTO(
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getDescricao(),
                transacao.getRealizadaEm());
    }
    public Integer getId() {
        return id;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getRealizadaEm() {
        return realizadaEm;
    }

    public void setRealizadaEm(LocalDateTime realizadaEm) {
        this.realizadaEm = realizadaEm;
    }
}
