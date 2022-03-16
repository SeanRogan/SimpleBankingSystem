package banking;

import org.sqlite.util.StringUtils;

import java.util.Arrays;

public class LuhnAlgorithmChecker {

    public LuhnAlgorithmChecker() {
    }

    public boolean verifyCardNumber(String cardNumber) {
        int checkDigit = returnCheckDigit(cardNumber.substring(0,15));
        return (cardNumber.endsWith(String.valueOf(checkDigit)));
    }

    public int findCheckSum(String number) {
        int[] cardNumberArray = new int[number.length()];
        for(int i : cardNumberArray) {
            //populate array with card number digits
            char digit = number.charAt(i);
            cardNumberArray[i] = Integer.parseInt("" + digit);
        }
        //iterate backwards through array
        for(int i = cardNumberArray.length-1; i>=0; i=i-2) {
            int digit = cardNumberArray[i];
            //multiply every odd number by 2
            digit = digit * 2;
            //if number is over 9, subtract 9
            // (weirdo "n % 10 + 1" thing is just to handle potential 0)
            if(digit>9) {
                digit = digit % 10 + 1;
            }
            //save processed number to array
            cardNumberArray[i] = digit;
        }
        //return sum off numbers in array
        return Arrays.stream(cardNumberArray).sum();
    }

    public int returnCheckDigit(String cardNumberSeed){
        int[] cardNumberArray = new int[cardNumberSeed.length()];
        for(int i = 0; i < cardNumberArray.length; i++) {
            //populate array with card number digits
            char digit = cardNumberSeed.charAt(i);
            cardNumberArray[i] = Integer.parseInt("" + digit);
        }
        //iterate backwards through array
        for(int i = cardNumberArray.length-1; i>=0; i=i-2) {
            int digit = cardNumberArray[i];
            //multiply every odd number by 2
            digit = digit * 2;
            //if number is over 9, subtract 9
            // (weirdo "n % 10 + 1" thing is just to handle potential 0)
            if(digit>9) {
                digit = digit % 10 + 1;
            }
            //save processed number to array
            cardNumberArray[i] = digit;
        }
        int checkSum = Arrays.stream(cardNumberArray).sum();
        int checkDigit = 10 - (checkSum % 10);
        if(checkDigit<10) {
            return  checkDigit;
        }
        return 0;
    }
}
