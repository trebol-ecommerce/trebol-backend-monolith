package cl.blm.newmarketing.pojos;

/**
 * Simple wrapper class for login credentials.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class LoginPojo {
  public String username;
  public String password;

  public boolean credentialsAreNotEmpty() {
    return (username != null && !username.isEmpty() && password != null && !password.isEmpty());
  }
}