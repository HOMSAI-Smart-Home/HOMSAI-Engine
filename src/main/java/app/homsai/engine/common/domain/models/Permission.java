package app.homsai.engine.common.domain.models;

public class Permission {

    private String name;

    public Permission() {}

    public Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
