package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletDeleteServiceImplTest {

    public static final long ID_1L = 1L;

    public static final long ID_5L = 5L;

    private static final String NAME_1 = "wallet name one";

    private static final Instant DATE_NOW = Instant.now();
    private static final Long USER_ID_1L = 1L;

    @MockBean
    private ErrorStrategy errorStrategy;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @Test
    @DisplayName("when wallet with id does not exist should not delete wallet")
    void shouldNotDeleteWallet_WhenWalletWithIdDoesNotExist() {
        //given
        User user = createTestUser();
        Wallet wallet = Wallet
                .builder().name(NAME_1)
                .id(ID_1L)
                .creationDate(DATE_NOW)
                .user(user)
                .build();

        //when
        when(walletRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> walletService.deleteWalletById(ID_5L, USER_ID_1L)).isInstanceOf(AppRuntimeException.class);
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(() -> walletService.deleteWalletById(ID_5L, USER_ID_1L))
                .withMessage(ErrorCode.W003.getBusinessMessage());
    }

    private User createTestUser() {
        return User.builder()
                .id(USER_ID_1L)
                .userName("userone")
                .email("Email@wp.pl")
                .password("password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
    }

}
