package pl.byczazagroda.trackexpensesappbackend.controller;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import pl.byczazagroda.trackexpensesappbackend.BaseControllerTestIT;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestExecutionListeners(value = {
//        {TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class}
//        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@Sql(scripts = {
        "classpath:db/insert_into_wallet.sql"
})
@Transactional
class WalletControllerTestIT extends BaseControllerTestIT {

    @InjectMocks
    private WalletController subjectUnderTest;

    @MockBean
    private WalletService walletService;

    @Test
    void createWallet() {
    }

    @Test
    void findOne() {


        WalletDTO one1 = walletService.findOne(1L);

        ResponseEntity<WalletDTO> one = subjectUnderTest.findOne(one1.id());

        assertEquals(one.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void updateWallet() {
    }
}