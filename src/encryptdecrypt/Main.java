/**
 * @author Yaroslav Knyazev aka lacelay
 *
 * This is only test application! I made this for improving my skills.
 */

package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ObjectWithData objectWithData = new ObjectWithData(args);

        CryptAlgorithmSelector algorithmSelector = null;
        OutputAlgorithmSelector algorithmSelector1 = null;

        for (int j = 0; j < args.length; j++) {
            switch (args[j]) {
                case "-alg":
                    switch (args[++j]) {
                        case "shift":
                            algorithmSelector = new CryptAlgorithmSelector(new Shift());
                            break;
                        case "unicode":
                            algorithmSelector = new CryptAlgorithmSelector(new Unicode());
                            break;
                    }
                    break;
                case "-out":
                    algorithmSelector1 = new OutputAlgorithmSelector(new Writer());
                    break;
            }
        }

        if (algorithmSelector1 == null) {
            algorithmSelector1 = new OutputAlgorithmSelector(new Printer());
        }

        if (algorithmSelector != null) {
            algorithmSelector1.output(new OutputData(algorithmSelector.crypt(objectWithData), args));
        } else {
            System.out.println("There are not such crypt algorithm!");
        }
    }
}

interface CryptAlgorithm {

    char[] crypt(ObjectWithData object);
}

class CryptAlgorithmSelector {

    private CryptAlgorithm algorithm;

    CryptAlgorithmSelector(CryptAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    char[] crypt(ObjectWithData object) {
        return this.algorithm.crypt(object);
    }
}

class LettersCaseChecker {

    boolean inUpperCase(char letter) {
        return letter >= 65 && letter <= 90 ? true : false;
    }

    boolean inLowerCase(char letter) {
        return letter >= 97 && letter <= 122 ? true : false;
    }
}

class ObjectWithData {

    private char[] data;

    char[] getData() {
        return data;
    }

    private int key = 0;

    int getKey() {
        return key;
    }

    String mode = "enc";

    String getMode() {
        return mode;
    }

    ObjectWithData(String[] args) {
        for (int j = 0; j < args.length; j++) {
            switch (args[j]) {
                case "-key":
                    this.key = Integer.parseInt(args[++j]);
                    break;
                case "-mode":
                    this.mode = args[++j];
                    break;
                case "-data":
                    this.data = args[++j].toCharArray();
                    break;
                case "-in":
                    if (data == null) {
                        try (Scanner scanner = new Scanner(new File(args[++j]))) {
                            data = scanner.nextLine().toCharArray();
                        } catch (FileNotFoundException e) {
                            System.out.println("This file is not exist.");
                        }
                    }
                    break;
            }
        }

        if (data == null) {
            System.out.println("Enter your message below:");
            data = new Scanner(System.in).nextLine().toCharArray();
        }
    }
}

interface OutputAlgorithm {

    void output(OutputData data);
}

class OutputAlgorithmSelector {

    private OutputAlgorithm algorithm;

    public OutputAlgorithmSelector(OutputAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void output(OutputData data) {
        this.algorithm.output(data);
    }
}

class OutputData {

    private char[] data;

    public char[] getData() {
        return data;
    }

    private FileWriter fileWriter;

    public FileWriter getFileWriter() {
        return fileWriter;
    }

    public OutputData(char[] crypytedData, String[] args) {

        this.data = crypytedData;

        for (int j = 0; j < args.length; j++) {
            switch (args[j]) {
                case "-out":
                    try {
                        fileWriter = new FileWriter(new File(args[++j]));
                        break;
                    } catch (IOException e) {
                        System.out.println(e.getCause() + " : " + e.getMessage());
                    }
            }
        }
    }
}

class Printer implements OutputAlgorithm {

    @Override
    public void output(OutputData data) {
        for (char character:
                data.getData()) {
            System.out.print(character);
        }
    }
}

class Shift implements CryptAlgorithm {

    char[] cryptedData;

    @Override
    public char[] crypt(ObjectWithData object) {

        this.cryptedData = object.getData();

        for (int j = 0; j < cryptedData.length; j++) {
            if (new LettersCaseChecker().inUpperCase(cryptedData[j])) {
                switch (object.getMode()) {
                    case "dec":
                        cryptedData[j] -= cryptedData[j] + object.getKey() > 65 ? object.getKey() - 26 : object.getKey();
                        break;
                    default:
                        cryptedData[j] += cryptedData[j] + object.getKey() > 90 ? object.getKey() - 26 : object.getKey();
                        break;
                }
            }
            if (new LettersCaseChecker().inLowerCase(cryptedData[j])) {
                switch (object.getMode()) {
                    case "dec":
                        cryptedData[j] -= cryptedData[j] - object.getKey() < 97 ? object.getKey() - 26 : object.getKey();
                        break;
                    default:
                        cryptedData[j] += cryptedData[j] + object.getKey() > 122 ? object.getKey() - 26 : object.getKey();
                        break;
                }
            }
        }

        return cryptedData;
    }
}

class Unicode implements CryptAlgorithm {

    char[] cryptedData;

    @Override
    public char[] crypt(ObjectWithData object) {

        this.cryptedData = object.getData();

        for (int j = 0; j < cryptedData.length; j++) {
            cryptedData[j] = object.getMode().equals("dec") ? (char) (cryptedData[j] - object.getKey()) : (char) (cryptedData[j] + object.getKey());
        }

        return cryptedData;
    }
}

class Writer implements OutputAlgorithm {

    @Override
    public void output(OutputData data) {
        try(FileWriter fw = data.getFileWriter()) {
            for (char character:
                    data.getData()) {
                fw.write(character);
            }
        } catch (IOException e) {
            System.out.println(e.getCause() + " : " + e.getMessage());
        }
    }
}