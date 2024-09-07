package pl.justpvp.bungee.util;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util
{
    private static SimpleDateFormat dateFormat;
    private static SimpleDateFormat timeFormat;
    private static LinkedHashMap<Integer, String> values;



    public static String replaceString(String s){
        return s.replace(">>", "»").replace("<<", "«").replace("*", "•").replace("%V%", "√").replace("%X%", "✗").replace("§", "&").replace("/n", "\n");
    }

    public static boolean containsIgnoreCase(String[] array, String element) {
        for (String s : array) {
            if (s.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }
    
    public static void copy(InputStream in, File file)
    {
      try
      {
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte['?'];
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        out.close();
        in.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    
    public static String getContent(String urlName)
    {
      String body = null;
      try
      {
        URL url = new URL(urlName);
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        body = toString(in, encoding);
        in.close();
      } catch (Exception ignored) {}
        return body;
    }
    
    public static String toString(InputStream in, String encoding)
      throws Exception
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buf = new byte['?'];
      int len;
      while (-1 != (len = in.read(buf))) {
        baos.write(buf, 0, len);
      }
      in.close();
      return new String(baos.toByteArray(), encoding);
    }

    
    public static String secondsToString(int seconds) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> e : Util.values.entrySet()) {
            int iDiv = seconds / e.getKey();
            if (iDiv >= 1) {
                int x = (int)Math.floor(iDiv);
                sb.append(x).append(e.getValue()).append(" ");
                seconds -= x * e.getKey();
            }
        }
        return sb.toString();
    }
    
    public static boolean isAlphaNumeric(String s) {
        return s.matches("^[a-zA-Z0-9_]*$");
    }
    
    public static boolean isFloat(String string) {
        return Pattern.matches("([0-9]*)\\.([0-9]*)", string);
    }
    
    public static boolean isInteger(String string) {
        return Pattern.matches("-?[0-9]+", string.subSequence(0, string.length()));
    }
    
    public static String getDate(long time) {
        return Util.dateFormat.format(new Date(time));
    }
    
    public static String getTime(long time) {
        return Util.timeFormat.format(new Date(time));
    }
    
    public static long parseDateDiff(String time, boolean future) {
        try {
            Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
            Matcher m = timePattern.matcher(time);
            int years = 0;
            int months = 0;
            int weeks = 0;
            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            boolean found = false;
            while (m.find()) {
                if (m.group() != null && !m.group().isEmpty()) {
                    for (int i = 0; i < m.groupCount(); ++i) {
                        if (m.group(i) != null && !m.group(i).isEmpty()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        continue;
                    }
                    if (m.group(1) != null && !m.group(1).isEmpty()) {
                        years = Integer.parseInt(m.group(1));
                    }
                    if (m.group(2) != null && !m.group(2).isEmpty()) {
                        months = Integer.parseInt(m.group(2));
                    }
                    if (m.group(3) != null && !m.group(3).isEmpty()) {
                        weeks = Integer.parseInt(m.group(3));
                    }
                    if (m.group(4) != null && !m.group(4).isEmpty()) {
                        days = Integer.parseInt(m.group(4));
                    }
                    if (m.group(5) != null && !m.group(5).isEmpty()) {
                        hours = Integer.parseInt(m.group(5));
                    }
                    if (m.group(6) != null && !m.group(6).isEmpty()) {
                        minutes = Integer.parseInt(m.group(6));
                    }
                    if (m.group(7) == null) {
                        break;
                    }
                    if (m.group(7).isEmpty()) {
                        break;
                    }
                    seconds = Integer.parseInt(m.group(7));
                    break;
                }
            }
            if (!found) {
                return -1L;
            }
            Calendar c = new GregorianCalendar();
            if (years > 0) {
                c.add(Calendar.YEAR, years * (future ? 1 : -1));
            }
            if (months > 0) {
                c.add(Calendar.MONTH, months * (future ? 1 : -1));
            }
            if (weeks > 0) {
                c.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
            }
            if (days > 0) {
                c.add(Calendar.DATE, days * (future ? 1 : -1));
            }
            if (hours > 0) {
                c.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
            }
            if (minutes > 0) {
                c.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
            }
            if (seconds > 0) {
                c.add(Calendar.SECOND, seconds * (future ? 1 : -1));
            }
            Calendar max = new GregorianCalendar();
            max.add(Calendar.YEAR, 10);
            if (c.after(max)) {
                return max.getTimeInMillis();
            }
            return c.getTimeInMillis();
        }
        catch (Exception e) {
            return -1L;
        }
    }

    static {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        (values = new LinkedHashMap<>(6)).put(31104000, "y");
        Util.values.put(2592000, "msc");
        Util.values.put(86400, "d");
        Util.values.put(3600, "h");
        Util.values.put(60, "min");
        Util.values.put(1, "s");
    }
}
