public class Student {
    // Declaring the instance variables / fields
    private String studentID;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private Module[] modules; // A student is automatically enrolled to 3 modules when a student object is created

    // Building the constructor with arguments
    public Student(String fullName, String studentID, String dateOfBirth, String gender, String email, String phoneNumber) {
        this.fullName = fullName;
        this.studentID = studentID;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.modules = new Module[3]; // Cause there are 3 modules per student
        initialiseModules(); // Enrolling a student object to 3 modules when student object is created
    }

    // This method is to initialise the modules array with with proper module name and module code
    private void initialiseModules() {
        // Registering the student for 3 modules
        for (int i=0; i<modules.length; i++){
            if (this.modules[i] == null) { // Checking if the object is null on the array
                String moduleCode = Module.getModuleID(i);
                String moduleName = Module.getModuleName(i);
                double marks = -1; // -1 marks means that the marks of the student is not yet registered

                this.modules[i] = new Module(moduleCode, moduleName, marks);
            }
        }
    }

    // Creating getter & setter methods to have an easily understandable code on Arrays class
    public String getStudentID() {
        return this.studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFullName() {
        return this.fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getModuleMarks(int index) {
        return this.modules[index].getModuleMarks();
    }
    public void setModuleMarks(int index, double marks) {
        this.modules[index].setModuleMarks(marks);
    }

    // Module grade is calculated and depends on the module marks so there is no separate setter method for this
    public char getModuleGrade(int index) {
        return this.modules[index].getModuleGrade();
    }

    /**
     * This method is to get the total marks obtained by the student
     * @return the total marks
     */
    public double getTotal() {
        double marksSum = 0;
        int registeredModules = 0;
        for (Module module: this.modules) {
            if (module.getModuleMarks() != -1) {
                marksSum += module.getModuleMarks();
                registeredModules += 1;
            }
        }
        if (registeredModules == 0)
            return -1; // returning -1 if the student didn't sit for any of the exams
        else
            return marksSum;
    }

    /**
     * This method is to calculate the average of a student for the modules in which marks have been registered
     * @return double value which is average, If the student didn't sit for any of the exams -1 is returned
     */
    public double getAverage() {
        int registeredModules = 0;
        // Calculating the number of modules this student has completed
        for (Module module: this.modules) {
            if (module.getModuleMarks() != -1) {
                registeredModules += 1;
            }
        }

        if(registeredModules == 0) {
            return -1; // returning -1 if the student didn't sit for any of the exams
        }
        else {
            double total = getTotal();
            return total / (double) registeredModules;
        }
    }

    /**
     * This method is to get the grade of the student which is calculated through the average
     * @return the grade of the student
     */
    public String getGrade() {
        double average = getAverage(); // Getting the average marks of the current instance of the class

        // returning the relevant grade of the student
        if (average >= 80)
            return "Distinction";
        else if (average >= 70)
            return "Merit";
        else if (average >= 40)
            return "Pass";
        else
            return "Fail";
    }

    /**
     * This method is find out the number of modules a student has completed
     * This method follows the same module index order explained in the previous methods
     * @return a boolean array with size 3
     * if the student hasn't attempted the 0th index module (SD2) the boolean array's 0 index in hold false and vice versa
     * 0 - "Software Development 2"
     * 1 - "Trends in Computer Science"
     * 2 - "Web Design & development"
     */
    public boolean[] getModulesCompleted() {
        boolean[] modulesCompleted = new boolean[3];
        for (int i=0; i<this.modules.length; i++) {
            if (this.modules[i].getModuleMarks() == -1) {
                modulesCompleted[i] = false;
            }
            else {
                modulesCompleted[i] = true;
            }
        }
        return modulesCompleted;
    }

    /**
     * This method is to print all the modules a student is enrolled to along with his marks.
     * The logic is simple - If the value of index 0 (which means "Software Development 2" module) is -1,
     * The marks of this student for the current module is not registered.
     * 0 - "Software Development 2"
     * 1 - "Trends in Computer Science"
     * 2 - "Web Design & development"
     */
    public void printModules() {
        System.out.println("\nPrinting marks of " + this.fullName);
        for (int i=0; i< modules.length; i++) {
            if (modules[i].getModuleMarks() == -1) {
                System.out.println(this.fullName + "'s marks for " + modules[i].getModuleName() + "(" + modules[i].getModuleID() + ")" + " is not registered!");
            }
            else {
                System.out.println(modules[i].getModuleName() + "(" + modules[i].getModuleID() + ")" + " - " + modules[i].getModuleMarks());
            }
        }
    }

    /**
     * toString method to get a customised string about the student object which is used to save student details to the external file
     * @return a string representation of the student object
     */
    public String toString() {
        return this.studentID + ", " + this.fullName + ", " + this.dateOfBirth + ", " + this.gender + ", " + this.email + ", " + this.phoneNumber + ", " + this.modules[0].getModuleMarks() + ", " + this.modules[1].getModuleMarks() + ", " + this.modules[2].getModuleMarks();
    }

}