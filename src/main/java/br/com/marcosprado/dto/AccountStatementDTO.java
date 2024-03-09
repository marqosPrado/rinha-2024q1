package br.com.marcosprado.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AccountStatementDTO {
        public Balance saldo;
        @JsonProperty("ultimas_transacoes")
        public List<ResponseTransactionStatement> ultimasTransacoes = new ArrayList<>();

        public record Balance(
                Integer total,
                @JsonProperty("data_extrato")
                LocalDateTime dataExtrato,
                Integer limite
        ) {
        }

        public record ResponseTransactionStatement(
                Integer valor,
                char tipo,
                String descricao,
                @JsonProperty("realizada_em")
                LocalDateTime realizadaEm
        ) {
        }

}
