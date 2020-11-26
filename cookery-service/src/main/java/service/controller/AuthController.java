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
import java.security.Key;
import java.util.Date;

public class AuthController {
    private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";


    /*---------------------------------------------------------------- Authenticate ----------------------------------------------------------------------*/
    public User authenticate(String email, String password) {
        AuthRepository authRepository = new AuthRepository();

        User user = null;
        try {
            user = authRepository.authenticate(email, password);
        }
        catch (CookeryDatabaseException ex) {
            ex.printStackTrace();
        }

        return user;
    }

    public boolean register(User user) {
        UsersRepository usersRepository = new UsersRepository();

        try {
            return usersRepository.createUser(user);
        }
        catch (CookeryDatabaseException ex) {
            ex.printStackTrace();
            return false;
        }
    }



    public static String generateAuthToken(User user) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        boolean isAdmin = user.getRole().equals(Role.admin) ? true : false;
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
//                .setId(id)
                .setIssuedAt(now)
                .setSubject(Integer.toString(user.getId()))
//                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey)
//                .setExpiration("7 days")
                .claim("name", user.getName())
                .claim("admin", isAdmin);



        //if it has been specified, let's add the expiration
//        if (ttlMillis > 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }


    public static Claims decodeJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();

        System.out.println("Claims");
        System.out.println(claims);
        return claims;
    }


    public int getIdInToken(String auth) {
        String AUTHENTICATION_SCHEME = "Bearer"; // the scheme (value starts with Basic)
        String encodedCredentials = auth.replaceFirst(AUTHENTICATION_SCHEME + " ", ""); // remove scheme (Basic) and space

        Claims decodedToken = AuthController.decodeJWT(encodedCredentials);

        int id = Integer.parseInt(decodedToken.get("sub").toString());

        return id;
    }
}
