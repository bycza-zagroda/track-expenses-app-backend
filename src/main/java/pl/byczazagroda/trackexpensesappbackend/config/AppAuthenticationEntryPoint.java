package pl.byczazagroda.trackexpensesappbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import pl.byczazagroda.trackexpensesappbackend.dto.error.ErrorResponseDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AppAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException auth) throws IOException, ServletException {
        ErrorResponseDTO dto = new ErrorResponseDTO(
                ErrorCode.S001.getBusinessStatus(),
                ErrorCode.S001.getBusinessMessage(),
                ErrorCode.S001.getBusinessMessage(),
                ErrorCode.S001.getBusinessStatusCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(dto));
        response.getWriter().flush();
        response.getWriter().close();
    }

}
