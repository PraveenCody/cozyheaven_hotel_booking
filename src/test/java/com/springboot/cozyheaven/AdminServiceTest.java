
package com.springboot.cozyheaven;

import com.springboot.cozyheaven.dto.request.AdminDto;
import com.springboot.cozyheaven.enums.Role;
import com.springboot.cozyheaven.enums.Status;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.AdminMapper;
import com.springboot.cozyheaven.model.Admin;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.repository.AdminRepository;
import com.springboot.cozyheaven.repository.UserRepository;
import com.springboot.cozyheaven.service.AdminService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    private MockedStatic<AdminMapper> adminMapper;

    private AdminDto adminDto;
    private Admin admin;
    private User user;

    @BeforeEach
    public void setUp() {

        adminDto = mock(AdminDto.class);
        admin = mock(Admin.class);
        user = mock(User.class);

        adminMapper = mockStatic(AdminMapper.class);

        adminMapper.when(() ->
                AdminMapper.getMappedForAdmin(adminDto)
        ).thenReturn(admin);

    }

    @AfterEach
    public void tearDown() {
        adminMapper.close();
    }

    // 1. Insert admin successfully

    @Test
    public void insertTest() {

        when(adminDto.username()).thenReturn("praveen");
        when(adminDto.password()).thenReturn("12345");
        when(userRepository.getUserByUserName("praveen")).thenReturn(null);
        when(passwordEncoder.encode("12345")).thenReturn("encodedPassword");

        adminService.insert(adminDto);

        verify(userRepository).getUserByUserName("praveen");
        verify(passwordEncoder).encode("12345");

        adminMapper.verify(() ->
                AdminMapper.getMappedForAdmin(adminDto)
        );

        verify(admin).setUser(any(User.class));
        verify(userRepository).save(any(User.class));
        verify(adminRepository).save(admin);
    }

    // 2. Insert admin when username already exists

    @Test
    public void insertExistingUserTest() {

        when(adminDto.username()).thenReturn("praveen");
        when(userRepository.getUserByUserName("praveen"))
                .thenReturn(user);

        assertThrows(RuntimeException.class, () ->
                adminService.insert(adminDto)
        );

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
        verify(adminRepository, never()).save(any());
    }

    // 3. Get admin by ID successfully

    @Test
    public void getByIdTest() {

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        Admin result = adminService.getById(1);

        assertNotNull(result);
        assertEquals(admin, result);

        verify(adminRepository).findById(1);
    }

    // 4. Get admin by ID when admin does not exist

    @Test
    public void getByIdNotFoundTest() {

        when(adminRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                adminService.getById(1)
        );

        verify(adminRepository).findById(1);
    }

    // 5. Update all admin details

    @Test
    public void updateTest() {

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        when(admin.getUser()).thenReturn(user);

        adminService.update(
                1,
                "Praveen",
                "9876543210",
                "praveen@gmail.com",
                Status.Active
        );

        verify(admin).setName("Praveen");
        verify(admin).setPhone("9876543210");
        verify(admin).setEmail("praveen@gmail.com");
        verify(user).setIsActive(Status.Active);

        verify(adminRepository).save(admin);
    }

    // 6. Update admin with null and empty values

    @Test
    public void updateWithEmptyValuesTest() {

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        adminService.update(1, "", null, "", null);

        verify(admin, never()).setName(anyString());
        verify(admin, never()).setPhone(anyString());
        verify(admin, never()).setEmail(anyString());

        verify(adminRepository).save(admin);
    }

    // 7. Update admin when admin does not exist

    @Test
    public void updateNotFoundTest() {

        when(adminRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                adminService.update(
                        1, "Praveen", "9876543210",
                        "praveen@gmail.com", Status.Active
                )
        );

        verify(adminRepository, never()).save(any());
    }

    // 8. Delete admin successfully

    @Test
    public void deleteTest() {

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        when(admin.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(10);

        adminService.delete(1);

        verify(userRepository).deleteById(10);
        verify(adminRepository).delete(admin);
    }

    // 9. Delete admin when admin does not exist

    @Test
    public void deleteNotFoundTest() {

        when(adminRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                adminService.delete(1)
        );

        verify(userRepository, never()).deleteById(anyInt());
        verify(adminRepository, never()).delete(any());
    }

    // 10. Get all admins

    @Test
    public void getAllTest() {

        List<Admin> admins = List.of(
                admin,
                mock(Admin.class)
        );

        when(adminRepository.findAll()).thenReturn(admins);

        List<Admin> result = adminService.getAll();

        assertEquals(2, result.size());
        assertEquals(admins, result);

        verify(adminRepository).findAll();
    }

    // 11. Get admin by username successfully

    @Test
    public void getAdminTest() {

        when(adminRepository.getAdminByUsername("praveen"))
                .thenReturn(admin);

        Admin result = adminService.getAdmin("praveen");

        assertNotNull(result);
        assertEquals(admin, result);

        verify(adminRepository).getAdminByUsername("praveen");
    }

    // 12. Get admin when username does not exist

    @Test
    public void getAdminNotFoundTest() {

        when(adminRepository.getAdminByUsername("praveen"))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                adminService.getAdmin("praveen")
        );

        verify(adminRepository).getAdminByUsername("praveen");
    }
}
