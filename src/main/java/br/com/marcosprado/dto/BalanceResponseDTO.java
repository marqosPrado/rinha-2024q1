package br.com.marcosprado.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record BalanceResponseDTO(
        Integer total,
        @JsonProperty("data_extrato")
        LocalDateTime dataExtrato,
        Integer limite
) {
}
