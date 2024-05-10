package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity //jpa가 해당 어노테이션을 통해서 인식을 합니다.
public class Member {

    @Id //JPA에게 PK를 알려줍니다.
    private Long id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
