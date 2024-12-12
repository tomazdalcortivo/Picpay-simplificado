package br.com.picpay.picpaysimplificado.service;

import br.com.picpay.picpaysimplificado.domain.user.User;
import br.com.picpay.picpaysimplificado.domain.user.UserType;
import br.com.picpay.picpaysimplificado.dto.TransactionDTO;
import br.com.picpay.picpaysimplificado.infra.exception.PicPayGeneralException;
import br.com.picpay.picpaysimplificado.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AuthService authService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is ok")
    void createTransactionCase1() throws Exception {
        User sender = new User(1L, "Mario", "Souza", "99999999901", "mario@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Joao", "Roberto", "99999999902", "joao@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction()).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);
        transactionService.createTransaction(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

    }

    @Test
    @DisplayName("Should throw Exception when transaction is not allowed")
    void createTransactionCase2() throws Exception {

        User sender = new User(1L, "Mario", "Souza", "99999999901", "mario@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Joao", "Roberto", "99999999902", "joao@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction()).thenReturn(false);

        Exception thrown = Assertions.assertThrows(PicPayGeneralException.class, () -> {
            TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);
            transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transação não autorizada", thrown.getMessage());
    }
}