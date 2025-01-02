package br.com.picpay.picpaysimplificado.repository;

import br.com.picpay.picpaysimplificado.domain.user.User;
import br.com.picpay.picpaysimplificado.domain.user.UserType;
import br.com.picpay.picpaysimplificado.dto.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("Test")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get user successfully from DB")
    void findUserByDocumentCase1() {
        String document = "99999999901";
        UserDTO data = new UserDTO("Pedro", "Teste", document, new BigDecimal(10), "test@gmail.com", "44444", UserType.COMMON);

        this.persistUser(data);

        Optional<User> result = this.repository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user from DB when user not exists")
    void findUserByDocumentCase2(){
        String document = "99999999901";

        Optional<User> result = this.repository.findUserByDocument(document);

        assertThat(result.isEmpty()).isTrue();
    }

    private void persistUser(UserDTO data){
        this.entityManager.persist(data.toUser());
    }

}