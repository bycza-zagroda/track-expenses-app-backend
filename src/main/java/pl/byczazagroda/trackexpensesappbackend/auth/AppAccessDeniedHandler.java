package pl.byczazagroda.trackexpensesappbackend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.general.exception.error.ErrorResponseDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AppAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponseDTO dto = new ErrorResponseDTO(
                ErrorCode.S002.getBusinessStatus(),
                ErrorCode.S002.getBusinessMessage(),
                ErrorCode.S002.getBusinessMessage(),
                ErrorCode.S002.getBusinessStatusCode());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(dto));
        response.getWriter().flush();
        response.getWriter().close();
    }

}
