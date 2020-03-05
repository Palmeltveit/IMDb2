package algorithms;

public class ProfessionalHashingAlgorithm {

    public static String encrypt(String text, int encryptionStrength) {
        StringBuilder result = new StringBuilder();

        for (int i=0; i<text.length(); i++) {

            if (Character.isUpperCase(text.charAt(i))) {
                char c = (char)(((int)text.charAt(i) +
                        encryptionStrength - 65) % 26 + 65);
                result.append(c);
            } else {
                char c = (char)(((int)text.charAt(i) +
                        encryptionStrength - 97) % 26 + 97);
                result.append(c);
            }
        }

        return result.toString();
    }

}
