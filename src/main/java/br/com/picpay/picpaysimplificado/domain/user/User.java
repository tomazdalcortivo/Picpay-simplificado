package br.com.picpay.picpaysimplificado.domain.user;

import br.com.picpay.picpaysimplificado.domain.user.validation.CnpjGroup;
import br.com.picpay.picpaysimplificado.domain.user.validation.CpfGroup;
import br.com.picpay.picpaysimplificado.domain.user.validation.UserGroupSequenceProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.math.BigDecimal;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@GroupSequenceProvider(UserGroupSequenceProvider.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "CPF/CNPJ is required")
    @CPF(groups = CpfGroup.class)
    @CNPJ(groups = CnpjGroup.class)
    @Column(unique = true)
    private String document;

    @Email(message = "invalid e-mail")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

}
