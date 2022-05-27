package app.homsai.engine.common.domain.models;


import java.util.Collection;

public class Role {

    private String name;
    private Collection<Permission> permissions;

    public Role() {}

    public Role(String name, Collection<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Permission> permissions) {
        this.permissions = permissions;
    }
}
