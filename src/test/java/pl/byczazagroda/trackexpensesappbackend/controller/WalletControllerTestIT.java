package pl.byczazagroda.trackexpensesappbackend.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import pl.byczazagroda.trackexpensesappbackend.BaseControllerTestIT;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@TestExecutionListeners(value = {
//        {TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
//        SqlScriptsTestExecutionListener.class}
//        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
//)
@Sql(scripts = {
        "classpath:db/insert_into_wallets.sql"
})
@Transactional
class WalletControllerTestIT extends BaseControllerTestIT {

    @Autowired
    private WalletController subjectUnderTest;

    @MockBean
    private WalletService walletService;

    @Test
    @DisplayName("Should say 'Hello World!'")
    void createWallet() throws Exception {

        String result = mockMvc.perform(get("/api/hello")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        assertEquals("Hello World!", result);
    }

    @Test
    void findOne() {


//        WalletDTO one1 = walletService.findOne(1L);
//
//        ResponseEntity<WalletDTO> one = subjectUnderTest.findOne(one1.id());
//
//        assertEquals(one.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void updateWallet() {
    }
}