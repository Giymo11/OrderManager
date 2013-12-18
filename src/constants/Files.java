package constants;

import javax.faces.context.FacesContext;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 24.10.13
 * Time: 15:08
 * Files.
 */
public enum Files {
    ADMIN, INFO, OFFERSMETA, OFFERSDIR;

    public static String getFolder() {
        String folder = "C:\\pock";
        try {
            folder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
        } catch (NullPointerException e) {
            //do nothing
        }
        return folder;
    }

    public String getPath() {
        switch (this) {
            case ADMIN:
                return getFolder() + "\\admins.txt";
            case INFO:
                return getFolder() + "\\contactinfo.properties";
            case OFFERSMETA:
                return getFolder() + "\\offers\\offers.dat";
            case OFFERSDIR:
                return getFolder() + "\\offers\\";
            default:
                return super.toString();
        }
    }
}

