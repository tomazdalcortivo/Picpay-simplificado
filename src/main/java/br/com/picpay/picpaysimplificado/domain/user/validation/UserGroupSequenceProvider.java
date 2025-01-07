package br.com.picpay.picpaysimplificado.domain.user.validation;

import br.com.picpay.picpaysimplificado.domain.user.User;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;


public class UserGroupSequenceProvider implements DefaultGroupSequenceProvider<User> {

    @Override
    public List<Class<?>> getValidationGroups(User user) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(User.class);

        if (isUserSelected(user))
            groups.add(user.getUserType().getGroup());

        return groups;
    }

    protected boolean isUserSelected(User user) {
        return user != null && user.getUserType() != null;
    }
}
