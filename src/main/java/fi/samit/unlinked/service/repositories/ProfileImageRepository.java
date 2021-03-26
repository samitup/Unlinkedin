package fi.samit.unlinked.service.repositories;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
ProfileImage findByAccount(Account account);
}
