package fi.samit.unlinked.service.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageObject extends AbstractPersistable<Long> implements Comparable<ImageObject> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Account account;
    private String name;
    private String mediaType;
    private Long size;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] content;
    
    @Override
    public int compareTo(ImageObject o) {
        return (int)(this.id -o.getId());
    }

}
