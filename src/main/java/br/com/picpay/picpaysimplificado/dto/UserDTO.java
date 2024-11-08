package br.com.picpay.picpaysimplificado.dto;

import br.com.picpay.picpaysimplificado.domain.user.User;
import br.com.picpay.picpaysimplificado.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(
        String firstName,
        String lastName,
        String document,
        BigDecimal balance,
        String email,
        String password,
        UserType userType) {

    public User toUser() {
        User user = new User();
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setDocument(this.document);
        user.setBalance(this.balance);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setUserType(this.userType);
        return user;
    }

}
