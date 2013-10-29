package constants;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 24.10.13
 * Time: 15:08
 * Files.
 */
public enum Files {
    ADMIN, INFO;

    public String getPath() {
        String folder = "C:\\pock\\";
        switch (this) {
            case ADMIN:
                return folder + "admins.txt";
            case INFO:
                return folder + "contactinfo.properties";
            default:
                return super.toString();
        }
    }
}

