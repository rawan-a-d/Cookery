package service.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import service.model.DTO.UserDTO;
import service.model.Role;
import service.repository.AuthRepository;
import service.repository.CookeryDatabaseException;
import service.repository.UsersRepository;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.net.URISyntaxException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.logging.Logger;

public class AuthController {
    private final static Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    private static String secretKey = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
    AuthRepository authRepository = new AuthRepository();
    UsersRepository usersRepository = new UsersRepository();

    /*---------------------------------------------------------------- Authenticate ----------------------------------------------------------------------*/
    public UserDTO authenticate(String email, String password) {
        UserDTO userDTO = null;
        try {
            // Encrypt password
            String encrypted = UsersRepository.doHashing(password);

            userDTO = authRepository.authenticate(email, encrypted);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return userDTO;
    }

//    public UserDTO register(User user) {
//        UserDTO userDTO = null;
//        try {
//
//            userDTO = usersRepository.createUser(user);
//        }
//        catch (CookeryDatabaseException | URISyntaxException ex) {
//            LOGGER.info(ex.getMessage()); // Compliant
//        }
//        return userDTO;
//    }



    public static String generateAuthToken(UserDTO user) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
//        Date oneWeek = Date.from(ZonedDateTime.now().plusHours(24).toInstant());
        Date oneWeek = Date.from(ZonedDateTime.now().plusHours(5).toInstant());

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        boolean isAdmin = user.getRole().equals(Role.admin);

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(oneWeek)
                .setSubject(Integer.toString(user.getId()))
                .signWith(signatureAlgorithm, signingKey)
                .claim("name", user.getName())
                .claim("admin", isAdmin);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }


    public static Claims decodeJWT(String jwt) {
//        System.out.println("Trying to decode");
//
//        System.out.println("JWT " + jwt);
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(jwt).getBody();

//        System.out.println("Claims " + claims);

        return claims;
    }


    public static boolean isTokenValid(Claims jwt) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
//        System.out.println("Token hoho");

        return jwt.getExpiration().after(now);
    }


    public static int getIdInToken(String auth) {
        String authenticationScheme = "Bearer"; // the scheme (value starts with Basic)
        String encodedCredentials = auth.replaceFirst(authenticationScheme + " ", ""); // remove scheme (Basic) and space
        Claims decodedToken = AuthController.decodeJWT(encodedCredentials);

        return Integer.parseInt(decodedToken.get("sub").toString());
    }

    public static UserDTO getUser(String auth) {
        String authenticationScheme = "Bearer"; // the scheme (value starts with Basic)
        String encodedCredentials = auth.replaceFirst(authenticationScheme + " ", ""); // remove scheme (Basic) and space
        Claims decodedToken = AuthController.decodeJWT(encodedCredentials);

        int id = Integer.parseInt(decodedToken.get("sub").toString());
        String name = decodedToken.get("name").toString();
        Role role = Boolean.parseBoolean(decodedToken.get("admin").toString()) ? Role.admin : Role.user;

        UserDTO user = new UserDTO(id, name, role);
        return user;
    }
}
