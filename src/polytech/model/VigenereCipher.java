/**
 * 
 */
package polytech.model;

/**
 * @author evant
 *
 */
public class VigenereCipher {

	    public static String encrypt(String plainText, long key) {
	        // Convertir la clé en une chaîne de lettres de l'alphabet
	        String keyString = convertKeyToString(key);

	        StringBuilder cipherText = new StringBuilder();
	        int keyLength = keyString.length();

	        for (int i = 0; i < plainText.length(); i++) {
	            char currentChar = plainText.charAt(i);

	            if (Character.isLetter(currentChar)) {
	                char keyChar = keyString.charAt(i % keyLength);
	                char encryptedChar = encryptChar(currentChar, keyChar);
	                cipherText.append(encryptedChar);
	            } else {
	                // Ne pas chiffrer les caractères non alphabétiques
	                cipherText.append(currentChar);
	            }
	        }

	        return cipherText.toString();
	    }

	    private static char encryptChar(char plainChar, char keyChar) {
	        // Décaler le caractère selon la clé
	        int shift = keyChar - 'A';
	        char base = Character.isLowerCase(plainChar) ? 'a' : 'A';

	        // Appliquer le chiffrement de Vigenère
	        return (char) (((plainChar - base + shift) % 26) + base);
	    }

	    private static String convertKeyToString(long key) {
	        // Convertir la clé en une chaîne de lettres de l'alphabet
	        StringBuilder keyString = new StringBuilder();
	        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	        while (key > 0) {
	            int index = (int) (key % 26);
	            keyString.insert(0, alphabet.charAt(index));
	            key /= 10;
	        }

	        return keyString.toString();
	    }

	    public static String decrypt(String cipherText, long key) {
	        // Convertir la clé en une chaîne de lettres de l'alphabet
	        String keyString = convertKeyToString(key);

	        StringBuilder decryptedText = new StringBuilder();
	        int keyLength = keyString.length();

	        for (int i = 0; i < cipherText.length(); i++) {
	            char currentChar = cipherText.charAt(i);

	            if (Character.isLetter(currentChar)) {
	                char keyChar = keyString.charAt(i % keyLength);
	                char decryptedChar = decryptChar(currentChar, keyChar);
	                decryptedText.append(decryptedChar);
	            } else {
	                // Ne pas déchiffrer les caractères non alphabétiques
	                decryptedText.append(currentChar);
	            }
	        }

	        return decryptedText.toString();
	    }

	    private static char decryptChar(char cipherChar, char keyChar) {
	        // Décaler le caractère selon la clé
	        int shift = keyChar - 'A';
	        char base = Character.isLowerCase(cipherChar) ? 'a' : 'A';

	        // Appliquer le déchiffrement de Vigenère
	        int decryptedChar = (cipherChar - base - shift + 26) % 26;
	        return (char) (decryptedChar + base);
	    }
}
