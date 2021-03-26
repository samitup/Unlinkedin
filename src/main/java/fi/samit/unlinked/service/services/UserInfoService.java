package fi.samit.unlinked.service.services;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.UserInfo;
import fi.samit.unlinked.service.repositories.UserInfoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public void postInfo(String name, String infoContent) {
        Account poster = accountService.getAuthenticatedAccount();
        String username = accountService.getAuthenticatedAccount().getUsername();
        UserInfo info = new UserInfo();
        if (infoContent != null && name.equals(username)) {
            info.setAccount(poster);
            info.setContent(infoContent);
            userInfoRepository.save(info);
        }
    }

    public List<UserInfo> getUserInfo(Account account) {
        return userInfoRepository.findByAccount(account);
    }

    public void deleteInfo(String profileName, Long id) {
        String username = accountService.getAuthenticatedAccount().getUsername();
        if (profileName.equals(username)) {                                        //tarkistetaan että kirjautunut käyttäjä
            userInfoRepository.deleteById(id);
        }
    }

}
