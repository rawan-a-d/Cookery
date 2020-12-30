package controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.controller.AuthController;
import service.model.DTO.UserDTO;
import service.model.Role;
import service.model.User;
import service.repository.AuthRepository;
import service.repository.CookeryDatabaseException;
import service.repository.UsersRepository;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.net.URISyntaxException;
import java.security.Key;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks
    AuthController authController;

    @Mock
    AuthRepository authRepository;

    @Mock
    UsersRepository usersRepository;

    @Test
    public void authenticate() throws URISyntaxException, CookeryDatabaseException {
        when(authRepository.authenticate("rawan@gmail.com", "1234")).thenReturn(
                new UserDTO(1, "Rawan", "rawan@gmail.com", Role.admin)
        );

        UserDTO actualUser = authController.authenticate("rawan@gmail.com", "1234");

        assertEquals(new UserDTO(1, "Rawan", "rawan@gmail.com", Role.admin), actualUser);
    }


    @Test
    public void register() throws URISyntaxException, CookeryDatabaseException {
        User newUser = new User("Denys Johnson", "denys@gmail.com", "Qw1234576@");

        UserDTO expectedUser = new UserDTO(5, "Denys Johnson", "denys@gmail.com", Role.user);

        when(usersRepository.createUser(newUser)).thenReturn(expectedUser);

        UserDTO user = authController.register(newUser);

        assertEquals(expectedUser, user);
    }


//    @Test
//    public void generateAuthToken() {
//        String expectedToken = generateToken(1, "Rawan", true);
//
//        String actualToken = AuthController.generateAuthToken(new User(1, "Rawan", "rawan@gmail.com", "1234", Role.admin));
//
//        assertEquals(expectedToken, actualToken);
//    }


    @Test
    public void decodeJWT() {
        String token = generateToken(1, "Rawan", true);

        Claims claims = AuthController.decodeJWT(token);

        assertEquals("1", claims.get("sub"));
        assertEquals("Rawan", claims.get("name"));
        assertEquals(true, claims.get("admin"));
    }


    @Test
    public void decodeJWT_invalidToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MDc1MzQ5MTAsInN1YiI6IjEiLCJuYW1lIjoiJ1Jhd2FuJyIsImFkbWluIjp0cnVlfo.KrU_OLWXXPb5-INQTAS0ZckWELj1T8CuhEN3AY-5MBM";

        assertThrows(io.jsonwebtoken.SignatureException.class, () ->
                AuthController.decodeJWT(token)
        );
    }


    @Test
    public void getIdInToken() {
        String auth = "Bearer ";
        auth += generateToken(1, "Rawan", true);

        int actualId = authController.getIdInToken(auth);

        assertEquals(1, actualId);
    }



    private String generateToken(int id, String name, boolean isAdmin) {
        String secretKey = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(Integer.toString(id))
                .signWith(signatureAlgorithm, signingKey)
                .claim("name", name)
                .claim("admin", isAdmin);


        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
