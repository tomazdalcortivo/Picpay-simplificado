package br.com.picpay.picpaysimplificado.dto;

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

}
