package com.liondevs.fastfood.authorizationserver.auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liondevs.fastfood.authorizationserver.auth.dto.AuthenticationRequest;
import com.liondevs.fastfood.authorizationserver.auth.dto.AuthenticationResponse;
import com.liondevs.fastfood.authorizationserver.auth.dto.RegisterRequest;
import com.liondevs.fastfood.authorizationserver.auth.dto.ValidateTokenResponse;
import com.liondevs.fastfood.authorizationserver.exceptions.UserNotFoundException;
import com.liondevs.fastfood.authorizationserver.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;


import static org.mockito.Mockito.*;


@TestPropertySource("/application.yml")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authService;


    private AuthenticationResponse response(RegisterRequest request){
        Map<String, Object> userData = new HashMap<>();
        userData.put("email",request.getEmail());
        userData.put("phone",request.getPhone());
        String token =  "testToken";
        return  AuthenticationResponse.builder()
                .token(token)
                .userData(userData)
                .build();
    }
    private AuthenticationResponse response(AuthenticationRequest request){
        Map<String, Object> userData = new HashMap<>();
        userData.put("email",request.getEmail());
        userData.put("phone",request.getPassword());
        String token =  "testToken";
        return  AuthenticationResponse.builder()
                .token(token)
                .userData(userData)
                .build();
    }
    private ValidateTokenResponse validateTokenResponse(){
        return ValidateTokenResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .email("test@gmail.com")
                .idUser(1L)
                .isValidToken(true)
                .build();

    }
    @DisplayName("Should return valid Object createUser")
    @Test
    void createUser_whenInputValuesProvidedIsCorrect_thenReturnValidUser() throws Exception {
        //Arrange
        RegisterRequest userDetailsRequestModel = new RegisterRequest();
        userDetailsRequestModel.setEmail("test@gmail.com");
        userDetailsRequestModel.setPhone("3208046584");
        userDetailsRequestModel.setFirstName("Kevin");
        userDetailsRequestModel.setLastName("Romero");
        userDetailsRequestModel.setPassword("12345678");

        final AuthenticationResponse response = response(userDetailsRequestModel);
        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        //Act

        MvcResult mvcResult =  mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        AuthenticationResponse createUser = new ObjectMapper().readValue(responseBodyAsString, AuthenticationResponse.class);

        //Then
        Assertions.assertEquals(HttpStatus.OK.value(),mvcResult.getResponse().getStatus());
        Assertions.assertEquals(response.getToken(), createUser.getToken());
        Assertions.assertEquals(response.getUserData().get("phone"), createUser.getUserData().get("phone"));
        Assertions.assertEquals(response.getUserData().get("email"), createUser.getUserData().get("email"));

    }
    @DisplayName("Should return valid response and valid Token")
    @Test
    void authenticate_shouldReturnValidResponse_WhenInputProviderIsValid() throws Exception {
        //Arrange
        AuthenticationRequest userDetailsRequestModel = new AuthenticationRequest();
        userDetailsRequestModel.setEmail("test@gmail.com");
        userDetailsRequestModel.setPassword("12345678");

        final AuthenticationResponse response = response(userDetailsRequestModel);
        when(authService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));


        //Act
            MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
            String responseBodyAsString = mvcResult.getResponse().getContentAsString();
            AuthenticationResponse authUser =  new ObjectMapper().readValue(responseBodyAsString, AuthenticationResponse.class);

        //Assert
        Assertions.assertEquals(HttpStatus.OK.value(),mvcResult.getResponse().getStatus());
        Assertions.assertEquals(response.getToken(), authUser.getToken());
        Assertions.assertEquals(response.getUserData().get("phone"), authUser.getUserData().get("phone"));
        Assertions.assertEquals(response.getUserData().get("email"), authUser.getUserData().get("email"));

    }

    @DisplayName("When email is not provided")
    @Test
    void testAuthUser_whenEmailIsNotProvided_returns400StatusCode() throws Exception {

        //Arrange
        AuthenticationRequest userDetailsRequestModel = new AuthenticationRequest();
        userDetailsRequestModel.setEmail("");
        userDetailsRequestModel.setPassword("12345678");

        RequestBuilder requestBuilder =  MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));


        //When
        MvcResult mvcResult =  mockMvc.perform(requestBuilder).andReturn();


        //Assert

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),mvcResult.getResponse().getStatus());

    }

    @DisplayName("When JWT is Valid")
    @Test
    void testValidateToken_whenProvidedTokenIsValid_shouldReturnStatus200() throws Exception {
        //Arrange
        String token  = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZXZpbi5mZW9AZ2xvYmFudC5jb20iLCJpYXQiOjE2Nzc4ODcyMDgsImV4cCI6MTY3Nzg4NzgwOH0.f6-_cq66F4_jySfoe4rzGtP6m3JFkJ2RzajButT69u0";

        when(authService.validateToken(token)).thenReturn(validateTokenResponse());
        RequestBuilder requestBuilder =  MockMvcRequestBuilders.post("/api/v1/auth/validate-token")
                .param("token",token);
        //Act

        MvcResult resultRequest = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = resultRequest.getResponse().getContentAsString();
        ValidateTokenResponse validateToken =  new ObjectMapper().readValue(responseBodyAsString, ValidateTokenResponse.class);

        //Assert
        Assertions.assertTrue(validateToken.isValidToken());

    }
    @DisplayName("Token is not provided")
    @Test
   void testValidationToken_whenIsNotProvidedToken_shouldReturnBadRequestException() throws Exception {
        //Arrange
         //  when(authService.validateToken("")).thenThrow(UserNotFoundException.class);
               RequestBuilder requestBuilder =  MockMvcRequestBuilders.post("/api/v1/auth/validate-token");


        //Act
            MvcResult resultRequest = mockMvc.perform(requestBuilder).andReturn();

        //Assert
            Assertions.assertEquals(resultRequest.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());


    }

    @DisplayName("Invalid Token")
    @Test
    void testValidationToken_whenProvidedTokenIsInvalid_shouldReturnUserNotFoundException() throws Exception {
        //Arrange
        when(authService.validateToken("failedToken")).thenThrow(UserNotFoundException.class);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders.post("/api/v1/auth/validate-token")
                .param("token","failedToken");


        //Act
        MvcResult resultRequest = mockMvc.perform(requestBuilder).andReturn();

        //Assert
        Assertions.assertEquals(resultRequest.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());


    }

}
