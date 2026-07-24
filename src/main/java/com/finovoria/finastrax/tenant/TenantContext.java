package com.finovoria.finastrax.tenant;

public class TenantContext {

    private static final ThreadLocal<Long> tenantId = new ThreadLocal<>();

    private static final ThreadLocal<String> databaseName = new ThreadLocal<>();


    public static void setTenantId(Long id) {
        tenantId.set(id);
    }


    public static Long getTenantId() {
        return tenantId.get();
    }


    public static void setDatabaseName(String name) {
        databaseName.set(name);
    }


    public static String getDatabaseName() {
        return databaseName.get();
    }


    public static void clear() {
        tenantId.remove();
        databaseName.remove();
    }
}