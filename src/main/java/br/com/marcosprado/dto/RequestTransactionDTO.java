package br.com.marcosprado.dto;

public record RequestTransactionDTO(
        Integer valor,
        char tipo,
        String descricao
) {
}
