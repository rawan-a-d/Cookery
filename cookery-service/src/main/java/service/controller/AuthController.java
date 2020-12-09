package service.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.logging.Logger;

public class AuthController {
    private final static Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    private static String secretKey = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
    AuthRepository authRepository = new AuthRepository();
    UsersRepository usersRepository = new UsersRepository();

    /*---------------------------------------------------------------- Authenticate ----------------------------------------------------------------------*/
    public User authenticate(String email, String password) {
        User user = null;
        try {
            user = authRepository.authenticate(email, password);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return user;
    }

    public boolean register(User user) {
        try {
            return usersRepository.createUser(user);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }



    public static String generateAuthToken(User user) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        boolean isAdmin = user.getRole().equals(Role.admin);

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(Integer.toString(user.getId()))
                .signWith(signatureAlgorithm, signingKey)
                .claim("name", user.getName())
                .claim("admin", isAdmin);


        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }


    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(jwt).getBody();

        return claims;
    }


    public int getIdInToken(String auth) {
        String authenticationScheme = "Bearer"; // the scheme (value starts with Basic)
        String encodedCredentials = auth.replaceFirst(authenticationScheme + " ", ""); // remove scheme (Basic) and space

        Claims decodedToken = AuthController.decodeJWT(encodedCredentials);

        return Integer.parseInt(decodedToken.get("sub").toString());
    }
}
