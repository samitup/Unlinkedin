package fi.samit.unlinked.service.services;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.repositories.AccountRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAuthenticatedAccount() {
        String username = authenticationService.getName();
        return accountRepository.findByUsername(username);
    }

    public Account getAccount(String name) {
        return accountRepository.findByUsername(name);
    }

    public void saveAccount(String username, String password) {
        Account a = new Account(username, passwordEncoder.encode(password));
        accountRepository.save(a);
    }

    public void clearDatabase() {
        accountRepository.deleteAll();
    }

    public void save(Account account) {
        String tempUsername = account.getUsername();
        int len = tempUsername.length();                                        //Jos usernamen lopussa välilyönti, poistetaan se
        for (; len > 0; len--) {
            if (!Character.isWhitespace(tempUsername.charAt(len - 1))) {
                break;
            }
        }
        String formattedUsername = tempUsername.substring(0, len);
        account.setUsername(formattedUsername);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }

    void deleteProfilePicture(Account account) {
        account.setProfileImage(null);
        accountRepository.save(account);
    }
    
}
