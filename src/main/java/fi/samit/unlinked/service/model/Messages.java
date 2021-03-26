package fi.samit.unlinked.service.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
public class Messages extends AbstractPersistable<Long> {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private int likes;
    private LocalDateTime messageDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account receiver;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account sender;

}
