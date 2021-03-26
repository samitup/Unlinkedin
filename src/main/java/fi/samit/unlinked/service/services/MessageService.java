package fi.samit.unlinked.service.services;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.repositories.MessageRepository;
import fi.samit.unlinked.service.model.Messages;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountService accountService;

    public List<Messages> getMessagesByAccount(Account account) {
        return messageRepository.findByReceiver(account);
    }

    public void postMessage(String name, String post) {
        Messages message = new Messages();
        Account receiver = accountService.getAccount(name);
        Account sender = accountService.getAuthenticatedAccount();
        if (post != null) {
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(post);
            message.setMessageDate(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    public void deleteMessage(String profileName, Long id) {
        String username = accountService.getAuthenticatedAccount().getUsername();
        if (profileName.equals(username)) {                                        //tarkistetaan että kirjautunut käyttäjä
            messageRepository.deleteById(id);
        }
    }
}
