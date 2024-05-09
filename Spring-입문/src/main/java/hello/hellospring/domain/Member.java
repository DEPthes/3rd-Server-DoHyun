package hello.hellospring.domain;

public class Member {
    private Long id; //데이터를 구분해주기 위해서 저장되는 id 값
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
