package br.com.picpay.picpaysimplificado.dto;

import br.com.picpay.picpaysimplificado.domain.transaction.Transaction;
import br.com.picpay.picpaysimplificado.domain.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        BigDecimal value,
        Long senderId,
        Long receiverId) {

    public Transaction toTransaction(User sender, User receiver) {
        Transaction transaction = new Transaction();
        transaction.setAmount(this.value);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}
