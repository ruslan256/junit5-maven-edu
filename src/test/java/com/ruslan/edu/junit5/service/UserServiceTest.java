package com.ruslan.edu.junit5.service;

import com.ruslan.edu.junit5.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserServiceTest {

    private UserService userService;

    @BeforeAll
    static void init() {
        System.out.println("Before ALL: ");
        System.out.println();
    }

    @AfterAll
    static void closeResources() {
        System.out.println("After ALL: ");
    }

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Test
    void usersEmptyIfNoUserAdded() {
        System.out.println("Test1: " + this);
        List<User> users = userService.getAll();
        assertTrue(users.isEmpty(), () -> "User list should be empty");
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("Test2: " + this);
        userService.add(new User());
        userService.add(new User());

        List<User> users = userService.getAll();
        assertEquals(2, users.size());
    }

    @AfterEach
    void deleteDataFromDB() {
        System.out.println("After each: " + this);
        System.out.println();
    }
}
