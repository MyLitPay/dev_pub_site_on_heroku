package main.model;

public enum Permission {
    READ("READ_AUTHORITY"),
    WRITE("WRITE_AUTHORITY"),
    UPDATE("UPDATE_AUTHORITY"),
    DELETE("DELETE_AUTHORITY"),
    MODERATE("MODERATE_AUTHORITY");

    private final String authority;

    Permission(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
