package cl.blm.newmarketing.backend.services;

import java.util.Date;

import org.springframework.lang.Nullable;

public interface UtilityService {
  /**
   * Parses the string as an instance of Date. Exception-safe.
   *
   * @param dateString The date string. Should be formatted like the
   *                   AppGlobals.DATE_FORMAT constant.
   * 
   * @return A Date instance, or null if the string couldn't be parsed.
   */
  @Nullable
  public Date formatString(String dateString);

  /**
   * Formats the Date into a string, applying the AppGlobals.DATE_FORMAT constant.
   *
   * @param date The date instance. Can be null
   * 
   * @return A formatted date string, or null if the specified date is null.
   */
  @Nullable
  public String formatDate(Date date);

  /**
   * Standard one-way cryptography method.
   *
   * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
   * 
   * @param data
   *
   * @return
   */
  @Nullable
  public String hash(String data);

  /**
   * Adds salt to, and then hashes the data.
   *
   * @param data
   *
   * @return
   */
  public String encrypt(String data);
}
