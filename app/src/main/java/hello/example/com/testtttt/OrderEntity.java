package hello.example.com.testtttt;

/**
 * Created by p2 on 27/10/2557.
 */
public class OrderEntity {
    private String name;
    private Integer number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString(){
        return getName() + "จำนวน" + getNumber().toString();
    }
}
