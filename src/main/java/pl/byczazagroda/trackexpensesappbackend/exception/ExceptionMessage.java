package pl.byczazagroda.trackexpensesappbackend.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception message send to api client
 */
public class ExceptionMessage {

    //business Errors
    public static final String I001 = "INCOME_NOT_FOUND";
    public static final String W001 ="WALLET_NOT_FOUND";

    public static final int CODE_NOT_FOUND =404;
    public static final int CODE_NOT_FOUND_2 =400;
    public static final int CODE_BAD_REQUEST = 400;

    private Integer code;
    private String status;

    public ExceptionMessage(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
