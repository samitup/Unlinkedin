package fi.samit.unlinked.service.controllers;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.ProfileImage;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import fi.samit.unlinked.service.services.AccountService;
import fi.samit.unlinked.service.services.MessageService;
import fi.samit.unlinked.service.services.ProfileImageService;
import fi.samit.unlinked.service.services.ReplyService;
import fi.samit.unlinked.service.services.UserInfoService;
import java.io.IOException;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProfileController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ProfileImageService profileImageService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ReplyService replyService;

    @GetMapping("/kayttajat/{name}")
    public String getProfilePage(Model model, @PathVariable String name) throws UnsupportedEncodingException, IOException {
        profileImageService.init();
        String formattedName = name.replace("+", " ");
        String currentUser = accountService.getAuthenticatedAccount().getUsername();
        Account account = accountService.getAccount(formattedName);
        if (account == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find user: " + name);
        }
        model.addAttribute("account", account);
        model.addAttribute("currentUser", currentUser);
        if (messageService.getMessagesByReceiver(account) != null) {
            model.addAttribute("messages", messageService.getMessagesByReceiver(account));

        }

        if (userInfoService.getUserInfo(account) != null) {
            model.addAttribute("infos", userInfoService.getUserInfo(account));

        }

        ProfileImage image = profileImageService.getProfileImageByAccount(account);
        if (image != null) {
            byte[] encode = Base64.getMimeEncoder().encode(image.getContent());
            model.addAttribute("imageData", image);
            model.addAttribute("imageContent", new String(encode, "UTF-8"));
        }
        if (replyService.getRepliesByReceiver(account) != null) {
            model.addAttribute("reply", replyService.getRepliesByReceiver(account));

        }
        if (replyService.getProfilePicturesOfReplySender() != null) {
            model.addAttribute("replyPic", replyService.getProfilePicturesOfReplySender());

        }
        if (messageService.getMessageSendersProfilePictures() != null) {
            model.addAttribute("sendersPic", messageService.getMessageSendersProfilePictures());

        }

        return "profilePage";
    }

}
