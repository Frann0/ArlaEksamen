package BE;


public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Enum userRole;
    private int password;
    private Screen assignedScreen;

    public User(int id, String firstName, String lastName, String userName, String email, int userRole, int password, Screen assignedScreen) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        setUserRole(userRole);
        this.password = password;
        this.assignedScreen = assignedScreen;
    }

    public User(int id, String firstName, String lastName, String userName, String email, int userRole, int password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        setUserRole(userRole);
        this.password = password;
        this.assignedScreen = null;

    }

    public User(String firstName, String lastName, String userName, String email, int userRole, int password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        setUserRole(userRole);
        this.password = password;
        this.assignedScreen = null;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Enum getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        if(userRole == 0){
            this.userRole = UserType.Admin;
        } else{
            this.userRole = UserType.User;
        }

    }

    public Screen getAssignedScreen() {
        return assignedScreen;
    }

    public void setAssignedScreen(Screen assignedScreen) {
        this.assignedScreen = assignedScreen;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +
                ", password=" + password +
                '}';
    }
}
