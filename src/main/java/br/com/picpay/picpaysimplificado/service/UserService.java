package br.com.picpay.picpaysimplificado.service;

import br.com.picpay.picpaysimplificado.domain.user.User;
import br.com.picpay.picpaysimplificado.domain.user.UserType;
import br.com.picpay.picpaysimplificado.dto.UserDTO;
import br.com.picpay.picpaysimplificado.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuario do tipo logista Não está autorizado a realizar a tranzação");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("saldo suficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuario não encontrado"));
    }

    public void saveUser(User user) {
        this.repository.save(user);
    }

    public User createUser(UserDTO data) {
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return this.repository.findAll();
    }
}
