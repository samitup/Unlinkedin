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
public class MessageController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ReplyService replyService;

    @GetMapping("kayttajat/{name}/kommentit")
    public String getMessages(Model model, @PathVariable String name) throws UnsupportedEncodingException {
        String formattedName = name.replace("+", " ");
        String currentUser = accountService.getAuthenticatedAccount().getUsername();
        Account account = accountService.getAccount(formattedName);
        model.addAttribute("messages", messageService.getMessagesByReceiver(account));
        model.addAttribute("account", account);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("replyPic", replyService.getProfilePicturesOfReplySender());
        model.addAttribute("reply", replyService.getRepliesByReceiver(account));
        model.addAttribute("sendersPic", messageService.getMessageSendersProfilePictures());

        return "fragments/layout::comments";
    }

    @PostMapping("/kayttajat/{name}/kommentit")
    public String postMessage(Model model,
            @PathVariable String name,
            @RequestBody(required = false) String post) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        messageService.postMessage(name, post);
        return "redirect:/kayttajat/" + encodedUsername + "/kommentit";
    }

    @GetMapping("/kayttajat/{name}/kommentit/{id}")
    public String deleteMessage(@PathVariable String name,
            @PathVariable Long id) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        messageService.deleteMessage(name, id);
        return "redirect:/kayttajat/" + encodedUsername;
    }
}
