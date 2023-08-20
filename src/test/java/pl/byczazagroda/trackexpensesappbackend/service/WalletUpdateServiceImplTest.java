package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletUpdateServiceImplTest {

    public static final long WALLET_ID_1L = 1L;

    public static final long USER_ID_1L = 1L;

    private static final String NAME_1 = "wallet name one";

    private static final String NAME_2 = "wallet name two";

    private static final Instant DATE_NOW = Instant.now();

    @MockBean
    private ErrorStrategy errorStrategy;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @Test
    @DisplayName("when finding wallet by id should update wallet")
    void shouldUpdateWallet_whenFindWalletById() {
        // given
        User user = createTestUser();

        WalletUpdateDTO walletUpdateDto = new WalletUpdateDTO(NAME_1);

        Wallet wallet = Wallet.builder()
                .id(WALLET_ID_1L)
                .name(NAME_2)
                .creationDate(DATE_NOW)
                .user(user)
                .build();

        WalletDTO newWalletDTO = new WalletDTO(WALLET_ID_1L, NAME_1, DATE_NOW, USER_ID_1L);
        given(walletRepository.findById(WALLET_ID_1L)).willReturn(Optional.of(wallet));
        given(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).willReturn(newWalletDTO);

        // when
        WalletDTO walletDTO = walletService.updateWallet(WALLET_ID_1L, walletUpdateDto, USER_ID_1L);

        // then
        assertThat(walletDTO.name()).isEqualTo(walletUpdateDto.name());
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
