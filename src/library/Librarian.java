package library;

public class Librarian {

    private String firstName;
    private String lastName;
    private int librarianId;
    private String email;
  
    public Librarian() {
        firstName = "";
        lastName = "";
        librarianId = 0;
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
      return librarianId;
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
  
    public void setLibrarianId(int librarianId) {
      this.librarianId = librarianId;
    }
  
    public void setEmail(String email) {
      this.email = email;
    }
}
  