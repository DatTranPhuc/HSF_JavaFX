package javafx.demojavafxs3.entity;

public enum AccountRole {
    ADMIN(1, true),
    STAFF(2, true),
    CUSTOMER(3, false);

    private final int value;
    private final boolean canManageProducts;

    AccountRole(int value, boolean canManageProducts) {
        this.value = value;
        this.canManageProducts = canManageProducts;
    }

    public int getValue() {
        return value;
    }

    public boolean canManageProducts() {
        return canManageProducts;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public static AccountRole fromValue(int value) {
        for (AccountRole role : values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role value: " + value);
    }
}
