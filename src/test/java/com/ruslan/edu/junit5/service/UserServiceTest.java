package com.ruslan.edu.junit5.service;

import com.ruslan.edu.junit5.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "p091");
    private static final User PETER = User.of(2, "Peter", "p331");
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
        userService.add(IVAN);
        userService.add(PETER);

        List<User> users = userService.getAll();
        assertEquals(2, users.size());
    }

    @Test
    void loginSuccessIfUserExist(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
    }

    @Test
    void loginFailIfPasswordIsNotCorrect() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), "dummy");

        assertTrue(maybeUser.isEmpty());
    }

    @Test
    void loginFailIfUserDoesNotExist() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login("dummy", IVAN.getPassword());

        assertTrue(maybeUser.isEmpty());
    }

    @AfterEach
    void deleteDataFromDB() {
        System.out.println("After each: " + this);
        System.out.println();
    }
}
