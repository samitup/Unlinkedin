package fi.samit.unlinked.service.repositories;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.ImageObject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageObjectRepository extends JpaRepository<ImageObject, Long> {
List<ImageObject> findByAccount(Account account);
}
