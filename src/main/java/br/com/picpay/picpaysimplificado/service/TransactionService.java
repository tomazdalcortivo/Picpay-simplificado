package br.com.picpay.picpaysimplificado.service;

import br.com.picpay.picpaysimplificado.domain.transaction.Transaction;
import br.com.picpay.picpaysimplificado.domain.user.User;
import br.com.picpay.picpaysimplificado.dto.TransactionDTO;
import br.com.picpay.picpaysimplificado.infra.exception.PicPayGeneralException;
import br.com.picpay.picpaysimplificado.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserService userService;

    private final TransactionRepository repository;

    private final AuthService authService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = getUserById(transactionDTO.senderId());
        User receiver = getUserById(transactionDTO.receiverId());

        userService.validateUserTypeAndAmount(sender, transactionDTO.value());
        authorizeTransactionOrThrow();

        Transaction createTransaction = transactionDTO.toTransaction(sender, receiver);

        updateBalanceAndSaveUser(sender, receiver, transactionDTO.value());

        this.repository.save(createTransaction);
        return createTransaction;
    }

    private void authorizeTransactionOrThrow() throws PicPayGeneralException {
        if (!this.authService.authorizeTransaction())
            throw new PicPayGeneralException("Transação não autorizada");
    }

    private User getUserById(Long userId) throws Exception {
        return this.userService.findUserById(userId);
    }

    private void updateBalanceAndSaveUser(User sender, User receiver, BigDecimal amount) {
        sender.setBalance(sender.getBalance().subtract(sender.getBalance().subtract(amount)));
        receiver.setBalance(receiver.getBalance().add(amount));

        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);
    }

}
