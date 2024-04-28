package library;

public class Member {

    private String firstName;
    private String lastName;
    private int memberId;
    private String email;
  
    public Member() {
        firstName = "";
        lastName = "";
        memberId = 0;
        email = "";
    }
  
    // Getters
    public String getFirstName() {
      return firstName;
    }
  
    public String getLastName() {
      return lastName;
    }
  
    public int getMemberId() {
      return memberId;
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
  
    public void setMemberId(int memberId) {
      this.memberId = memberId;
    }
  
    public void setEmail(String email) {
      this.email = email;
    }
  }
  