package br.com.marcosprado.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AccountStatementDTO(
        BalanceResponseDTO saldo,
        @JsonProperty("ultimas_transacoes")
        List<ResponseTransactionStatementDTO> ultimasTransacoes
) {
}
