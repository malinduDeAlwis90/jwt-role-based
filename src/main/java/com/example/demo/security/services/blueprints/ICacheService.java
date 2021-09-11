package com.example.demo.security.services.blueprints;

import com.example.demo.models.User;

public interface ICacheService {

    /**
     * This method is used to save logged in user by JWT
     *
     * @param jwt              the JWT token
     * @param user              the user
     */
    void saveLoggedInUser(String jwt, User user);

    /**
     * This method is used to return logged in user by JWT
     *
     * @param key the key
     * @return the user
     */
    User getLoggedInUser(String key);


    /**
     * Clear the logged in user's information.
     *
     * @param key the key
     */
    void clearLoggedInUsersSession(String key);
}
