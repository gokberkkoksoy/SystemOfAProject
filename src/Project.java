import java.lang.StringBuilder;

//TODO:
// - Floating point to binary method

public class Project {
    public static void main(String[] args) {
        int num = 86;
        int bitSize = 16; // fixed for signed and unsigned
        //System.out.println("binary rep of uns num is: " + convertU2B(num,bitSize));
        //System.out.println("its hex rep is: " + convertB2Hex(convertU2B(num, bitSize)));
        convertF2B(29.109375, 8,  32);
    }

    // unsigned to bits WORKS WELL
    static String convertU2B(int num, int bitSize) {
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

    //bits to unsigned decimal WORKS WELL
    static int convertB2U(String num) {
        int result = 0;
        for(int i = num.length()-1;i >= 0;i--){
            if(num.charAt(i) == '1'){
                result += Math.pow(2,(num.length()-1)-i);
            }
        }
        return result;
    }


    // signed decimal to binary WORKS WELL
    static String convertS2B(int num, int bitSize) {
        StringBuilder result = new StringBuilder();
        int numResult;
        double limitPowerOf2 = 2;
        int powerIndex = 0;
        int len;
        if (num >= 0) {
            result.append(convertU2B(num, bitSize));
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
            for(int i = 0;i < bitSize - len;i++){
                result.insert(0, '1');
            }
        }
        return result.toString();
    }

    // bits to signed decimal WORKS WELL
    static int convertB2S(String num) {
        int result = 0;
        if(num.charAt(0) == '0') {
            result = convertB2U(num);
        } else {
            result += Math.pow(2,num.length()-1) * -1;
            for(int i = 1; i < num.length();i++){
                if(num.charAt(i) == '1') {
                    result += Math.pow(2,num.length()-1-i);
                }
            }
        }
        System.out.println(result);
        return result;
    }

    static String convertF2B(double num, int exponentSize, int mantissaSize) {
        StringBuilder result = new StringBuilder();
        StringBuilder decimalPartStr = new StringBuilder();
        StringBuilder fractionPartStr = new StringBuilder();
        StringBuilder exponentStr = new StringBuilder();
        StringBuilder mantissaStr = new StringBuilder();
        String[] tokens = Double.toString(num).split("\\.");
        int decimalPart = Integer.parseInt(tokens[0]);
        int exponent = 0;
        int bias = (int)Math.pow(2,exponentSize-1) - 1;
        fractionPartStr.append(tokens[1]);
        fractionPartStr.insert(0,"0.");
        double fractionPart = Double.parseDouble(fractionPartStr.toString());
        if (num > 0) {
            result.append("0");
            while(decimalPart > 0){
                decimalPartStr.append(decimalPart % 2);
                decimalPart >>= 1;
            }
            result.append(decimalPartStr.reverse());
            exponent = decimalPartStr.length()-1;
            result.append(".");

            System.out.println(fractionPart);
            while(fractionPart != 1.0) {
                fractionPart *= 2;
                if (fractionPart > 1.0) {
                    result.append(1);
                    fractionPart -= 1.0;
                } else if (fractionPart < 1.0){
                    result.append(0);
                }
            }
            result.append(1);
        } else {
            result.append("1");
        }
        System.out.println(result);
        result.deleteCharAt(result.toString().indexOf('.'));
        result.insert(2,'.');
        System.out.println(result);
        exponentStr.append(convertS2B(exponent + bias,exponentSize));
        System.out.println("exponent binary is: " + exponentStr);
        String[] tokens2 = result.toString().split("\\.");
        mantissaStr.append(tokens2[1]);
        System.out.println("mantissa binary is: " + mantissaStr);
        return "";
    }

    //TODO
    // bits to hexadecimal
    // split the hex rep NOT DONE YET FIX IT
    static String convertB2Hex(String num) {
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
        return hexResult.toString();
    }

    static String hexMapping(int num) {
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
