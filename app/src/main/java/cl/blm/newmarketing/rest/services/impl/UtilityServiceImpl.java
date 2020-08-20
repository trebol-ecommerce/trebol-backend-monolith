package cl.blm.newmarketing.rest.services.impl;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import cl.blm.newmarketing.rest.AppGlobals;
import cl.blm.newmarketing.rest.services.UtilityService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class UtilityServiceImpl implements UtilityService {
  private static final Logger LOG = LoggerFactory.getLogger(UtilityServiceImpl.class);
  
  @Autowired AppGlobals globals;
  
  @Nullable
  @Override
  public final Date formatString(String dateString) {
    DateFormat formateador = new SimpleDateFormat(globals.DATE_FORMAT);
    try {
      return formateador.parse(dateString);
    } catch (Exception exc) {
      LOG.warn("Date couldn't be parsed", exc);
      return null;
    }
  }

  /**
   * Formats the Date into a string, applying the AppGlobals.DATE_FORMAT constant.
   *
   * @param date The date instance. Can be null
   * 
   * @return A formatted date string, or null if the specified date is null.
   */
  @Nullable
  @Override
  public final String formatDate(Date date) {
    if (date != null) {
      DateFormat formateador = new SimpleDateFormat(globals.DATE_FORMAT);
      return formateador.format(date);
    } else {
      return null;
    }
  }

  @Nullable
  @Override
  public final String hash(String data) {
    LOG.debug("hash");
    try {
      byte[] payload = data.getBytes(Charset.forName(globals.CRYPTOGRAPHIC_CHARSET));

      MessageDigest crypto = MessageDigest.getInstance(globals.CRYPTOGRAPHIC_ALGORITHM);
      byte[] rawHash = crypto.digest(payload);

      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < rawHash.length; i++) {
        String hex = Integer.toHexString(0xff & rawHash[i]);
        if (hex.length() == 1) {
          sb.append('0');
        }
        sb.append(hex);
      }

      return sb.toString();
    } catch (Exception ex) {
      LOG.error("Couldn't hash input data", ex);
      return null;
    }
  }

  @Override
  public final String encrypt(String data) {
    LOG.debug("encrypt");
    return hash(globals.CRYPTOGRAPHIC_SALT + data);
  }
}
