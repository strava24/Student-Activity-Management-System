import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

// This class is defined to get user inputs safely without crashing the program
// All the scenarios where the program has to interact with the user to get an input is covered here to improve the readability of the code
public class InputValidations {
    static Scanner input = new Scanner(System.in);
    static final String intakeYear = "2024";

    /**
     * This method is to get the full name from the user
     * @return a valid full name
     */
    public static String validFullName() {
        boolean notLetter;
        while (true) {
            notLetter = false;
            System.out.print("\nEnter student's first name : ");
            String firstName = input.next();
            firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase(); // Capitalizing to maintain an order
            input.nextLine(); // Clearing the scanner object if the user enters multiple lines
            System.out.print("\nEnter student's last name : ");
            String lastName = input.next();
            input.nextLine(); // Clearing the scanner object if the user enters multiple lines
            lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase(); // Capitalizing to maintain an order

            String fullName = firstName + " " + lastName;

            for (int i=0; i<fullName.length(); i++) {
                if (!(Character.isLetter(fullName.charAt(i)) || Character.isWhitespace(fullName.charAt(i)))) {
                    System.out.println("\nInvalid name");
                    System.out.println("Name should have only alphabet letters");
                    notLetter = true;
                    break;
                }
            }
            if (!notLetter) {
                return fullName; // Returning the full name
            }
        }
    }

    /**
     * This method is to get valid date of birth from the user
     *  java.time.LocalDate class is used to achieve this
     * @return a string representation of LocalDate object will be returned
     */
    public static String validDateOfBirth() {
        while (true) {
            try {
                System.out.print("\nEnter the year the student was born  : ");
                String year = String.format("%04d", input.nextInt());
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines

                System.out.print("Enter the month the student was born : ");
                String month = String.format("%02d", input.nextInt());
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines

                System.out.print("Enter the day the student was born   : ");
                String day = String.format("%02d", input.nextInt());
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines

                LocalDate dateOfBirth = LocalDate.parse(year + "-" + month + "-" + day);

                if (dateOfBirth.isBefore(LocalDate.of(1950, 1, 1))) { // The user cannot enter a date of birth earlier than 1950 january 1st
                    System.out.println("Date of birth shouldn't be before 1950!");
                    continue;
                }
                return dateOfBirth.toString();
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid number!");
                input.nextLine();
            }
            catch (DateTimeParseException e) {
                System.out.println("Invalid date!"); // Scanner will have been cleaned before getting to this point if this exception raises
            }
        }
    }

    /**
     * This method is to get gender from the user
     * @return a valid string gender from the user
     */
    public static String validGender() {
        String choice;

        while (true) {
            System.out.println("\n1. Male");
            System.out.println("2. Female");
            System.out.println("3. Other");
            System.out.print("Enter your gender : ");
            choice = input.next();
            input.nextLine(); // Clearing the scanner object if the user enters multiple lines


            if (choice.equals("1")) {
                return "Male";
            }
            else if (choice.equals("2")) {
                return "Female";
            }
            else if (choice.equals("3")) {
                System.out.print("\nEnter your gender : ");
                return input.nextLine();
            }
            else {
                System.out.println("Invalid Choice!");
            }
        }

    }

    /**
     * This method is to check the validity of a user input email address.
     * Below link was used as a reference way to check the validity of user input:
     * @return a valid email address string
     */
    public static String validEmail() {
        String email;
        boolean AtCheck; // This variable is to check if the input email has '@' in it
        boolean periodCheck; // This variable is to check if the input email has at least one period (.)
        boolean hasInvalidFormat;
        char character;
        int AtIndex = 0; // This variable is to hold the index of @

        while (true) {
            try {
                AtCheck = false; // setting the variables to false on the beginning of the iteration
                periodCheck = false;
                hasInvalidFormat = false;

                System.out.println("\nFormat - UserName@Domain.TopLevelDomain");
                System.out.print("Enter student's email address: ");
                email = input.next();
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines

                // This loop is to check if the user input has "@" in it
                for (int i=0; i<email.length(); i++) {
                    character = email.charAt(i); // Getting the current character
                    if (character == '@') {
                        AtCheck = true;
                        AtIndex = i;
                    }

                    // Checking if the character is anything other than a letter, number, ., _, -
                    if (!(Character.isLetter(character))) { // Checking if the character is not a letter
                        if (!(character == '.' || character == '-' || character == '_' || character == '@' || Character.isDigit(character))) {
                            System.out.println("Allowed characters : letters (a-z), numbers, underscores, periods, and dashes");
                            hasInvalidFormat = true;
                            break;
                        }
                    }

                    // Checking if the following character of a ./-/_ is a letter or number
                    if (character == '.' || character == '-' || character == '_') {
                        if (email.charAt(i+1) == '.' || email.charAt(i+1) == '-' || email.charAt(i+1) == '_') {
                            System.out.println("An underscore, period, or dash must be followed by one or more letter or number");
                            hasInvalidFormat = true;
                            break;
                        }
                    }
                }

                // This loop is to check if the domain has at least one period (.)
                for (int i=AtIndex+1; i<email.length(); i++) { // Starting after the '@', because domain comes after the '@'
                    if (email.charAt(i) == '.') {
                        periodCheck = true;
                    }
                }
                if (AtCheck && periodCheck && !hasInvalidFormat)
                    return email.toLowerCase();
                else {
                    System.out.println("\nInvalid email. Basic attributes of an email : ");
                    System.out.println("1. Email should consist @ in it");
                    System.out.println("2. There should be at least one period on the domain part");
                }
            }
            catch (Exception e) {
                System.out.println("\nInvalid email address!"); // Having a catch statement just in case of an error
                input.nextLine();
            }
        }
    }

    /**
     * This method is to get a valid contact number from the user
     * @return a valid contact number which is a string
     */
    public static String validPhoneNumber() {
        boolean notDigit;
        String countryCode = "+94-";
        String phoneNumber;

        while (true) {
            notDigit = false;
            System.out.print("\nEnter student's contact number : +94-");
            phoneNumber = input.next();
            input.nextLine(); // Clearing the scanner object if the user enters multiple lines

            for (int i=0; i<phoneNumber.length(); i++) { // Making sure that phone number consists only digits
                if (!(Character.isDigit(phoneNumber.charAt(i)))) {
                    System.out.println("\nInvalid contact number!");
                    System.out.println("Contact number should have only digits!");
                    notDigit = true;
                    break;
                }
            }

            if (notDigit)
                continue;

            if (phoneNumber.length() != 9) { // Sri Lankan numbers should consist only 9 digits (without the country code)
                System.out.println("\nInvalid contact number!");
                System.out.println("Contact number should be 9 characters long!");
                continue;
            }

            return countryCode + phoneNumber;
        }
    }

    /**
     * The program requires the user to input an index to delete or update records.
     * This method is defined to get a valid index from the user that would prevent the program from crashing
     * @param studentNames - the main String array which holds the student IDs
     * @return a valid index that will make sure the program won't crash
     */
    public static int validIndex(String[] studentNames) {
        int seats = StudentActivityManagementSystem.checkAvailableSeats(); // Getting the number of seats available
        int[] AvailableSeats = new int[seats]; // Creating an int array with available seats size
        int x = 0;
        int choice;

        // Storing all the indices that have a record and not empty
        for (int i=0; i<studentNames.length; i++) {
            if (!(studentNames[i].equals("e"))) {
                AvailableSeats[x] = i; // Storing the indices that are filled
                x += 1;
            }
        }

        while (true) {
            try {
                StudentActivityManagementSystem.viewStudents(); // Printing the student details along with the indices
                System.out.print("Enter an index from above : ");
                choice = input.nextInt();
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines

                // Checking if the input integer is a valid index
                for (int i=0; i<AvailableSeats.length; i++) {
                    if (choice == AvailableSeats[i]) {
                        return choice;
                    }
                }

                System.out.println("\nEnter an integer from the provided list!");
            }
            catch (InputMismatchException ime) {
                System.out.println("\nEnter an integer!");
                input.nextLine();
            }
        }
    }

    /**
     * This method is to get a confirmation from the user to do something. (delete or update)
     * @param message - This is the message that will be printed for the user when asking for the confirmation
     * @return - true if the user confirms if not returns false
     */
    public static boolean validConfirmation(String message) {
        String choice;
        while (true) {
            System.out.print(message);
            choice = input.next().toLowerCase();
            input.nextLine(); // Clearing the scanner object if the user enters multiple lines

            if (choice.equals("t")) {
                return true;
            }
            else if (choice.equals("f")) {
                return false;
            }
            else {
                System.out.println("Enter either T (true) or F (false)");
            }
        }
    }

    /**
     * This method will be useful to check if a user input studentID is valid
     * this method will be called on the search functionality
     * @return the valid studentID student entered
     */
    public static String validStudentID() {
        while (true) {
            try {
                System.out.print("\nEnter student ID to find student details : ");
                String studentID = input.next();
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines

                if (!(studentID.charAt(0) == 'w')) { // Checking if the user input ID is starting with a "w"
                    System.out.println("Invalid ID, The Student ID should start with a 'w'");
                    continue;
                }
                if (!(studentID.substring(1, 5).equals(intakeYear))) {
                    System.out.println("Invalid ID, Intake year should be : " + intakeYear);
                    continue;
                }
                if (studentID.length() != 8) {
                    System.out.println("Invalid ID, The Student ID should be 8 characters long");
                    continue;
                }
                return studentID;
            }
            catch (StringIndexOutOfBoundsException e) {
                System.out.println("\nInvalid ID!");
                System.out.println("ID format : ");
                System.out.println("1. Should start with 'w' to indicate University of Westminster");
                System.out.println("2. Intake year should be there after the 'w' (eg: 2024)");
                System.out.println("3. ID should be 8 characters long");
            }
        }
    }

    /**
     * This method is to get a valid marks for a module from the user
     * @return the valid marks
     */
    public static double validMarks(String moduleName) {
        while (true) {
            try {
                System.out.print("\nEnter your marks for " + moduleName + " : ");
                double marks = input.nextInt();
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines

                if (marks > 100)
                    System.out.println("Marks cannot exceed 100!");
                else if (marks < 0)
                    System.out.println("Marks cannot be less than 0!");
                else
                    return marks;
            }
            catch (InputMismatchException ime) {
                System.out.println("Invalid marks. Try again!");
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines
            }
        }
    }

}