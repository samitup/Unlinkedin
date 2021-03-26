package fi.samit.unlinked.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min = 2, max = 30)
    private String username;
    @NotEmpty
    private String password;

    @OneToMany(mappedBy = "sender")
    private List<Messages> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Messages> receivedMessages = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<ImageObject> imageObjects = new ArrayList<>();

    @ManyToOne
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "account")
    private List<UserInfo> skills = new ArrayList<>();

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
