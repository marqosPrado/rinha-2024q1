package br.com.marcosprado.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ResponseTransactionStatementDTO(
        Integer valor,
        char tipo,
        String descricao,
        @JsonProperty("realizada_em")
        LocalDateTime realizadaEm
) {
}
