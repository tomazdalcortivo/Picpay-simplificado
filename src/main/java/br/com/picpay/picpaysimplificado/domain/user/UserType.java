package br.com.picpay.picpaysimplificado.domain.user;

import br.com.picpay.picpaysimplificado.domain.user.validation.CnpjGroup;
import br.com.picpay.picpaysimplificado.domain.user.validation.CpfGroup;
import lombok.Getter;

@Getter
public enum UserType {

    COMMON("CPF", CpfGroup.class),
    MERCHANT("CNPJ", CnpjGroup.class);

    private final String documentType;
    private final Class<?> group;

    UserType(String documentType, Class<?> group) {
        this.documentType = documentType;
        this.group = group;
    }
}
