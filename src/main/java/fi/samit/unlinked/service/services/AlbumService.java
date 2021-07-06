package fi.samit.unlinked.service.services;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.ImageObject;
import fi.samit.unlinked.service.repositories.ImageObjectRepository;
import java.io.IOException;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlbumService {

    @Autowired
    private ImageObjectRepository imageRepository;

    @Autowired
    private AccountService accountService;

    public Map<Long, String> getImagesOfUser(Account account) {
        Map<Long, String> images = new HashMap<>();
        for (ImageObject img : account.getImageObjects()) {
            try {
                byte[] encode = Base64.getEncoder().encode(img.getContent());
                images.put(img.getId(), new String(encode, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        TreeMap<Long, String> sorted = new TreeMap<>(images);
        return sorted;
    }

    public void postImageToAlbum(String profileName, MultipartFile file) throws IOException {
        String loggedInUsername = accountService.getAuthenticatedAccount().getUsername();
        ImageObject image = new ImageObject();
        System.out.println("ContentType :"+file.getContentType());
        if (profileName.equals(loggedInUsername) && file.isEmpty() == false && !(file.getContentType().equals("image/jpeg") ||
                file.getContentType().equals("image/jpg") || 
                file.getContentType().equals("image/png")))throw new IOException("Filetype is incorrect! Only png, jpg and jpeg are allowed"); {
            Account account = accountService.getAccount(profileName);
            image.setContent(file.getBytes());
            image.setAccount(account);
            account.getImageObjects().add(image);
            imageRepository.save(image);
        }
    }

    public void deleteImageById(Long id) {
        imageRepository.deleteById(id);
    }

    public boolean imageExistById(Long id) {
        return imageRepository.existsById(id);
    }

    public byte[] getImageContentById(Long id) {
        ImageObject image = imageRepository.getOne(id);
        byte[] encode = Base64.getEncoder().encode(image.getContent());
        return encode;
    }

    public List<ImageObject> getImagesByAccount(String name) {
        Account account = accountService.getAccount(name);
        return imageRepository.findByAccount(account);

    }

    public ImageObject getImageById(Long id) {
        return imageRepository.getOne(id);
    }
}
