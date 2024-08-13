public class Module {
    // Declaring the instance variables
    private String moduleID;
    private String moduleName;
    private double moduleMarks;

    /* This array is to hold details regarding the modules the students are going to take.
     * Relying on the user to enter the module name & code might be a bad practice because the students from the same intake will share the same modules
     * Making the array private to maintain readability and manageability of the code
     */
    private final static String[][] moduleDetails = {
            {"4COSC010C.3", "Software Development 2"},
            {"4COSC008C.3", "Trends in Computer Science"},
            {"4COSC011C.3", "Web Design & development"}
    };

    // There are no setter methods for module name and ID, cause they will stay constant for all the students and for semester

    // Building the constructor with arguments
    public Module(String moduleID, String moduleName, double moduleMarks) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.moduleMarks = moduleMarks;
    }

    // Creating getter and setter methods
    public String getModuleID() {
        return this.moduleID;
    }
    public String getModuleName() {
        return this.moduleName;
    }
    // There are no setter methods for module name and code because we don't want to allow the user to change them

    public double getModuleMarks() {
        return this.moduleMarks;
    }
    public void setModuleMarks(double moduleMarks) {
        this.moduleMarks = moduleMarks;
    }

    /***
     * These method is to get the module name from moduleDetails array
     * Making the method static cause module name is common across all the student objects
     * @param index - modules array of a student object will store module details on the same order as moduleDetails
     *              So this index parameter will be useful to retrieve the value of the current index
     * @return the module name
     */
    public static String getModuleName(int index) {
        return moduleDetails[index][1];
    }

    /***
     * These method is to get the module ID from moduleDetails array
     * Making the method static cause this method id is common across all the student objects
     * @param index - modules array of a student object will store module details on the same order as moduleDetails
     *              So this index parameter will be useful to retrieve the value of the current index
     * @return the module name
     */
    public static String getModuleID(int index) {
        return moduleDetails[index][0];
    }

    /**
     * This method is to get compute and get the grade of a module
     * @return a char which is the grade of a module
     */
    public char getModuleGrade() {
        if (this.moduleMarks >= 75)
            return 'A';
        else if (this.moduleMarks >= 65)
            return 'B';
        else if (this.moduleMarks >= 55)
            return 'C';
        else if (this.moduleMarks >= 40)
            return 'S';
        else if (this.moduleMarks == -1)
            return 'I'; // I stands for invalid / Not registered
        else
            return 'F';
    }
}