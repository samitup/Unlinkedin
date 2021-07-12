package fi.samit.unlinked.service.controllers;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.services.AccountService;
import fi.samit.unlinked.service.services.MessageService;
import fi.samit.unlinked.service.services.ReplyService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ReplyController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ReplyService replyService;

    @PostMapping("/kayttajat/{name}/kommentit/{messageId}/reply")
    public String postReply(@PathVariable String name, @PathVariable Long messageId, @RequestBody(required = false) String reply) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        replyService.postReply(name, reply, messageService.getMessageById(messageId));
        return "redirect:/kayttajat/" + encodedUsername + "/kommentit";
    }

    @GetMapping("/kayttajat/{name}/kommentit/{messageId}/reply")
    public String getReplies(Model model, @PathVariable String name, @PathVariable Long messageId) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        String formattedUsername = encodedUsername.replace("+", " ");
        Account account = accountService.getAccount(formattedUsername);
        model.addAttribute("messages", messageService.getMessagesByReceiver(account));
        model.addAttribute("account", account);
        model.addAttribute("replyPic", replyService.getProfilePicturesOfReplySender());

        if (!replyService.getAllReplies().isEmpty()) {
            model.addAttribute("reply", replyService.getRepliesByMessageId(messageId));
        }

        return "fragments/layout::comments";
    }
}
