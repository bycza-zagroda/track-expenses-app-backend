package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

  @InjectMocks
  WalletServiceImpl underTest;

  @Mock
  WalletRepository walletRepository;

  @Test
  void shouldCreateWallet(){
    //given

    //when

    //then
    assertEquals(true, true);
  }
}
