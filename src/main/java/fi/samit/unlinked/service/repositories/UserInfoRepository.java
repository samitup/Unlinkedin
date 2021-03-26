package fi.samit.unlinked.service.repositories;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.UserInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    List<UserInfo> findByAccount(Account account);
}
