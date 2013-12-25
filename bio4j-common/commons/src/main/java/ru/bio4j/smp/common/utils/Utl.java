package ru.bio4j.smp.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;

//import java.io.File;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;

public class Utl {

//	public static ArrayList<String> getClassNamesFromPackage(String packageName) throws IOException{
//	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//	    URL packageURL;
//	    ArrayList<String> names = new ArrayList<String>();;
//
//	    packageName = packageName.replace(".", "/");
//	    packageURL = classLoader.getResource(packageName);
//
//	    if(packageURL.getProtocol().equals("jar")){
//	        String jarFileName;
//	        JarFile jf ;
//	        Enumeration<JarEntry> jarEntries;
//	        String entryName;
//
//	        // build jar file name, then loop through zipped entries
//	        jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
//	        jarFileName = jarFileName.substring(5,jarFileName.indexOf("!"));
//	        jf = new JarFile(jarFileName);
//	        try {
//		        jarEntries = jf.entries();
//		        while(jarEntries.hasMoreElements()){
//		            entryName = jarEntries.nextElement().getName();
//		            if(entryName.startsWith(packageName) && entryName.length()>packageName.length()+5){
//		                entryName = entryName.substring(packageName.length(),entryName.lastIndexOf('.'));
//		                names.add(entryName);
//		            }
//		        }
//	        } finally {
//	        	jf.close();
//	        }
//
//	    // loop through files in classpath
//	    } else {
//	    	URI uri = null;
//	    	try {
//	    		uri = new URI(packageURL.toString());
//	    	} catch (URISyntaxException ex) {
//	    		
//	    	}
//	    	File folder = new File(uri.getPath());
//	        // won't work with path which contains blank (%20)
//	        // File folder = new File(packageURL.getFile()); 
//	        File[] contenuti = folder.listFiles();
//	        String entryName;
//	        for(File actual: contenuti){
//	            entryName = actual.getName();
//	            entryName = entryName.substring(0, entryName.lastIndexOf('.'));
//	            names.add(entryName);
//	        }
//	    }
//	    return names;
//	}

//	public static List<Class> listMatchingClasses(String matchPattern) throws IOException {
//	    List<Class> classes = new LinkedList<Class>();
//	    PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
//	    Resource[] resources = scanner.getResources(matchPattern);
//
//	    for (Resource resource : resources) {
//	        Class<?> clazz = getClassFromResource(resource);
//	        classes.add(clazz);
//	    }
//
//	    return classes;
//	}

    /**
     * Вытаскивает имя вайла из полного пути
     * @param filePath
     * @return
     */
    public static String fileName(String filePath) {
        if(StringUtl.isNullOrEmpty(filePath)){
            int p = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
            if (p >= 0)
                return filePath.substring(p+1);
            return filePath;
        }
        return filePath;
    }

    /**
     * Вытаскивает расширение файла из полного пути
     * @param fileName
     * @return
     */
    public static String fileExt(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

    /**
     * Приводит LongToInt, если это возможно
     * @param l
     * @return
     */
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    /**
     * Читает из потока заданное кол-во байтов и возвращает их в виде массива
     * @param in
     * @param length
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream in, int length) throws IOException {
        if (length > (Integer.MAX_VALUE - 5))
            throw new IllegalArgumentException("Parameter \"length\" too big!");
        byte[] buff = new byte[Utl.safeLongToInt(length)];
        int readed = in.read(buff);
        return buff;
    }

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readStream(InputStream in) throws IOException {
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb=new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		String read = br.readLine();
		while(read != null) {
		    sb.append(read);
		    read =br.readLine();
		}
		return sb.toString();		
	}

	/**
	 * @param clazz
	 * @param in
	 * @return
	 * @throws JAXBException
	 */
	public static <T> T unmarshalXml(Class<T> clazz, InputStream in) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Object obj = jaxbUnmarshaller.unmarshal(in);
		if(obj == null) return null;
		if(obj.getClass() == clazz)
			return (T)obj;
		return null;
	}

    /**
     * Checks two classes is assignable
     * @param clazz1
     * @param clazz2
     * @return boolean
     * @throws
     */
	public static boolean typesIsEquals(Class<?> clazz1, Class<?> clazz2) {
		if((clazz1 == null) && (clazz2 == null)) return true;
		if(((clazz1 != null) && (clazz2 == null)) || ((clazz1 == null) && (clazz2 != null))) return false;
		return (clazz1 == clazz2) || clazz1.isAssignableFrom(clazz2) || clazz2.isAssignableFrom(clazz1);
	}

    /**
     * Checks two classes is the same
     * @param clazz1
     * @param clazz2
     * @return boolean
     * @throws
     */
    public static boolean typesIsSame(Class<?> clazz1, Class<?> clazz2) {
        if((clazz1 == null) || (clazz2 == null)) return true;
        if(((clazz1 != null) && (clazz2 == null)) || ((clazz1 == null) && (clazz2 != null))) return false;
        return clazz1.getName().equals(clazz2.getName());
    }

	/**
	 * @param annotationType - Type of annotation to find
	 * @param clazz - Annotated type
	 * @return - Annotation object
	 */
	public static <T> T findAnnotation(Class<T> annotationType, Class<?> clazz) {
		for (Annotation annotation : clazz.getDeclaredAnnotations()) {
			Class<?> atype = annotation.annotationType();
		    if(typesIsEquals(atype, annotationType))
		    	return (T)annotation; 
		}
		return null;
	}

	/**
	 * @param packageName
	 * @return
	 */
	public static String pkg2path(String packageName) {
		return (StringUtl.isNullOrEmpty(packageName) ? null : "/"+packageName.replace('.', '/')+"/");
	}
	
	/**
	 * @param path
	 * @return
	 */
	public static String path2pkg(String path) {
		if(StringUtl.isNullOrEmpty(path)) return null;
		String result = path.replace('/', '.');
		if(result.charAt(0) == '.')
			result = result.substring(1);
		if(result.charAt(result.length()-1) == '.')
			result = result.substring(0, result.length()-1);
		return result;
	}
	
	/**
	 * @param path
	 * @return
	 */
	public static String classNameFromPath(String path) {
		if(StringUtl.isNullOrEmpty(path)) return null;
		return path.endsWith(".class") ? path2pkg(path.replaceAll("\\.class$", "")) : null;
	}
	
	/**
	 * @param context
	 * @param context
	 * @return
	 */
	public static List<Class<?>> findClassesOfBandle(BundleContext context) {
		Logger logger = LoggerFactory.getLogger(Utl.class);
		List<Class<?>> lst = new ArrayList<Class<?>>();
		Enumeration<URL> entrEnum = context.getBundle().findEntries("/", "*.class", true);
		if(entrEnum != null) {
			List<URL> entrs = Collections.list(entrEnum);
			logger.debug("entrs found count: "+entrs.size());
			logger.debug("entrs: ");
			for (URL entry : entrs) {
				logger.debug(" -- entry: "+entry);
				Class<?> clazz;
				try {
					String className = classNameFromPath(entry.getFile()); 
					logger.debug(" -- finding class for: " + className);
					clazz = context.getBundle().loadClass(className);
					logger.debug(" -- class found: "+clazz);
				} catch (ClassNotFoundException ex) {
					clazz = null;
					logger.debug(" -- class not found!");
				}
				if(clazz != null)
					lst.add(clazz);
			}
			return lst;
		} else
			logger.debug("getEntryPaths returns no entry!");
		return lst;
	}

    public static String buildBeanStateInfo(Object bean, String beanName, String tab) {
        if(tab == null) tab = "";
        final String attrFmt = tab + " - %s : %s;\n";
        StringBuilder out = new StringBuilder();
        Class<?> type = bean.getClass();
        String bnName = StringUtl.isNullOrEmpty(beanName) ? type.getName() : beanName;
        out.append(String.format(tab + "%s {\n", bnName));
        for(java.lang.reflect.Field fld : type.getDeclaredFields()) {
            Object val;
            try {
                fld.setAccessible(true);
                val = fld.get(bean);
            } catch (IllegalAccessException ex) {
                val = ex.toString();
            }
            out.append(String.format(attrFmt, fld.getName(), val));
        }
        out.append(tab + "}");
        return out.toString();
    }

    public static <T> boolean arrayContains(T[] array, T item) {
        for (T itm : array)
            if (itm == item)
                return true;
        return false;
    }

    public static <T> T nvl(T a, T b) {
        return (a == null)?b:a;
    }
}

