import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class StudentActivityManagementSystem {
    // Declaring class variables and objects that can be accessed by all the methods
    static final int intakeCount = 100; // Making intakeCount so that increasing the intakeCount later if needed is possible
    static final String intakeYear = "2024";
    static String[] studentNames = new String[intakeCount]; // The String array is designed to hold studentID
    static Student[] students = new Student[intakeCount]; // The Student array is used to hold student objects
    static Scanner input = new Scanner(System.in);

    /*
    Trying to save to the file without loading from the file might result in loosing data. This variable is make sure this never happens
    If the user at least loads the data once this variable will become true and prevent loosing data.
    If we try to check the seats before saving instead using this variable the program will lose its ability to permanently delete a record when there is only one record to delete
     */
    static boolean hasLoaded = false;

    public static void main(String[] args) {
        // Initializing both the arrays
        initialiseStringArray();
        initialiseObjectArray();
        mainMenu(); // Calling the main menu
    }

    // Main menu where the user interacts with the program
    private static void mainMenu() {
        boolean quit = false;

        while(!quit) {
            System.out.println("\n*****************************************");
            System.out.println("             --Main Menu-- ");
            System.out.println("*****************************************");
            System.out.println("1. Check Available Seats.");
            System.out.println("2. Register Student.");
            System.out.println("3. Delete Student.");
            System.out.println("4. Find Student.");
            System.out.println("5. Store Student Details into a File.");
            System.out.println("6. Load Student Details from the File.");
            System.out.println("7. View Students based on their Names.");
            System.out.println("8. More Options.");
            System.out.println("9. Exit!");
            System.out.print("Enter your choice : ");
            String choice = input.next();
            input.nextLine(); // Clearing the scanner object if the user enters multiple words

            switch (choice) {
                case "1":
                    int seats = checkAvailableSeats();
                    System.out.println("\nThere are " + seats + " seats available!");
                    break;
                case "2":
                    registerStudent();
                    break;
                case "3":
                    deleteStudent();
                    break;
                case "4":
                    findStudent();
                    break;
                case "5":
                    saveDetails();
                    break;
                case "6":
                    loadDetails();
                    break;
                case "7":
                    viewStudents();
                    break;
                case "8":
                    subMenu();
                    break;
                case "9":
                    System.out.println("\nSaving and Terminating program!");
                    saveDetails(); // Auto saving to prevent data lost
                    quit = true;
                    break;
                default:
                    System.out.println("\nInvalid Choice");
            }
        }
    }

    private static void subMenu() {
        boolean quit = false;
        while (!quit) {
            System.out.println("\nMore Controls : ");
            System.out.println("a. Update Student Details.");
            System.out.println("b. Add Module Marks.");
            System.out.println("c. Generate Intake Summary Report.");
            System.out.println("d. Complete Report with Students Progress and Details.");
            System.out.println("e. Quit Sub Menu.");
            System.out.print("Enter your choice : ");
            String newChoice = input.next().toLowerCase();
            input.nextLine(); // Clearing the scanner object if the user enters multiple words

            switch (newChoice) {
                case "a":
                    updateStudentDetails();
                    break;
                case "b":
                    if (checkAvailableSeats() == intakeCount) {
                        System.out.println("\nThere are no students on the records!");
                    }
                    else {
                        System.out.println("\nWould you like to : ");
                        System.out.println("1. Delete existing marks.");
                        System.out.println("2. Set / Update existing marks.");
                        System.out.print("Enter your choice : ");
                        String option = input.next();

                        if (option.equals("1"))
                            deleteModuleMarks();
                        else if (option.equals("2"))
                            setModuleMarks();
                        else
                            System.out.println("\nInvalid Choice");
                    }
                    break;
                case "c":
                    generateIntakeSummary();
                    break;
                case "d":
                    generateCompleteReport();
                    break;
                case "e":
                    System.out.println("\nQuitting Sub-Menu!");
                    quit = true;
                    break;
                default:
                    System.out.println("\nInvalid Choice!");
            }
        }
    }

    /**
     * To initialize the studentNames array with "e" (empty)
     * Having the string values in null instead of "e" could result in NullPointerException
     */
    private static void initialiseStringArray() {
        for (int i=0; i < studentNames.length; i++) { // Initialising the array with all "e" to indicate empty
            studentNames[i] = "e";
        }
    }

    /**
     * This method is to initialise the array with student objects
     * Having the string values in null instead of "e" could result in NullPointerException
     */
    private static void initialiseObjectArray() {
        for (int i=0; i<students.length; i++) {
            students[i] = new Student("e", "e", "e", "e", "e", "e");
            // Using the constructor and initialising all the student fields with "e" / empty
        }
    }

    /**
     * Method to check the available seats / To count the elements with "e"
     * This method is public so that InputValidations class can also access it when needed
     * @return the number of available seats
     */
    public static int checkAvailableSeats() {
        int seatCount = 0; // This variable is to keep track of available seats
        for (int i = 0; i< studentNames.length; i++) {
            if (studentNames[i].equals("e")) // Checking the available seats
                seatCount += 1;
        }
        return seatCount;
    }

    /**
     * This method is to register a student to the records
     * Students details will be collected and stored on the object, while the studentID will automatically get generated by the program
     */
    private static void registerStudent(){
        // Checking the available seats before registering a student
        int seats = checkAvailableSeats();

        if (seats == 0) {
            System.out.println("\nThere are no available seats to register!");
        }
        else {
            // Loading records from the external file if the seat count is equal to the intakeCount
            if (seats == intakeCount) { // If all seats are empty then there is a possibility where the user failed to load records, registering without loading could result in unexpected behaviours
                System.out.println("\nLoading records before proceeding to ensure data security!");
                loadDetails();
            }
            // Getting valid user inputs
            String fullName = InputValidations.validFullName();
            String studentID = generateStudentID(studentNames);
            String dateOfBirth = InputValidations.validDateOfBirth();
            String gender = InputValidations.validGender();
            String email = InputValidations.validEmail();
            String phoneNumber = InputValidations.validPhoneNumber();

            boolean confirmation = false;
            boolean isDuplicate = false;
            // Checking for data duplications
            for (int i = 0; i< studentNames.length; i++) {
                if (students[i].getFullName().equals(fullName) && students[i].getPhoneNumber().equals(phoneNumber)) { // Checking for data duplications on the file
                    System.out.println("\nThere is a possible data duplication, Review the input details!");
                    System.out.println("The data that's already on the records : ");
                    System.out.println("Student name           : " + students[i].getFullName());
                    System.out.println("Student ID             : " + students[i].getStudentID());
                    System.out.println("Student date of birth  : " + students[i].getDateOfBirth());
                    System.out.println("Student gender         : " + students[i].getGender());
                    System.out.println("Student contact number : " + students[i].getPhoneNumber());
                    System.out.println("Student email          : " + students[i].getEmail());

                    confirmation = InputValidations.validConfirmation("Would you like to continue with the registration? (T / F) : ");
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate || confirmation) { // Recoding details if there is no duplications or if the user confirms even after detecting a duplication
                for (int i = 0; i< studentNames.length; i++) {
                    if (studentNames[i].equals("e")) { // Storing the student details on the very first empty spot on the array
                        studentNames[i] = fullName; // Updating the String Array
                        students[i].setFullName(fullName);
                        students[i].setStudentID(studentID);
                        students[i].setDateOfBirth(dateOfBirth);
                        students[i].setGender(gender);
                        students[i].setEmail(email);
                        students[i].setPhoneNumber(phoneNumber);
                        break;
                    }
                }
                System.out.println("\nStudent details recorded!");
            }
            else {
                System.out.println("\nNo new registrations were done!");
            }
        }
    }

    // Method to delete a student from the records / array
    private static void deleteStudent() {
        int seats = checkAvailableSeats();

        if (seats == intakeCount) {
            System.out.println("\nThere are no records to delete!");
        }
        else {
            int index = targetStudent();

            // When the user tries to search for a student using valid ID and no one is registered under this ID -1 will be returned
            if (index == -1) {
                return; // Returning out of the method if the entered ID is not registered
            }

            boolean confirmation = InputValidations.validConfirmation("\nWould you like to delete this record? (T / F) : ");

            if (confirmation) {
                // Deleting the studentID on the String array
                studentNames[index] = "e"; // Reassigning "e"

                // deleting the chosen student records on the Student array
                students[index].setStudentID("e");
                students[index].setFullName("e");
                students[index].setDateOfBirth("e");
                students[index].setGender("e");
                students[index].setEmail("e");
                students[index].setPhoneNumber("e");

                // Deleting the marks of the student as well
                students[index].setModuleMarks(0, -1);
                students[index].setModuleMarks(1, -1);
                students[index].setModuleMarks(2, -1);

                System.out.println("\nDeleted successfully!");
            }
            else {
                System.out.println("\nNo records were deleted!");
            }
        }
    }

    /**
     * Method to find and print a student's details based on their student ID
     * @return the index on the array in which the student is saved
     * if the array returns -1 then that means there is no student registered under this ID
     */
    private static int findStudent() {
        int seats = checkAvailableSeats();

        if (seats == intakeCount) {
            System.out.println("\nThere are no students on the records!");
        }
        else {
            String studentID = InputValidations.validStudentID(); // Getting a valid ID from the user

            for (int i = 0; i< students.length; i++) {
                if (students[i].getStudentID().equals(studentID)) {
                    System.out.println("Student Name           : " + students[i].getFullName());
                    System.out.println("Student Date of Birth  : " + students[i].getDateOfBirth());
                    System.out.println("Student Gender         : " + students[i].getGender());
                    System.out.println("Student Email          : " + students[i].getEmail());
                    System.out.println("Student Contact Number : " + students[i].getPhoneNumber());

                    // Finding the index on the array of the student
                    String stringIndex = studentID.substring(5, 8);
                    int index = Integer.parseInt(stringIndex);

                    return index;
                }
            }
            System.out.println("\nNo student is registered under this ID!");
        }
        return -1; // If the method returns -1 that means there is no student under this ID
    }

    // Method to permanently save the data on the array to the external file
    private static void saveDetails() {
        if (!hasLoaded) {
            System.out.println("\nTrying to save to the file before loading from the file might result in loosing data!");
            System.out.println("Loading data!");
            loadDetails();
            return; // returning out of the method
        }

        try {
            FileWriter writer = new FileWriter("StudentRecords.txt");
            for (int i=0; i< students.length; i++) {
                writer.write(students[i].toString()); // Using the toString method to write to the file
                writer.write("\n");
            }
            writer.close();
            System.out.println("\nSuccessfully saved records!");
        }
        catch (IOException e) {
            System.out.println("\nThere was an issue when saving the file!");
        }
    }

    // Method to store data back into the array from the external file
    private static void loadDetails() {
        int seats = checkAvailableSeats();

        if (seats != 100) { // Checking if the array is holding values
            System.out.println("\nTrying to load data now might result in loosing data!");
            System.out.println("Saving data to ensure data security!");
            saveDetails();
        }

        try {
            File file = new File("StudentRecords.txt");
            Scanner reader = new Scanner(file); // Creating a scanner object to read from the file

            for (int i = 0; i< studentNames.length; i++) {
                if (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    String[] studentInfo = line.split(", ");

                    // Saving the student ID on the String array
                    studentNames[i] = studentInfo[1];

                    // Saving the details back on the Student array
                    students[i].setStudentID(studentInfo[0]);
                    students[i].setFullName(studentInfo[1]);
                    students[i].setDateOfBirth(studentInfo[2]);
                    students[i].setGender(studentInfo[3]);
                    students[i].setEmail(studentInfo[4]);
                    students[i].setPhoneNumber(studentInfo[5]);

                    // Updating the Student marks
                    students[i].setModuleMarks(0, Double.parseDouble(studentInfo[6]));
                    students[i].setModuleMarks(1, Double.parseDouble(studentInfo[7]));
                    students[i].setModuleMarks(2, Double.parseDouble(studentInfo[8]));

                }
            }
            System.out.println("\nSuccessfully loaded records!");
        }
        catch (FileNotFoundException e) {
            System.out.println("\nThere are no available records!");
        }
        catch (ArrayIndexOutOfBoundsException | NumberFormatException e) { // Catching 2 errors to print the same error message
            System.out.println("\nYou might have edited the file");
        }
        hasLoaded = true; // Setting this variable to true when the user loads data
    }

    public static void viewStudents() {
        int seats = checkAvailableSeats();

        if (seats == intakeCount) {
            System.out.println("\nThere are no students in the records!");
        }
        else {
            String[][] sortedArray = bubbleSortName();
            // Printing the array
            System.out.println("\nPrinting the students based on their names : ");
            for (String[] student: sortedArray) {
                int index = Integer.parseInt(student[1]); // Cause the index 1 holds the index of a student
                System.out.println("Index                  : " + index);
                System.out.println("Student Name           : " + students[index].getFullName());
                System.out.println("Student ID             : " + students[index].getStudentID());
                System.out.println("Student Date of Birth  : " + students[index].getDateOfBirth());
                System.out.println("Student Gender         : " + students[index].getGender());
                System.out.println("Student Email          : " + students[index].getEmail());
                System.out.println("Student Contact Number : " + students[index].getPhoneNumber());
                System.out.println(); // To maintain order
            }
        }
    }

    /**
     * This method is to sort the name in alphabetical order
     * @return a 2D array where each inner array will be - [name, ID], and the inner arrays will be sorted on alphabetical order
     */
    private static String[][] bubbleSortName() {
        // Creating an array with all the registered student names
        String[][] nameArray = new String[intakeCount - checkAvailableSeats()][2]; // Creating an array on the size of filled seats, and an inner array is designed to hold name and ID of a student
        int x = 0;
        for (int i = 0; i< studentNames.length; i++) {
            if (!studentNames[i].equals("e")) { // Getting all the registered students
                nameArray[x][0] = studentNames[i];
                nameArray[x][1] = Integer.toString(i);
                x++;
            }
        }

        boolean exchanged = true;
        int bottom = nameArray.length - 2;

        // Sorting based on name
        while (exchanged) {
            exchanged = false;
            for (int i=0; i<=bottom; i++) {
                if (nameArray[i][0].compareTo(nameArray[i+1][0]) > 0) {
                    String[] temp = nameArray[i];
                    nameArray[i] = nameArray[i + 1];
                    nameArray[i + 1] = temp;
                    exchanged = true;
                }
            }
            bottom--;
        }
        return nameArray;
    }

    /**
     * The method is structured to generate student ID automatically for the Array Solutions / task 01.
     * The last 3 digits of the ID will represent the index of the main array (studentDetails).
     * @return valid student ID to register a student
     */
    public static String generateStudentID(String[] studentNames) {
        // Declaring intakeCount and intakeYear to increase the readability and updatability of the code
        String indexPrefix = "w" + intakeYear; // Doing this step manually so that the intake year can be updated on the program easily
        String indexSuffix = "";
        for (int i=0; i<intakeCount; i++) {
            if (studentNames[i].equals("e")) {
                indexSuffix =  String.format("%03d", i);
                break;
            }
        }
        return (indexPrefix + indexSuffix);
    }

    // This method is to update details regarding a student
    private static void updateStudentDetails() {
        int seats = checkAvailableSeats();

        if (seats == intakeCount) {
            System.out.println("\nThere are no students on the records!");
        }
        else {
            int index = targetStudent();

            // When the user tries to search for a student using valid ID and no one is registered under this ID -1 will be returned
            if (index == -1) {
                return; // Returning out of the method if the entered ID is not registered
            }

            while (true) {
                boolean successfullyUpdated = false;

                System.out.println("\nEnter your choice : ");
                System.out.println("1. Set Student Name.");
                System.out.println("2. Set Student Date Of Birth.");
                System.out.println("3. Set Student Gender.");
                System.out.println("4. Set Student Email.");
                System.out.println("5. Set Student Contact Number.");
                System.out.println("6. Quit Function.");
                System.out.print("Enter your choice : ");
                String newChoice = input.next();
                input.nextLine(); // Clearing the scanner object if the user enters multiple lines

                switch (newChoice) {
                    case "1":
                        String fullName = InputValidations.validFullName();
                        students[index].setFullName(fullName);
                        studentNames[index] = fullName; // Updating the string array too
                        successfullyUpdated = true;
                        break;
                    case "2":
                        String dateOfBirth = InputValidations.validDateOfBirth();
                        students[index].setDateOfBirth(dateOfBirth);
                        successfullyUpdated = true;
                        break;
                    case "3":
                        String gender = InputValidations.validGender();
                        students[index].setGender(gender);
                        successfullyUpdated = true;
                        break;
                    case "4":
                        String email = InputValidations.validEmail();
                        students[index].setEmail(email);
                        successfullyUpdated = true;
                        break;
                    case "5":
                        String phoneNumber = InputValidations.validPhoneNumber();
                        students[index].setPhoneNumber(phoneNumber);
                        successfullyUpdated = true;
                        break;
                    case "6":
                        successfullyUpdated = true;
                        break;
                    default:
                        System.out.println("Invalid Choice!");
                }
                if (successfullyUpdated) {
                    break; // breaking out of the infinite loop
                }
            }
        }
    }

    // This method is to update a student's mark
    private static void setModuleMarks() {
        int index = targetStudent();

        // When the user tries to search for a student using valid ID and no one is registered under this ID -1 will be returned
        if (index == -1)
            return; // Returning out of the method if the entered ID is not registered

        // Printing the students marks if the marks are already on the system
        students[index].printModules();

        boolean successfullyUpdated = false;
        while (!successfullyUpdated) {
            System.out.println("\nEnter the module you want to update : ");
            System.out.println("0. Software Development 2");
            System.out.println("1. Trends in Computer Science");
            System.out.println("2. Web Design & Development");
            System.out.println("3. Quit.");
            System.out.print("Enter the module that you want to update : ");
            String module = input.next();
            input.nextLine(); // Clearing the scanner object if the user enters multiple lines

            double marks;
            switch (module) {
                case "0":
                    marks = InputValidations.validMarks(Module.getModuleName(0));
                    students[index].setModuleMarks(0, marks); // Updating the marks
                    System.out.println("\nSuccessfully updated marks for Software Development 2!");
                    break;

                case "1":
                    marks = InputValidations.validMarks(Module.getModuleName(1));
                    students[index].setModuleMarks(1, marks); // Updating the marks
                    System.out.println("\nSuccessfully updated marks for Trends in Computer Science!");
                    break;
                case "2":
                    marks = InputValidations.validMarks(Module.getModuleName(2));
                    students[index].setModuleMarks(2, marks); // Updating the marks
                    System.out.println("\nSuccessfully updated marks for Web Design & Development!");
                    break;
                case "3":
                    System.out.println("No marks were updated of " + students[index].getFullName());
                    successfullyUpdated = true;
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // This method is to delete a students marks for a module
    private static void deleteModuleMarks() {
        int index = targetStudent();

        if (index == -1)
            return; // Returning out of the program if no student is registered under target index

        // Printing the students marks if the marks are already on the system
        students[index].printModules();

        boolean successfullyDeleted = false;
        while (!successfullyDeleted) {
            System.out.println("\nEnter the module you want to delete : ");
            System.out.println("0. " + Module.getModuleName(0));
            System.out.println("1. " + Module.getModuleName(1));
            System.out.println("2. " + Module.getModuleName(2));
            System.out.println("3. Quit.");
            System.out.print("Enter the module that you want to update : ");
            String module = input.next();
            input.nextLine(); // Clearing the scanner object if the user enters multiple lines

            switch (module) {
                case "0":
                    if (students[index].getModuleMarks(0) == -1) {
                        System.out.println("The records are already empty!");
                    }
                    else {
                        students[index].setModuleMarks(0, -1); // reassigning -1 / deleting the marks
                        System.out.println("\nSuccessfully deleted " + students[index].getFullName() + "'s marks for " + Module.getModuleName(0));
                    }
                    break;
                case "1":
                    if (students[index].getModuleMarks(1) == -1) {
                        System.out.println("The records are already empty!");
                    }
                    else {
                        students[index].setModuleMarks(1, -1); // reassigning -1 / deleting the marks
                        System.out.println("\nSuccessfully deleted " + students[index].getFullName() + "'s marks for " + Module.getModuleName(1));
                    }
                    break;
                case "2":
                    if (students[index].getModuleMarks(2) == -1) {
                        System.out.println("The records are already empty!");
                    }
                    else {
                        students[index].setModuleMarks(2, -1); // reassigning -1 / deleting the marks
                        System.out.println("\nSuccessfully deleted " + students[index].getFullName() + "'s marks for " + Module.getModuleName(2));
                    }
                    break;
                case "3":
                    System.out.println("\nQuiting delete functionality!");
                    successfullyDeleted = true;
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    /**
     * To delete/update a module's marks and to update student details we have to actually target a student to continue with the process
     * This meths will be useful to target a student.
     * The method uses 2 different ways to achieve the task
     * 1. Using the find functionality - The user can target a student by searching for that student through that students ID
     * 2. using the view functionality - The user can target a student by viewing all the students' data on the records and choose a student among them
     * @return the index on the array in which the student is saved
     */
    private static int targetStudent() {
        int index;
        while (true) {
            System.out.println("\nEnter a method to target a student to update details!");
            System.out.println("1. Find Student by ID.");
            System.out.println("2. View all of the student details on the records.");
            System.out.print("Enter your choice : ");
            String choice = input.next();
            input.nextLine(); // Clearing the scanner object if the user enters multiple lines

            if (choice.equals("1")) {
                index = findStudent();
                break;
            }
            else if (choice.equals("2")) {
                index = InputValidations.validIndex(studentNames);
                break;
            }
            else {
                System.out.println("invalid Choice!");
            }
        }
        return index;
    }

    /**
     * This method id to find the number of students who have completed the exam for a module
     * @param index - indicates which module we're trying to get the number of passes
     * @return the number of students who have completed the exams for a module
     */
    private static int studentsCompletedAModule(int index) {
        int studentsCompleted = 0;
        for (int i=0; i<students.length; i++) {
            if (students[i].getModuleMarks(index) != -1)
                studentsCompleted += 1;
        }
        return studentsCompleted;
    }

    /**
     * This method is to find the number students passed for a module
     * @param index - indicates which module we're trying to get the number of passes
     * @return the number of students who passed the specified module (through the index)
     */
    private static int studentsPassedForAModule(int index) {
        int studentsPassed = 0;
        for (int i=0; i<students.length; i++) {
            if (students[i].getModuleMarks(index) >= 40) // Pass marks is 40
                studentsPassed += 1;
        }
        return studentsPassed;
    }

    /**
     * This method is to find the average marks obtained by students for a module
     * @param index - indicates which module we're trying to get the number of passes
     * @return the average marks of a modules which is calculated by taking all the marks obtained by students for a module
     * returns -1 when no one has completed exams on the module
     */
    private static double averageMarksOfAModule(int index) {
        int studentsRegistered = 0;
        double marksSum = 0;
        for (int i=0; i<students.length; i++) {
            if (students[i].getModuleMarks(index) > -1) { // Since the program is structured to hold -1 if the marks is not registered
                studentsRegistered += 1;
                marksSum += students[i].getModuleMarks(index);
            }
        }

        if (studentsRegistered == 0) {
            return -1;
        }
        else {
            double moduleAverage = (marksSum / (double) studentsRegistered);
            return moduleAverage;
        }
    }

    /**
     * This method is to generate a report about the intake
     * Details regarding each module will also be printed
     */
    private static void generateIntakeSummary() {
        int seats = checkAvailableSeats();

        if (seats == intakeCount) {
            System.out.println("\nThere are no students on the records to generate a report!");
            return; // returning out of the function
        }

        int filledSeats = 100 - seats;

        System.out.println("\nHow would you like to generate the report?");
        System.out.println("1. As an external file");
        System.out.println("2. On the Command Line Interface");
        System.out.print("Enter you choice : ");
        String choice = input.next();
        input.nextLine(); // Clearing the scanner object if the user enters multiple lines

        switch (choice) {
            case "1":
                try {
                    FileWriter writer = new FileWriter("IntakeSummary.txt");
                    writer.write("-- Intake Summary Report (" + intakeYear + ") Computer Science / Software Engineering -- \n\n");
                    writer.write("The number of students registered to the System                                      - " + filledSeats + "\n\n");

                    // Details regarding Software Development 2
                    writer.write(" - " + Module.getModuleName(0) + " (" + Module.getModuleID(0) + ")\n");
                    writer.write("The number of students who have completed the exams for " + Module.getModuleName(0) + "       - " + studentsCompletedAModule(0) + "\n");
                    writer.write("The number of students who passed " + Module.getModuleName(0) + "                             - "+ studentsPassedForAModule(0) + "\n");
                    if (averageMarksOfAModule(0) == -1) {
                        writer.write("The average marks for " + Module.getModuleName(0) + "                                         - N/A\n\n");
                    }
                    else {
                        writer.write("The average marks for " + Module.getModuleName(0) + "                                         - " + averageMarksOfAModule(0) + "\n\n");
                    }

                    // Details regarding Trends in Computer Science
                    writer.write(" - " + Module.getModuleName(1) + " (" + Module.getModuleID(1) + ")\n");
                    writer.write("The number of students who have completed the exams for " + Module.getModuleName(1) + "   - " + studentsCompletedAModule(1) + "\n");
                    writer.write("The number of students who passed " + Module.getModuleName(1) + "                         - "+ studentsPassedForAModule(1) + "\n");
                    if (averageMarksOfAModule(1) == -1) {
                        writer.write("The average marks for " + Module.getModuleName(1) + "                                     - N/A\n\n");
                    }
                    else {
                        writer.write("The average marks for " + Module.getModuleName(1) + "                                     - " + averageMarksOfAModule(1) + "\n\n");
                    }
                    // Details regarding Web Design & Development
                    writer.write(" - " + Module.getModuleName(2) + " (" + Module.getModuleID(2) + ")\n");
                    writer.write("The number of students who have completed the exams for " + Module.getModuleName(2) + "     - " + studentsCompletedAModule(2) + "\n");
                    writer.write("The number of students who passed " + Module.getModuleName(2) + "                           - "+ studentsPassedForAModule(2) + "\n");
                    if (averageMarksOfAModule(2) == -1) {
                        writer.write("The average marks for " + Module.getModuleName(2) + "                                       - N/A\n\n");
                    }
                    else {
                        writer.write("The average marks for " + Module.getModuleName(2) + "                                       - " + averageMarksOfAModule(2) + "\n\n");
                    }
                    writer.close();

                    System.out.println("\nSuccessfully generated report!");

                }
                catch (IOException e) {
                    System.out.println("\nThere was a issue in generating the report!");
                }
                break;
            case "2":
                System.out.println("\n-- Intake Summary Report (" + intakeYear + ") Computer Science / Software Engineering -- \n");
                System.out.println("The number of students registered to the System                                      - " + filledSeats + "\n");

                // Software development 2 module
                System.out.println(" - " + Module.getModuleName(0) + " (" + Module.getModuleID(0) + ")\n");
                System.out.println("The number of students who have completed the exams for " + Module.getModuleName(0) + "       - " + studentsCompletedAModule(0));
                System.out.println("The number of students who passed " + Module.getModuleName(0) + "                             - "+ studentsPassedForAModule(0));
                if (averageMarksOfAModule(0) == -1) {
                    System.out.println("The average marks for " + Module.getModuleName(0) + "                                         - N/A\n\n");
                }
                else {
                    System.out.println("The average marks for " + Module.getModuleName(0) + "                                         - " + averageMarksOfAModule(0) + "\n");
                }

                // Trends in computer science module
                System.out.println(" - " + Module.getModuleName(1) + " (" + Module.getModuleID(1) + ")\n");
                System.out.println("The number of students who have completed the exams for " + Module.getModuleName(1) + "   - " + studentsCompletedAModule(1));
                System.out.println("The number of students who passed " + Module.getModuleName(1) + "                         - "+ studentsPassedForAModule(1));
                if (averageMarksOfAModule(1) == -1) {
                    System.out.println("The average marks for " + Module.getModuleName(1) + "                                     - N/A\n\n");
                }
                else {
                    System.out.println("The average marks for " + Module.getModuleName(1) + "                                     - " + averageMarksOfAModule(1) + "\n");
                }

                // Web design & development module
                System.out.println(" - " + Module.getModuleName(2) + " (" + Module.getModuleID(2) + ")\n");
                System.out.println("The number of students who have completed the exams for " + Module.getModuleName(2) + "     - " + studentsCompletedAModule(2));
                System.out.println("The number of students who passed " + Module.getModuleName(2) + "                           - "+ studentsPassedForAModule(2));
                if (averageMarksOfAModule(2) == -1) {
                    System.out.println("The average marks for " + Module.getModuleName(2) + "                                       - N/A\n\n");
                }
                else {
                    System.out.println("The average marks for " + Module.getModuleName(2) + "                                       - " + averageMarksOfAModule(2) + "\n");
                }

                break;
            default:
                System.out.println("\nInvalid choice!");
        }
    }

    /**
     * This method is to generate a complete report with all the students' progress
     * The students will be ordered according to their averaged (in a sorted way)
     */
    private static void generateCompleteReport() {
        int seats = checkAvailableSeats();

        if (seats == intakeCount) {
            System.out.println("\nThere are no records to generate a report!");
            return; // returning out of the function
        }

        String[][] studentsCompleted = bubbleSortTotal(); // Getting the sorted array
        int printingElement = studentsCompleted.length - 1; // This variable will be useful to write in reverse order of the array / Cause the bubble sort sorts in ascending order

        System.out.println("\nHow would you like to generate the report?");
        System.out.println("1. As an external file");
        System.out.println("2. On the Command Line Interface");
        System.out.print("Enter you choice : ");
        String choice = input.next();
        input.nextLine(); // Clearing the scanner object if the user enters multiple lines

        switch (choice) {
            case "1":
                try {
                    FileWriter writer = new FileWriter("CompleteReport.txt");

                    writer.write("-- Complete Report with Student Progress (" + intakeYear + ") Computer Science / Software Engineering -- \n\n");

                    for (int i=0; i<studentsCompleted.length; i++) {
                        // Getting the index of printing element
                        int index = Integer.parseInt(studentsCompleted[printingElement][0].substring(5, 8)); // This will return the index of the printing element

                        writer.write("Student Name                                 - " + students[index].getFullName() + "\n");
                        writer.write("Student ID                                   - " + students[index].getStudentID() + "\n");

                        // Checking if the student has completed the exam for SD2 before writing to the file
                        if (students[index].getModuleMarks(0) == -1)
                            writer.write("Marks & Grade for " + Module.getModuleName(0) + "     - N/A\n");
                        else
                            writer.write("Marks & Grade for " + Module.getModuleName(0) + "     - " + students[index].getModuleMarks(0) + " / " + students[index].getModuleGrade(0) + "\n");

                        // Checking if the student has completed the exam for TCS before writing to the file
                        if (students[index].getModuleMarks(1) == -1)
                            writer.write("Marks & Grade for " + Module.getModuleName(1) + " - N/A\n");
                        else
                            writer.write("Marks & Grade for " + Module.getModuleName(1) + " - " + students[index].getModuleMarks(1) + " / " + students[index].getModuleGrade(1) + "\n");

                        // Checking if the student has completed the exam for WDD before writing to the file
                        if (students[index].getModuleMarks(2) == -1)
                            writer.write("Marks & Grade for " + Module.getModuleName(2) + "   - N/A\n");
                        else
                            writer.write("Marks & Grade for " + Module.getModuleName(2) + "   - " + students[index].getModuleMarks(2) + " / " + students[index].getModuleGrade(2) + "\n");

                        // Checking if the student has at least completed on exam
                        if (students[index].getTotal() == -1) { // Which means that the student didn't didn't even sit for a single exam
                            writer.write("The Total Marks                              - N/A\n");
                            writer.write("The Average Marks                            - N/A\n");
                            writer.write("The Final Grade                              - N/A\n");

                            // Notifying that the student did,t complete even a single module
                            writer.write("Haven't completed exams in any modules yet!\n");

                        }
                        else {
                            writer.write("The Total Marks                              - " + students[index].getTotal() + "\n");
                            writer.write("The Average Marks                            - " + students[index].getAverage() + "\n");
                            writer.write("The Final Grade                              - " + students[index].getGrade() + "\n");

                            // Printing the modules completed by a student
                            writer.write("Completed exams in the following modules :\n");
                            boolean[] modulesCompleted = students[index].getModulesCompleted();
                            if (modulesCompleted[0])
                                writer.write(" - " + Module.getModuleName(0) + " (" + Module.getModuleID(0) + ")\n");
                            if (modulesCompleted[1])
                                writer.write(" - " + Module.getModuleName(1) + " (" + Module.getModuleID(1) + ")\n");
                            if (modulesCompleted[2])
                                writer.write(" - " + Module.getModuleName(2) + " (" + Module.getModuleID(2) + ")\n");

                        }
                        writer.write("\n"); // To maintain order

                        printingElement--; // Reducing because we're printing on the reverse order
                    }
                    // Printing some mandatory information for the person reading the report
                    writer.write("Note that average is calculated based on the number of modules a student has completed");

                    writer.close();

                    System.out.println("\nSuccessfully generated report!");

                }
                catch (IOException e) {
                    System.out.println("\nThere was a issue in generating the report!");
                }
                break;
            case "2":
                for (int i=0; i<studentsCompleted.length; i++) {
                    // Getting the index of printing element
                    int index = Integer.parseInt(studentsCompleted[printingElement][0].substring(5, 8)); // This will return the index of the printing element

                    System.out.println("Student Name                                 - " + students[index].getFullName());
                    System.out.println("Student ID                                   - " + students[index].getStudentID());

                    // Checking if the student has completed the exam for SD2 before writing to the file
                    if (students[index].getModuleMarks(0) == -1)
                        System.out.println("Marks & Grade for " + Module.getModuleName(0) + "     - N/A");
                    else
                        System.out.println("Marks & Grade for " + Module.getModuleName(0) + "     - " + students[index].getModuleMarks(0) + " / " + students[index].getModuleGrade(0));

                    // Checking if the student has completed the exam for TCS before writing to the file
                    if (students[index].getModuleMarks(1) == -1)
                        System.out.println("Marks & Grade for " + Module.getModuleName(1) + " - N/A");
                    else
                        System.out.println("Marks & Grade for " + Module.getModuleName(1) + " - " + students[index].getModuleMarks(1) + " / " + students[index].getModuleGrade(1));

                    // Checking if the student has completed the exam for WDD before writing to the file
                    if (students[index].getModuleMarks(2) == -1)
                        System.out.println("Marks & Grade for " + Module.getModuleName(2) + "   - N/A");
                    else
                        System.out.println("Marks & Grade for " + Module.getModuleName(2) + "   - " + students[index].getModuleMarks(2) + " / " + students[index].getModuleGrade(2));

                    // Checking if the student has at least completed on exam
                    if (students[index].getTotal() == -1) { // Which means that the student didn't didn't even sit for a single exam
                        System.out.println("The Total Marks                              - N/A");
                        System.out.println("The Average Marks                            - N/A");
                        System.out.println("The Final Grade                              - N/A");

                        // Notifying that the student did,t complete even a single module
                        System.out.println("Haven't completed exams in any modules yet!");

                    }
                    else {
                        System.out.println("The Total Marks                              - " + students[index].getTotal());
                        System.out.println("The Average Marks                            - " + students[index].getAverage());
                        System.out.println("The Final Grade                              - " + students[index].getGrade());

                        // Printing the modules completed by a student
                        System.out.println("Completed exams in the following modules :\n");
                        boolean[] modulesCompleted = students[index].getModulesCompleted();
                        if (modulesCompleted[0])
                            System.out.println(" - " + Module.getModuleName(0) + " (" + Module.getModuleID(0) + ")");
                        if (modulesCompleted[1])
                            System.out.println(" - " + Module.getModuleName(1) + " (" + Module.getModuleID(1) + ")");
                        if (modulesCompleted[2])
                            System.out.println(" - " + Module.getModuleName(2) + " (" + Module.getModuleID(2) + ")");

                    }
                    System.out.println(); // To maintain order

                    printingElement--; // Reducing because we're printing on the reverse order
                }
                break;
            default:
                System.out.println("\nInvalid Choice!");
        }
    }

    /**
     * This method is to implement bubble sort on the total marks obtained by the user.
     * @return a 2D array where each inner array will be - [ID, average], and the inner arrays will be sorted according to the average
     */
    private static String[][] bubbleSortTotal() {
        // Creating an array with registered number of students
        String[][] studentsCompleted = new String[intakeCount - checkAvailableSeats()][2]; // Creating the 2D array to hold both ID and average
        // Giving values to the arrays
        int x = 0;
        for (int i=0; i<students.length; i++) {
            if (!(studentNames[i].equals("e"))) {
                studentsCompleted[x][0] = students[i].getStudentID();
                studentsCompleted[x][1] = Double.toString(students[i].getAverage()); // Cause this is a string array
                x++;
            }
        }

        // Implementing bubble sort
        int bottom = studentsCompleted.length - 2; // Reducing 2 to prevent ArrayIndexOutOfBoundsException
        String[] temp;
        boolean exchanged = true;

        while(exchanged) {
            exchanged = false;
            for (int i=0; i<=bottom; i++) {
                if (Double.parseDouble(studentsCompleted[i][1]) > Double.parseDouble(studentsCompleted[i+1][1])) {
                    temp = studentsCompleted[i];
                    studentsCompleted[i] = studentsCompleted[i+1];
                    studentsCompleted[i+1] = temp;
                    exchanged = true;
                }
            }
            bottom--;
        }
        return studentsCompleted;
    }
}