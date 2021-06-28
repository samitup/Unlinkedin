
package fi.samit.unlinked.service.repositories;



import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.Messages;
import fi.samit.unlinked.service.model.Reply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReplyRepository extends JpaRepository<Reply,Long> {
    List<Reply> findBySender(Account account);
        List<Reply> findByReceiver(Account account);
        List<Reply> findByMessageId(Long messageId);
}