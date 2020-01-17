package db;

import business.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDB {

    static HashMap<String, User> users = new HashMap<String, User>();

    // For Debugging, delete afterwards
    static {
        addUser("miguelsolans", "IAmForgetful");
        addUser("tifanysilva", "IAmForgetful");
        addUser("joaosilva", "IAmForgetful");
        addUser("henriquepereira", "IAmForgetful");
    }

    public static List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    public static void addUser(String username, String password) {
        users.put(username, new User(username, password, "admin"));
    }

    public static User getUser(String username) {
        return users.get(username);
    }

}
