package org.bb;

/**
 * Determines OS architecture.
 * @author      Morten Bondo	<Bondo.Morten@gmail.com>
 * @version     1.0
 * @since       2014-12-10
 */
public class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
    public static String getOS(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }

}
