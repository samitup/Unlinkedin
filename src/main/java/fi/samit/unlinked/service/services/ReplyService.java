package fi.samit.unlinked.service.services;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.Messages;
import fi.samit.unlinked.service.model.Reply;
import fi.samit.unlinked.service.repositories.ReplyRepository;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ProfileImageService profileImageService;

    public void postReply(String name, String replyContent, Messages message) {
        Reply reply = new Reply();
        Account receiver = accountService.getAccount(name);
        Account sender = accountService.getAuthenticatedAccount();
        if (reply != null) {
            reply.setSender(sender);
            reply.setReceiver(receiver);
            reply.setContent(replyContent);
            reply.setReplyDate(LocalDateTime.now());
            reply.setMessage(message);
            replyRepository.save(reply);
        }
    }

    public List<Reply> getAllReplies() {
        return replyRepository.findAll();
    }

    public List<Reply> getRepliesByMessageId(Long messageId) {
        return replyRepository.findByMessageId(messageId);
    }
    public List<Reply> getRepliesByReceiver(Account account){
        return replyRepository.findByReceiver(account);
    }

    public HashMap<Account, String> getProfilePicturesOfReplySender() throws UnsupportedEncodingException {
        HashMap<Account, String> replyProfilePictures = profileImageService.getAllProfileImages();
        Account replySender = new Account();
        HashMap<Account, String> replySenderPic = new HashMap();
        if (replyProfilePictures != null) {
            for (Reply reply : getAllReplies()) {
                replySender = reply.getSender();
                for (Map.Entry<Account, String> entry : replyProfilePictures.entrySet()) {
                    if (entry.getKey().equals(replySender)) {
                        replySenderPic.put(replySender, entry.getValue());
                    }
                }
            }
        }
        return replyProfilePictures;
    }

}
