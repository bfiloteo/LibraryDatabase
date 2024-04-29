package library;

public class Member {

    private String firstName;
    private String lastName;
    private int memberID;
    private String email;
  
    public Member() {
        firstName = "";
        lastName = "";
        memberID = 0;
        email = "";
    }
  
    // Getters
    public String getFirstName() {
      return firstName;
    }
  
    public String getLastName() {
      return lastName;
    }
  
    public int getMemberID() {
      return memberID;
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
  
    public void setMemberID(int memberID) {
      this.memberID = memberID;
    }
  
    public void setEmail(String email) {
      this.email = email;
    }
  }
  