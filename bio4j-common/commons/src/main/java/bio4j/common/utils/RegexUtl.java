package bio4j.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtl {
    public static Matcher match(String line, String regex, Boolean ignoreCase) {
        int flags = Pattern.DOTALL | Pattern.MULTILINE;
        if(ignoreCase)
            flags = flags | Pattern.CASE_INSENSITIVE;
        Pattern pattern = Pattern.compile(regex, flags);
        return pattern.matcher(line);
    }
	
    public static String find(String line, String regex, Boolean ignoreCase) {
        Pattern pattern = Pattern.compile(regex, ((ignoreCase) ? Pattern.CASE_INSENSITIVE : 0));
        Matcher m = pattern.matcher(line);
        return m.matches() ? m.group() : null;
    }
    
    public static long pos(String line, String regex, Boolean ignoreCase) {
        Pattern pattern = Pattern.compile(regex, ((ignoreCase) ? Pattern.CASE_INSENSITIVE : 0));
        Matcher m = pattern.matcher(line);
        return m.matches() ? m.start() : -1L;
    }
    
    public static String replace(String line, String regex, String replacement, Boolean ignoreCase) {
        Pattern pattern = Pattern.compile(regex, ((ignoreCase) ? Pattern.CASE_INSENSITIVE : 0));
        Matcher m = pattern.matcher(line);
        return m.replaceAll(replacement);
    }
    
}
