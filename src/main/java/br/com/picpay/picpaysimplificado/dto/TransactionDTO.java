package br.com.picpay.picpaysimplificado.dto;

import java.math.BigDecimal;

public record TransactionDTO(
        BigDecimal value,
        Long senderId,
        Long receiverId) {
}
