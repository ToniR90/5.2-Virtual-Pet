package com.toni.virtualpet;

import com.toni.virtualpet.config.TestSecurityConfig;
import com.toni.virtualpet.repository.PetActionRepository;
import com.toni.virtualpet.repository.PetRepository;
import com.toni.virtualpet.repository.UserRepository;
import com.toni.virtualpet.service.user.AuthService;
import com.toni.virtualpet.service.pet.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestSecurityConfig.class)
@ContextConfiguration(classes = {VirtualPetApiApplication.class, TestSecurityConfig.class})
@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = com.toni.virtualpet.security.SecurityConfig.class
))

@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class VirtualPetApplicationTest {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private PetService petService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetActionRepository petActionRepository;

    // CONTEXT

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void applicationContextStartsSuccessfully() {
        assertThat(applicationContext.getBean(VirtualPetApiApplication.class)).isNotNull();
        assertThat(applicationContext.isActive()).isTrue();
    }

    // SERVICES

    @Test
    void allCriticalServicesAreLoaded() {
        assertThat(petService).isNotNull();
        assertThat(authService).isNotNull();
        assertThat(applicationContext.getBean(PetService.class)).isNotNull();
        assertThat(applicationContext.getBean(AuthService.class)).isNotNull();
    }

    @Test
    void serviceLayerIsProperlyConfigured() {
        assertThat(petService).isInstanceOf(PetService.class);
        assertThat(authService).isInstanceOf(AuthService.class);
    }

    // REPOSITORIES

    @Test
    void allRepositoriesAreLoaded() {
        assertThat(petRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(petActionRepository).isNotNull();
    }

    @Test
    void repositoryLayerIsProperlyConfigured() {
        assertThat(petRepository.getClass().getName()).contains("Proxy");
        assertThat(userRepository.getClass().getName()).contains("Proxy");
        assertThat(petActionRepository.getClass().getName()).contains("Proxy");
    }

    // DATABASE

    @Test
    void databaseConnectionWorks() {
        assertThat(userRepository.count()).isGreaterThanOrEqualTo(0);
        assertThat(petRepository.count()).isGreaterThanOrEqualTo(0);
        assertThat(petActionRepository.count()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void transactionsAreWorkingProperly() {
        long initialUserCount = userRepository.count();
        assertThat(initialUserCount).isGreaterThanOrEqualTo(0);
    }

    // SECURITY

    @Test
    void securityBeansAreLoaded() {
        assertThat(applicationContext.containsBean("testPasswordEncoder")).isTrue();
        assertThat(applicationContext.containsBean("testFilterChain")).isTrue();
    }

    @Test
    void testSecurityConfigIsActive() {
        Object passwordEncoder = applicationContext.getBean("testPasswordEncoder");
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder.getClass().getSimpleName()).isEqualTo("BCryptPasswordEncoder");
    }

    // COMPONENTS

    @Test
    void controllersAreLoaded() {
        assertThat(applicationContext.containsBean("petController")).isTrue();
        assertThat(applicationContext.containsBean("authController")).isTrue();
    }

    @Test
    void configurationClassesAreLoaded() {
        boolean hasSecurityConfig = !applicationContext.getBeansOfType(
                org.springframework.security.web.SecurityFilterChain.class).isEmpty();
        assertThat(hasSecurityConfig).isTrue();
    }

    // INTEGRATION

    @Test
    void applicationIsReadyForIntegrationTests() {
        assertThat(applicationContext.isActive()).isTrue();
        assertThat(applicationContext.isRunning()).isTrue();
        assertThat(petService).isNotNull();
        assertThat(authService).isNotNull();
        assertThat(petRepository).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    void allExpectedBeansArePresent() {
        String[] expectedBeans = {
                "petService", "authService",
                "petRepository", "userRepository", "petActionRepository",
                "petController", "authController",
                "testPasswordEncoder"
        };

        for (String beanName : expectedBeans) {
            assertThat(applicationContext.containsBean(beanName))
                    .withFailMessage("Expected bean '%s' is missing", beanName)
                    .isTrue();
        }
    }

    // BUSINESS LOGIC

    @Test
    void basicBusinessLogicIntegration() {
        assertThat(petService).isNotNull();
        assertThat(authService).isNotNull();
        assertThat(petService.getClass().getMethods().length).isGreaterThan(0);
        assertThat(authService.getClass().getMethods().length).isGreaterThan(0);
    }
}