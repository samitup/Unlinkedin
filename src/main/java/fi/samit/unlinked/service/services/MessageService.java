package fi.samit.unlinked.service.services;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.repositories.MessageRepository;
import fi.samit.unlinked.service.model.Messages;
import fi.samit.unlinked.service.repositories.ReplyRepository;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ProfileImageService profileImageService;

    public List<Messages> getMessagesByReceiver(Account account) {
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
            replyService.getRepliesByMessageId(id).forEach((reply) -> {             //poistetaan viestin vastaukset
                replyRepository.delete(reply);
            });
            messageRepository.deleteById(id);
        }
    }

    public List<Messages> getAllMessages() {
        return messageRepository.findAll();
    }

    public Messages getMessageById(long messageId) {
        return messageRepository.getOne(messageId);
    }

    public HashMap<Account, String> getMessageSendersProfilePictures() throws UnsupportedEncodingException {
        HashMap<Account, String> messageSendersProfilePictures = profileImageService.getAllProfileImages();
        Account sender = new Account();
        HashMap<Account, String> sendersPic = new HashMap();
        if (messageSendersProfilePictures != null) {
            for (Messages message : getAllMessages()) {
                sender = message.getSender();
                for (Map.Entry<Account, String> entry : messageSendersProfilePictures.entrySet()) {
                    if (entry.getKey().equals(sender)) {
                        sendersPic.put(sender, entry.getValue());
                    }
                }
            }
        }
        return messageSendersProfilePictures;
    }
}
