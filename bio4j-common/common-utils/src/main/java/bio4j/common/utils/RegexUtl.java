package bio4j.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtl {
    public static Boolean Match(String line, String regex, Boolean ignoreCase) {
        Pattern pattern = Pattern.compile(regex, (ignoreCase) ? Pattern.CASE_INSENSITIVE : 0);
        Matcher m = pattern.matcher(line); 
        return m.matches();
    }
	
    public static String Find(String line, String regex, Boolean ignoreCase) {
        Pattern pattern = Pattern.compile(regex, ((ignoreCase) ? Pattern.CASE_INSENSITIVE : 0));
        Matcher m = pattern.matcher(line);
        return m.matches() ? m.group() : null;
    }
    
    public static long Pos(String line, String regex, Boolean ignoreCase) {
        Pattern pattern = Pattern.compile(regex, ((ignoreCase) ? Pattern.CASE_INSENSITIVE : 0));
        Matcher m = pattern.matcher(line);
        return m.matches() ? m.start() : -1L;
    }
    
    public static String Replace(String line, String regex, String replacement, Boolean ignoreCase) {
        Pattern pattern = Pattern.compile(regex, ((ignoreCase) ? Pattern.CASE_INSENSITIVE : 0));
        Matcher m = pattern.matcher(line);
        return m.replaceAll(replacement);
    }
    
}
