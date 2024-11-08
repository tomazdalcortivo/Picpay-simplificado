package br.com.picpay.picpaysimplificado.controllers;

import br.com.picpay.picpaysimplificado.domain.transaction.Transaction;
import br.com.picpay.picpaysimplificado.dto.TransactionDTO;
import br.com.picpay.picpaysimplificado.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class  TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction) throws Exception {
        Transaction createTransaction = this.service.createTransaction(transaction);
        return new ResponseEntity<>(createTransaction, HttpStatus.OK);
    }
}
