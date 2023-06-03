package org.example;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
@DisplayName("Test UserService")
public class TestUserService {
    @Mock
    private Map<String, User> userDatabase;
    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        System.out.println("Start tests for method with @Before");
    }
    @BeforeClass
    public static void setClass() {
        System.out.println("Set up @BeforeClass");
    }
    @AfterClass
    public static void tearDownClass() {
        System.out.println("Compileclass after testing with @AfterClass");
    }
    @After
    public void tearDown() {
        System.out.println("Execute clean up operations for after each test with @After");
    }

    //test  registerUser()
    @Test
    public void registerUserPositive() {
        User user = new User("Nombre", "password", "Nombre@gmail.com");
        when(userDatabase.containsKey("Nombre")).thenReturn(false);  //set up mock behavior for existing username in database, return false
        boolean result = userService.registerUser(user);                //registering user
        assertTrue(result);                                             //verify result comes back true
        verify(userDatabase).put("Nombre", user);                       //verify that the 'put' method of 'userDatabase' is called with the expected arguments
    }

    @Test
    public void registerUserNegative() {
        User user = new User("Nombre", "password", "Nombre@gmail.com");
        when(userDatabase.containsKey("Nombre")).thenReturn(true);   //set up mock behavior for existing username in database, return true
        boolean result = userService.registerUser(user);                //register user
        assertFalse(result);                                            //verify result comes back false
        verify(userDatabase, never()).put(anyString(), any(User.class));
    }

    @Test
    public void registerUserEdge() {
        User user = new User("", "password", "Nombre@gmail.com");
        when(userDatabase.containsKey("")).thenReturn(false);   //set up mock behavior for existing username in database, return false
        boolean result = userService.registerUser(user);           //registering user
        assertTrue(result);                                        //verify the result comes back true
        verify(userDatabase).put("", user);                        //verify 'put' of 'userDatabase' with what user argument acutally is
    }

    //test loginUser()
    @Test
    public void loginUserPositive() {
        User user = new User("Nombre", "password", "Nombre@gmail.com");
        when(userDatabase.get("Nombre")).thenReturn(user);                                  //set up mock for existing username in database and return user obj
        User loggedInUser = userService.loginUser("Nombre", "password");  //log user in
        assertNotNull(loggedInUser);                                                        //verify logged in user is not null
        assertEquals(user, loggedInUser);                                                   //verify that logged-in user matches expected user obj
    }

    @Test
    public void loginUserNegative_UserNotFound() {
        when(userDatabase.get("unknownUser")).thenReturn(null);                             //set up mock for existing username in database
        User loggedInUser = userService.loginUser("unknownUser", "pw1234"); //logging user in
        assertNull(loggedInUser);                                                             //verify logged in user is null
    }

    @Test
    public void loginUserEdge_WrongPassword() {
        User user = new User("Nombre", "password", "Nombre@gmail.com");
        when(userDatabase.get("Nombre")).thenReturn(user);                                      //set up mock behavior for existing username in database
        User loggedInUser = userService.loginUser("Nombre", "Nombren0tfr33"); //logging user in
        assertNull(loggedInUser);                                                               //verify logged in user is null
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Ignore
    @Test
    public void updateProfilePositive() {
        User user = new User("Nombre", "password", "Nombre@gmail.com");
        when(userDatabase.containsKey("Name")).thenReturn(false);        //set up mock behavior for existing username in database, return false
        boolean result = userService.updateUserProfile(user, "Name", "constrasena", "Name@gmail.com");  //attempt at updating profile info
        assertTrue(result);                                                 //verify result comes back true
        assertEquals("Name", user.getUsername());                  //verify use info is update right
        assertEquals("constrasena", user.getPassword());
        assertEquals("Name@gmail.com", user.getEmail());
        verify(userDatabase).put("Name", user);                             //verify that 'put'  method of 'userDatabase' is called with expected arguments
    }

    @Test
    public void updateProfileNegative_UsernameExistsAlready() {
        User user1 = new User("Nombre", "password", "Nombre@gmail.com");
        when(userDatabase.containsKey("Name")).thenReturn(true);          //set up mock behavior for existing username in database, return true
        boolean result = userService.updateUserProfile(user1, "Name", "password123", "newname@gmail.com"); //attempt at updating profile info
        assertFalse(result);                                                 //verify false  result
        assertEquals("Nombre", user1.getUsername());                //verify user info stays the same
        assertEquals("password", user1.getPassword());
        assertEquals("Nombre@gmail.com", user1.getEmail());
        verify(userDatabase, never()).put(anyString(), any(User.class));    //verify that the 'put' method of 'userDatabase' is never called
    }

    @Test
    public void updateProfileEdge() {
        User user1 = new User("Nombre", "password", "Nombre@gmail.com");
        when(userDatabase.containsKey(anyString())).thenReturn(true);   //set up mock behavior for existing username in database
        boolean result = userService.updateUserProfile(user1, "Name", "constrasena", "Name@gmail.com");  //attempt to update profile with existing username
        assertFalse(result);                                               //verify false result
        assertEquals("Nombre", user1.getUsername());              //verify original profile remains the same
        assertEquals("password", user1.getPassword());
        assertEquals("Nombre@gmail.com", user1.getEmail());
        verify(userDatabase, never()).put(anyString(), any(User.class));    //verify that user info was not modified
    }
    @Test
    public void updateProfileSetNewInfo() {
        User user = mock(User.class);
        when(userDatabase.containsKey(anyString())).thenReturn(false);  //set up mock behavior in user database - check username is not yet taken
        boolean result = userService.updateUserProfile(user, "Nombre", "password", "Nombre@gmail.com");  //update user info
        verify(user).setUsername("Nombre");                               //verify new info is set
        verify(user).setPassword("password");
        verify(user).setEmail("Nombre@gmail.com");
        verify(userDatabase).put("Nombre", user);
        assertTrue(result);                                               //verify the user info was set

    }


}