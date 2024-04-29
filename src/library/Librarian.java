package library;

public class Librarian {

    private String firstName;
    private String lastName;
    private int librarianID;
    private String email;
  
    public Librarian() {
        firstName = "";
        lastName = "";
        librarianID = 0;
        email = "";
    }
  
    // Getters
    public String getFirstName() {
      return firstName;
    }
  
    public String getLastName() {
      return lastName;
    }
  
    public int getLibrarianID() {
      return librarianID;
    }
  
    public String getEmail() {
      return email;
    }
  
    // Setters
    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }
  
    public void setLastName(String lastName) {
      this.lastName = lastName;
    }
  
    public void setLibrarianID(int librarianID) {
      this.librarianID = librarianID;
    }
  
    public void setEmail(String email) {
      this.email = email;
    }
}
  