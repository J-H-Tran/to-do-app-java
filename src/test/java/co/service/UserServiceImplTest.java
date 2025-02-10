//package co.service;
//
//import co.jht.model.domain.persist.appuser.AppUser;
//import co.jht.repository.UserRepository;
//import co.jht.security.jwt.JwtTokenUtil;
//import co.jht.service.impl.UserServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class UserServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtTokenUtil jwtTokenUtil;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testLoadUserByUsername_UserFound() {
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setPassword("password");
//
//        when(userRepository.findByUsername("testuser")).thenReturn(user);
//
//        var userDetails = userService.loadUserByUsername("testuser");
//
//        assertNotNull(userDetails);
//        assertEquals("testuser", userDetails.getUsername());
//        verify(userRepository, times(1)).findByUsername("testuser");
//    }
//
//    @Test
//    void testLoadUserByUsername_UserNotFound() {
//        when(userRepository.findByUsername("testuser")).thenReturn(null);
//
//        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testuser"));
//        verify(userRepository, times(1)).findByUsername("testuser");
//    }
//
//    @Test
//    void testGetAllUsers() {
//        when(userRepository.findAll()).thenReturn(Collections.emptyList());
//
//        var users = userService.getAllUsers();
//
//        assertNotNull(users);
//        assertTrue(users.isEmpty());
//        verify(userRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetUserById_UserFound() {
//        AppUser user = new AppUser();
//        user.setId(1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        var foundUser = userService.getUserById(1L);
//
//        assertNotNull(foundUser);
//        assertEquals(1L, foundUser.getId());
//        verify(userRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetUserById_UserNotFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(1L));
//        verify(userRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testCreateUser() {
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setPassword("password");
//
//        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
//        when(userRepository.save(user)).thenReturn(user);
//
//        var createdUser = userService.createUser(user);
//
//        assertNotNull(createdUser);
//        assertEquals("encodedPassword", createdUser.getPassword());
//        verify(passwordEncoder, times(1)).encode("password");
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void testUpdateUser() {
//        AppUser user = new AppUser();
//        user.setId(1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(userRepository.save(user)).thenReturn(user);
//
//        var updatedUser = userService.updateUser(user);
//
//        assertNotNull(updatedUser);
//        assertEquals(1L, updatedUser.getId());
//        verify(userRepository, times(1)).findById(1L);
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void testDeleteUser() {
//        doNothing().when(userRepository).deleteById(1L);
//
//        userService.deleteUser(1L);
//
//        verify(userRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void testAuthenticateUser_Success() {
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setPassword("encodedPassword");
//
//        when(userRepository.findByUsername("testuser")).thenReturn(user);
//        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
//        when(jwtTokenUtil.generateToken("testuser")).thenReturn("token");
//
//        var token = userService.authenticateUser("testuser", "password");
//
//        assertNotNull(token);
//        assertEquals("token", token);
//        verify(userRepository, times(1)).findByUsername("testuser");
//        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
//        verify(jwtTokenUtil, times(1)).generateToken("testuser");
//    }
//
//    @Test
//    void testAuthenticateUser_Failure() {
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setPassword("encodedPassword");
//
//        when(userRepository.findByUsername("testuser")).thenReturn(user);
//        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);
//
//        var token = userService.authenticateUser("testuser", "password");
//
//        assertNull(token);
//        verify(userRepository, times(1)).findByUsername("testuser");
//        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
//        verify(jwtTokenUtil, times(0)).generateToken("testuser");
//    }
//
//    @Test
//    void testRegisterUser() {
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setEmail("test@tda.com");
//
//        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
//        when(userRepository.save(any(AppUser.class))).thenReturn(user);
//
//        userService.registerUser("testuser", "password", "test@tda.com");
//
//        verify(userRepository, times(1)).save(any(AppUser.class));
//        verify(passwordEncoder, times(1)).encode("password");
//    }
//
//    @Test
//    void testFindByUsername() {
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//
//        when(userRepository.findByUsername("testuser")).thenReturn(user);
//
//        var foundUser = userService.findByUsername("testuser");
//
//        assertNotNull(foundUser);
//        assertEquals("testuser", foundUser.getUsername());
//        verify(userRepository, times(1)).findByUsername("testuser");
//    }
//}