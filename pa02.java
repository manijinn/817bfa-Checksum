


import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;


public class pa02 {

    private static ArrayList<Character> chararr = new ArrayList<Character>();
    private static int result;
    private static String message;
    private static int cSize;

    // The constructor. It does all the work essentially.
    pa02(String file, String size) throws Exception {

        // If inputted size isn't 8, 16, or 32, it is invalid. Notify the user.
        if (!size.equals("8") && !size.equals("16") && !size.equals("32")) {

            System.err.println("Valid checksum sizes are 8, 16, or 32\n");
        }

        // Otherwise, perform checksum on appropriate size.
        else if (size.equals("8")) {

            cSize = 8;
            Scan(file);
            result = check8();
            display();

        }

        else if (size.equals("16")) {

            cSize = 16;
            Scan(file);
            result = check16();
            display();

        }

        else {

            cSize = 32;
            Scan(file);
            result = check32();
            display();

        }
    }   


    // Checksum for 8 bits.
    static int check8() {

        int checksum = 0;

        // Very simply case, 8 bits = 1 byte = 1 character. No need for padding.
        for (int i = 0; i < chararr.size(); i++) {

            checksum += chararr.get(i);

            // We need to throw away any overflow beyond 8-bits. 255 is equivalent to FF, or 1111 1111. If we AND this binary value with the checksum value,
            // we can keep the first 8 bits and throw away the rest. This is similar if we were to do checksum by hand. We would throw away any overflow, which is
            // what & 255 allows us to do.
            checksum = checksum & 255;
        } 

        return checksum;
    }


    // Checksum for 16 bits.
    static int check16() {

        int checksum = 0;

        // We do modulus of the length of the arraylist by 2 since 16 but checksum requires us to add 2 characters at once.
        int remainder = chararr.size() % 2;

        // If the remainder isn't equal to 0, then we need to use padding.
        if (remainder != 0) {

            // Add a 'X' for each iteration, which will increase the size of chararr. Then perform an additional remainder calculation. If remainder = 0,
            // then we have a length that will allow us to add 2 character per iteration.
            while (remainder != 0) {

                chararr.add('X');

                remainder = chararr.size() % 2;

            }
        }

        for (int i = 0; i < chararr.size(); i += 2) {

            // We shift the first number left by 8 bits, this will yield a binary number of 16-bits length. We then add the next number to it, which would only
            // have the first 8 bits filled in (characters only have 8 bits, hence why they are equal to 1 byte). The end result is a filled-in 16 bit binary value, which we can
            // add to the checksum.
            checksum += (chararr.get(i) << 8) + (chararr.get(i + 1));

            // 65535 is equivalent to 11111111 11111111, or (FF * FF) - 1, or (256 * 256) - 1. This is our mask for 16-bits. We are going to use it each iteration to
            // throwaway any overflow.
            checksum = checksum & 65535;

        } 
     
        return checksum;

    }


    // Checksum for 32 bits.
    static int check32() {

        int checksum = 0;

        // We do modulus by 4 since 32 bits checksum requires us to add 4 characters at once.
        int remainder = chararr.size() % 4;

        if (remainder != 0) {

            while (remainder != 0) {

                chararr.add('X');

                remainder = chararr.size() % 4;

            }
        }

        for (int i = 0; i < chararr.size(); i += 4) {

            // Shift left by 24 to make space for 3 more additional numbers which we can combine, giving us a filled-in 32 bit value. 
            checksum += (chararr.get(i) << 24) + (chararr.get(i + 1) << 16) + (chararr.get(i + 2) << 8) + (chararr.get(i + 3));

            // The decimal value for the 32 bit mask was too large to type out, so we convert the mask to its hexadecimal form.
            // I.e 256 * 256 * 256 * 256 = FF * FF * FF * FF = 0xFFFFFFFF. We throwout any overflow each iteration, and maintain a 32 bit size.
            // 0xFFFFFFFF should be equal to = 11111111 11111111 11111111 11111111, which is a mask that lets us keep the first 32 bits.
            checksum = checksum & 0xFFFFFFFF;
        } 

        return checksum;

    }


    // Scan the file
    static void Scan(String File) throws Exception {

        Scanner scan = new Scanner(new File(File));

        message = scan.nextLine();

        for (int i = 0; i < message.length(); i++) {

            chararr.add(message.charAt(i));

        }

        System.out.println();

        // End the "LF" character to the end (which is the \n newline character).
        chararr.add('\n');
        
        scan.close();

    }


    // Final display of the checksum with the size of the checksum used and amount of chars used.
    static void display() {

        int width = 0;

        for (int i = 0; i < chararr.size(); i++) {

            // Print out the message for the output.
            System.out.print(chararr.get(i));
            width++;

            // Start a newline at 80 characters.
            if (width >= 80) {

                System.out.println();

                width = 0;

            }
        }

        System.out.println();

        System.out.printf("%2d bit checksum is %8x for all %4d chars\n", cSize, result, chararr.size());

    }
    
    public static void main(String args[]) throws Exception {

        pa02 Obj = new pa02(args[0], args[1]);

    }
}
