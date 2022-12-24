package com.ruslan.edu.junit5.service;

import com.ruslan.edu.junit5.dto.User;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.*;

@Tags(@Tag("user"))
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "p091");
    private static final User PETER = User.of(2, "Peter", "p331");
    private UserService userService;

    @BeforeAll
    static void init() {
        System.out.println("Before ALL: ");
        System.out.println();
    }

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Test
    @Order(1)
    @DisplayName("users will be empty if no user added")
    void usersEmptyIfNoUserAdded() {
        System.out.println("Test1: " + this);
        List<User> users = userService.getAll();
        assertTrue(users.isEmpty(), "User list should be empty");
        MatcherAssert.assertThat(users, empty());
    }

    @Test
    @Order(2)
    void usersSizeIfUserAdded() {
        System.out.println("Test2: " + this);
        userService.add(IVAN);
        userService.add(PETER);

        List<User> users = userService.getAll();
        assertThat(users).hasSize(2);
//        assertEquals(2, users.size());
    }

    @Test
    void usersConvertedToMapById() {
        userService.add(IVAN, PETER);
        Map<Integer, User> users = userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETER.getId()),
                () -> assertThat(users).containsValues(IVAN, PETER),
                () -> MatcherAssert.assertThat(users, IsMapContaining.hasKey(IVAN.getId()))
        );
    }

    @AfterEach
    void deleteDataFromDB() {
        System.out.println("After each: " + this);
        System.out.println();
    }

    @AfterAll
    static void closeResources() {
        System.out.println("After ALL: ");
    }

    @Nested
    @DisplayName("test user login functionality")
    @Tag("login")
    class LoginTest {

        @Test
        void loginSuccessIfUserExist() {
            userService.add(IVAN);
            Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

//        assertTrue(maybeUser.isPresent());
//        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
            assertThat(maybeUser).isPresent();
            maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
        }

        @Test
        void throwExceptionIfUsernameOrPasswordIsNullWay1() {
            try {
                userService.login(null, "dummy");
                fail("login should throw exception on null username");
            } catch (IllegalArgumentException ex) {
                assertTrue(true);
            }
        }

        @Test
        void throwExceptionIfUsernameOrPasswordIsNullWay2() {
            assertAll(
                    () -> {
                        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy"));
                        assertThat(exception.getMessage()).isEqualTo("username or password is null");
                    },
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null))
            );
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
    }
}