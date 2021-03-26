package fi.samit.unlinked.service.repositories;

import fi.samit.unlinked.service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUsername(String username);
}
