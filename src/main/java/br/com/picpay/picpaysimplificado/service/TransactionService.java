package br.com.picpay.picpaysimplificado.service;

import br.com.picpay.picpaysimplificado.domain.transaction.Transaction;
import br.com.picpay.picpaysimplificado.domain.user.User;
import br.com.picpay.picpaysimplificado.dto.TransactionDTO;
import br.com.picpay.picpaysimplificado.infra.exception.PicPayGeneralException;
import br.com.picpay.picpaysimplificado.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserService userService;

    private final TransactionRepository repository;

    private final RestTemplate restTemplate;

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
        if (!this.authorizeTransaction())
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

    private boolean authorizeTransaction() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

            if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().value() == 403) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    String status = (String) responseBody.get("status");
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

                    return "success".equalsIgnoreCase(status)
                            && data != null
                            && Boolean.TRUE.equals(data.get("authorization"));
                }
            }
        } catch (HttpStatusCodeException e) {
            System.out.println("Erro ao autorizar transação: " + e.getMessage());
        }
        return false;
    }
}
