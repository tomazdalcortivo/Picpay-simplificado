package br.com.picpay.picpaysimplificado.service;

import br.com.picpay.picpaysimplificado.domain.transaction.Transaction;
import br.com.picpay.picpaysimplificado.domain.user.User;
import br.com.picpay.picpaysimplificado.dto.TransactionDTO;
import br.com.picpay.picpaysimplificado.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserService userService;

    private final TransactionRepository repository;

    private final RestTemplate restTemplate;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction();

        if (!isAuthorized)
            throw new Exception("Transação não autorizada");


        Transaction createTransaction = new Transaction();
        createTransaction.setAmount(transaction.value());
        createTransaction.setReceiver(receiver);
        createTransaction.setSender(sender);
        createTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(createTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);


        return createTransaction;
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
