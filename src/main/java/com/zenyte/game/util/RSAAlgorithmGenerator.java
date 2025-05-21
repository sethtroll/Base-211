package com.zenyte.game.util;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Logger;

/**
 * A class that handles the creation of the 1024 bit RSA algorithm.
 * This class only generates 1024 bit based public and private key pairs.
 *
 * @author Eobard Cowan
 */
public class RSAAlgorithmGenerator {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RSAAlgorithmGenerator.class);
    /**
     * The name of the algorithm.
     */
    private static final String ALGORITHM = "RSA";
    /**
     * The singleton instance of this class.
     */
    public static RSAAlgorithmGenerator singleton;

    /**
     * Starts up a new application.
     *
     * @param params Any program arguments.
     */
    public static void main(String[] params) {
        if (params.length != 0) throw new IllegalArgumentException("Program arguments are not allowed at start up!");
        getSingleton().generate();
    }

    /**
     * @return The singleton instance.
     */
    public static RSAAlgorithmGenerator getSingleton() {
        return (singleton == null ? singleton = new RSAAlgorithmGenerator() : singleton);
    }

    /**
     * Generates a set of rsa pair keys. One pair is public the other is private.
     */
    public void generate() {
        try {
            /**
             * Gets the generator singleton instance.
             */
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            /**
             * Generate 1024 bit rsa pair keys.
             */
            generator.initialize(1024);
            /**
             * Gets the pair of keys (private and public).
             */
            KeyPair pair = generator.generateKeyPair();
            /**
             * Represents the public key.
             */
            PublicKey publicKey = pair.getPublic();
            /**
             * Represents the private key.
             */
            PrivateKey privateKey = pair.getPrivate();
            /**
             * Gets the key factory singleton instance.
             */
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            /**
             * Creates a new {@link RSAPublicKeySpec}.
             */
            RSAPublicKeySpec publicSpec = factory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            /**
             * Creates a new {@link RSAPrivateKeySpec}.
             */
            RSAPrivateKeySpec privateSpec = factory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
            /**
             * Writes the pairs onto a file for users to view.
             */
            writeToFile(ALGORITHM + "-public", publicSpec.getPublicExponent(), publicSpec.getModulus());
            writeToFile(ALGORITHM + "-private", privateSpec.getPrivateExponent(), privateSpec.getModulus());
        } catch (
        /**
         * If the algorithm name we put in the generator doesn't exist.
         */
                NoSuchAlgorithmException ne) {
            log.error("", ne);
        } catch (
        /**
         * If an error with the public or private specs processing occurs.
         */
                InvalidKeySpecException ie) {
            log.error("", ie);
        }
    }

    /**
     * Writes the rsa encryption data onto a file.
     *
     * @param name     The name of the file.
     * @param exponent The rsa exponent.
     * @param modulus  The rsa modulus.
     */
    private void writeToFile(String name, BigInteger exponent, BigInteger modulus) {
        /**
         * Represents a new file.
         */
        File file = new File(name + ".txt");
        try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
            /**
             * Print out the data received.
             */
            writer.println("private static final BigInteger RSA_EXPONENT = new BigInteger(\"" + exponent + "\");");
            writer.println("private static final BigInteger RSA_MODULUS = new BigInteger(\"" + modulus + "\");");
            /**
             * Close the writer.
             */
            writer.close();
            /**
             * Print out where the file is located at.
             */
            Logger.getLogger("RSAAlgorithmGenerator").info("Finished writing: " + name + " key pair! [Located: " + file.getAbsolutePath() + "]");
        } catch (
        /**
         * Any problems finding the file.
         */
                FileNotFoundException fe) {
            log.error("", fe);
        } catch (
        /**
         * Any io file related problems.
         */
                IOException ie) {
            log.error("", ie);
        }
    }
}
