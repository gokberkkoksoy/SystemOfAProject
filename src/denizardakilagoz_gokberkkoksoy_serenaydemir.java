/*
 * Deniz Arda KILAGÖZ 150118003
 * Gökberk KÖKSOY     150118069
 * Serenay DEMİR      150118057
 */

/*
    This program displays unsigned, signed and floating point numbers' binary representation.
 */

import java.io.FileNotFoundException;
import java.lang.StringBuilder;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.PrintWriter;

public class denizardakilagoz_gokberkkoksoy_serenaydemir {
    public static void main(String[] args) throws InputMismatchException {
        /*
        int isLittle;   indicates the byte representation
        int fpBitSize;  indicates floating point numbers' bit size specified by the user
        final int bitSize;    indicates unsigned and signed numbers' bit size
        */

        try {
        int isLittle;
        int fpBitSize;
        final int bitSize = 16;
        Scanner sc = new Scanner(System.in);
        PrintWriter writer = new PrintWriter("output.txt");

        System.out.print("Byte Ordering Type (0 for Little Endian, 1 for Big Endian): ");
        isLittle = sc.nextInt();
        do {
            if(isLittle < 0 || isLittle > 1) {
                System.out.println("Invalid input!");
            } else {
                continue;
            }
            System.out.print("Byte Ordering Type (0 for Little Endian, 1 for Big Endian): ");
            isLittle = sc.nextInt();
        } while(isLittle < 0 || isLittle > 1);

        System.out.print("Floating Point Size: ");
        fpBitSize = Integer.parseInt(sc.next()) * 8;
        do {
            if(fpBitSize < 8 || fpBitSize > 32) {
                System.out.println("Invalid input!");
            } else {
                continue;
            }
            System.out.print("Floating Point Size: ");
            fpBitSize = Integer.parseInt(sc.next()) * 8;
        } while(fpBitSize < 8 || fpBitSize > 32);
            File file = new File("input.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                if(data.contains("u")){ // if the number is unsigned
                    int unsNum = Integer.parseInt(new StringBuilder().append(data).deleteCharAt(data.length()-1).toString());
                    System.out.println(isLittle == 1 ? convertB2Hex(convertU2B(unsNum,bitSize)) : littleEndian(convertB2Hex(convertU2B(unsNum,bitSize))));
                    writer.println(isLittle == 1 ? convertB2Hex(convertU2B(unsNum,bitSize)) : littleEndian(convertB2Hex(convertU2B(unsNum,bitSize))));
                    writer.flush();
                } else if(data.contains(".")){ // is the number is floating point
                    double number = Double.parseDouble(data);
                    switch (fpBitSize) {
                        case 8:
                            System.out.println(isLittle == 1 ? convertB2Hex(convertF2B(number,3,4)) : littleEndian(convertB2Hex(convertF2B(number,3,4))));
                            writer.println(isLittle == 1 ? convertB2Hex(convertF2B(number,3,4)) : littleEndian(convertB2Hex(convertF2B(number,3,4))));
                            writer.flush();
                            break;
                        case 16:
                            System.out.println(isLittle == 1 ? convertB2Hex(convertF2B(number,8,7)) : littleEndian(convertB2Hex(convertF2B(number,8,7))));
                            writer.println(isLittle == 1 ? convertB2Hex(convertF2B(number,8,7)) : littleEndian(convertB2Hex(convertF2B(number,8,7))));
                            writer.flush();
                            break;
                        case 24:
                            System.out.println(isLittle == 1 ? convertB2Hex(convertF2B(number,10,13)) : littleEndian(convertB2Hex(convertF2B(number,10,13))));
                            writer.println(isLittle == 1 ? convertB2Hex(convertF2B(number,10,13)) : littleEndian(convertB2Hex(convertF2B(number,10,13))));
                            writer.flush();
                            break;
                        case 32:
                            System.out.println(isLittle == 1 ? convertB2Hex(convertF2B(number,12,19)) : littleEndian(convertB2Hex(convertF2B(number,12,19))));
                            writer.println(isLittle == 1 ? convertB2Hex(convertF2B(number,12,19)) : littleEndian(convertB2Hex(convertF2B(number,12,19))));
                            writer.flush();
                            break;
                    }
                } else { // signed integer
                    int number = Integer.parseInt(data);
                    System.out.println(isLittle == 1 ? convertB2Hex(convertS2B(number)) : littleEndian(convertB2Hex(convertS2B(number))));
                    writer.println(isLittle == 1 ? convertB2Hex(convertS2B(number)) : littleEndian(convertB2Hex(convertS2B(number))));
                    writer.flush();
                }
            }
            scanner.close();
            writer.close();
        } catch (FileNotFoundException | InputMismatchException e) {
            if(e instanceof InputMismatchException) {
                System.out.println("\nAn error occurred while getting the user input.");
            } else {
                System.out.println("\nAn error occurred while reading the input file");
            }
            System.out.println("\nPlease run the program again.");
        }

    }

    // Converts unsigned numbers to binary with Division algorithm.
    private static String convertU2B(int num, int bitSize) {
        StringBuilder numberStr = new StringBuilder();
        while (num > 0) {
            numberStr.append(num % 2);
            num >>= 1;
        }
        int len = numberStr.length();
        for(int i = 0;i < bitSize - len;i++){
            numberStr.insert(numberStr.length(), '0');
        }
        numberStr.reverse();
        return numberStr.toString();
    }

    // Converts binary unsigned numbers to decimal.
    private static int convertB2U(String num) {
        int result = 0;
        for(int i = num.length()-1;i >= 0;i--){
            if(num.charAt(i) == '1'){
                result += Math.pow(2,(num.length()-1)-i);
            }
        }
        return result;
    }


    // Converts signed integers to binary with Subtract Powers of Two algorithm.
    private static String convertS2B(int num) {
        StringBuilder result = new StringBuilder();
        int numResult;
        double limitPowerOf2 = 2;
        int powerIndex = 0;
        int len;
        if (num >= 0) {
            result.append(convertU2B(num, 16));
        } else {
            result.append("1");
            while(Math.abs(num) > Math.pow(limitPowerOf2,powerIndex)) {
                powerIndex++;
            }
            numResult = (int)Math.pow(2,powerIndex) * -1;
            powerIndex--;
            for(int i = powerIndex; i>=0;i--){
                if(numResult != num && numResult + (int)Math.pow(2,i) <= num) {
                    numResult += (int)Math.pow(2,i);
                    result.append("1");
                } else {
                    result.append("0");
                }
            }
            len = result.length();
            for(int i = 0; i < 16 - len; i++){
                result.insert(0, '1');
            }
        }
        return result.toString();
    }

    // Converts floating point numbers to binary.
    private static String convertF2B(double num, int exponentSize, int mantissaSize) {
        String[] tokens = Double.toString(num).split("\\.");
        StringBuilder decimalPartStr = new StringBuilder();
        StringBuilder fractionPartStr = new StringBuilder();
        StringBuilder exponentStr = new StringBuilder();
        StringBuilder mantissaStr = new StringBuilder(); // decimalStr +  fractionStr
        StringBuilder result = new StringBuilder(); // exponentStr + mantissaStr
        int decimal = Math.abs(Integer.parseInt(tokens[0]));
        int bias = (int)Math.pow(2,exponentSize-1) - 1; // 2^(k-1) - 1
        int exponent = 0;
        double fraction = Double.parseDouble("0." + tokens[1]);
        int mantissaIndex = 0;
        if(-1 < num && num < 1) {
            // num is +-0.xxxxxx
            decimalPartStr.append(0);
            getFraction(fractionPartStr, fraction, mantissaIndex);
            int length = fractionPartStr.length();
            for(int i = 0; i < length;i++){
                if(fractionPartStr.charAt(0) == '0'){
                    fractionPartStr.deleteCharAt(0);
                    exponent--;
                } else {
                    break;
                }
            }

            exponent += bias-1;
            exponentStr.append(convertU2B(exponent,exponentSize));
            if(exponent != 0) {
                fractionPartStr.deleteCharAt(0);
            }
            mantissaStr.append(fractionPartStr);
            if(mantissaStr.length() < mantissaSize) {
                while(mantissaStr.length()<mantissaSize){
                    mantissaStr.append(0);
                }
            } else {
                int decMantissa;
                while(mantissaSize < mantissaStr.length()-1) { // chops the extra bits (not gonna used)
                    mantissaStr.deleteCharAt(mantissaStr.length()-1);
                }
                if(mantissaStr.charAt(mantissaStr.length()-1) == '0') {
                    mantissaStr.deleteCharAt(mantissaStr.length()-1);
                } else {
                    mantissaStr.deleteCharAt(mantissaStr.length()-1);
                    if(mantissaSize >= 13) {
                        decMantissa = convertB2U(mantissaStr.toString()) + 1;
                        mantissaStr.replace(0,mantissaStr.length(), "");
                        mantissaStr.append(convertU2B(decMantissa,mantissaSize));
                    }
                }
            }
            result.append(num < 0 ? 1 : 0).append(exponentStr).append(mantissaStr);
            return result.toString();
        } else {
            // obtain the xxxxxx. part
            while(decimal > 0){
                decimalPartStr.append(decimal % 2);
                decimal >>= 1;
            }
            decimalPartStr.reverse();
            getFraction(fractionPartStr, fraction, mantissaIndex);
            // 1.xxxxx obtained
            exponent = bias + decimalPartStr.length()-1;
            exponentStr.append(convertU2B(exponent,exponentSize));
            for(int i = exponentStr.length()-1; i >= exponentSize;i--){
                exponentStr.deleteCharAt(i);
            }
            mantissaStr.append(decimalPartStr.deleteCharAt(0)).append(fractionPartStr);
            if(mantissaStr.length() > mantissaSize) {
                int decMantissa;
                for(int i = mantissaStr.length()-1; i > mantissaSize;i--){
                    mantissaStr.deleteCharAt(i);
                }
                if(mantissaStr.charAt(mantissaStr.length()-1) == '1')  {
                    // rounding operation
                    mantissaStr.deleteCharAt(mantissaStr.length()-1);
                    if(mantissaSize >= 13) {
                        decMantissa = convertB2U(mantissaStr.toString()) + 1;
                        mantissaStr.replace(0,mantissaStr.length(), "");
                        mantissaStr.append(convertU2B(decMantissa,mantissaSize));
                    }
                } else {
                    mantissaStr.deleteCharAt(mantissaStr.length()-1);
                }
            } else {
                for(int i = mantissaStr.length();i < mantissaSize;i++) {
                    mantissaStr.append(0);
                }
            }
            result.append(num > 0 ? 0 : 1).append(exponentStr).append(mantissaStr);
        }
        return result.toString();
    }
    // Gets the binary value of floating point numbers latter part.
    private static void getFraction(StringBuilder fractionPartStr, double fraction, int mantissaIndex) {
        if(fraction == 0.0){
            fractionPartStr.append(0);
            return;
        }
        while(fraction != 1.0) {
            mantissaIndex++;
            fraction *= 2;
            if (fraction > 1.0) {
                fractionPartStr.append(1);
                fraction -= 1.0;
            } else if (fraction < 1.0){
                fractionPartStr.append(0);
            } else {
                fractionPartStr.append(1);
            }
        }
    }
    // Hexadecimal representation of binary numbers
    private static String convertB2Hex(String num) {
        StringBuilder hexResult = new StringBuilder();
        StringBuilder number = new StringBuilder();
        number.append(num);
        for(int i = num.length(); i > 0; i -= 4) {
            number.insert(i, ' ');
        }
        String [] tokens = number.toString().split(" ");
        for (String token: tokens) {
            hexResult.append(hexMapping(convertB2U(token)));
        }
        for(int i = hexResult.length();i> 0;i-=2) {
            hexResult.insert(i,' ');
        }
        return hexResult.toString();
    }

    // Displays the hexadecimal numbers according to the little endian representation.
    private static String littleEndian(String hex) {
        StringBuilder result = new StringBuilder();
        String[] tokens = hex.split(" ");
        for(int i = tokens.length-1;i >= 0;i--){
            result.append(tokens[i]);
            result.append(" ");
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }

    // Utility function to get hexadecimal character for 4 bits.
    private static String hexMapping(int num) {
        switch (num) {
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            default:
                return "" + num;
        }
    }

}
