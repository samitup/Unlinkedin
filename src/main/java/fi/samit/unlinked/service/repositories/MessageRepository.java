
package fi.samit.unlinked.service.repositories;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.Messages;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Messages,Long> {
    List<Messages> findBySender(Account account);
        List<Messages> findByReceiver(Account account);

}
