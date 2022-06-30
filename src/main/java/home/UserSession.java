package home;

import home.Models.SystemUser;

public final class UserSession {
    private static UserSession userSessionInstance;



    private static SystemUser systemUser;
    // private Set<String> privileges;   Check here: https://stackoverflow.com/questions/46508098/how-to-keep-user-information-after-login-in-javafx-desktop-application

    private UserSession(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public static UserSession getUserSessionInstance(SystemUser systemUser) {
        if (userSessionInstance == null) {
            synchronized (UserSession.class) {
                if (userSessionInstance == null) {
                    userSessionInstance = new UserSession(systemUser);
                }
            }

        }
        return userSessionInstance;
    }

    public static SystemUser getSystemUser() {
        return systemUser;
    }
    public static void setSystemUser(SystemUser systemUser) {
        UserSession.systemUser = systemUser;
    }

    public static void resetUserSession() {
        systemUser = null;
    }
}
