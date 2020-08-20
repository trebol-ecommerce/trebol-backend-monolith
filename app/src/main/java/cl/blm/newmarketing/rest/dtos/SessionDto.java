package cl.blm.newmarketing.rest.dtos;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class SessionDto {
  private Long sessionId;
  private String sessionHash;
  private UserDto user;

  public SessionDto() {
    super();
  }

  public Long getSessionId() {
    return sessionId;
  }

  public void setSessionId(Long sessionId) {
    this.sessionId = sessionId;
  }

  public String getSessionHash() {
    return sessionHash;
  }

  public void setSessionHash(String sessionHash) {
    this.sessionHash = sessionHash;
  }

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "SessionDto{" + "sessionId=" + sessionId + ", sessionHash=" + sessionHash + ", user=" + user + '}';
  }
}
