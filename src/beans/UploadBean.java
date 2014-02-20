package beans;

import dbaccess.ConnectionManager;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 30.10.13
 * Time: 14:04
 */
@ManagedBean
@SessionScoped
public class UploadBean {

    private UploadedFile file;
    private File fileToDelete;
    private String folder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");

    private ConnectionManager connectionManager;
    private Connection connection;

    private List<String> pics;

    public UploadBean() {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
        pics = new ArrayList<>();
    }

    public void upload(FileUploadEvent event) {
        file = event.getFile();
        if (file == null) {
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Bild konnte nicht hochgeladen werden!"));
            return;
        }
        if (!file.getFileName().equals("")) {
            try {
                File newFile = new File(folder + "\\" + file.getFileName());
                if (!newFile.createNewFile())
                    FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("File already exists and will be overwritten!"));
                else {
                    ResultSet res = connection.createStatement().executeQuery("SELECT count(*) FROM ordermanager.picture WHERE name = '" + file.getFileName() + "';");
                    res.next();
                    if(res.getInt(1)==0){
                        connection.createStatement().executeUpdate("INSERT INTO ordermanager.picture VALUES(" + getNextID() + ", '" + file.getFileName().toString() + "');");
                        connection.createStatement().executeUpdate("COMMIT;");
                    }
                    res.close();
                }

                FileOutputStream out = new FileOutputStream(newFile);
                InputStream in = file.getInputstream();

                byte[] buffer = new byte[100000];
                for (int len; (len = in.read(buffer)) != -1; )
                    out.write(buffer, 0, len);

                out.close();
                in.close();

                FacesContext.getCurrentInstance().addMessage("Success!", new FacesMessage("Bild " + file.getFileName() + " wurde erfolgreich hochgeladen!"));

            } catch (IOException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("File could not be created."));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            System.out.println("shit went full retard..");

        file = null;
    }

    private int getNextID() {
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT pictureid FROM ordermanager.picture");
            int id = 1;
            while (res.next()) {
                if (id <= res.getInt(1)) {
                    id = res.getInt(1) + 1;
                }
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean hasPictureExtension(String filename) {
        return filename.toLowerCase().endsWith(".png") || filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg");
    }

    public File[] getUploadedPictures() {
        File folder = new File(this.folder);
        return folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return hasPictureExtension(name);
            }
        });
    }

    public void delete() {
        String filename = fetchParameter("name");

        for (File file : getUploadedPictures()) {
            if (file.getName().equals(filename))
                fileToDelete = file;
        }
        try {
            if (fileToDelete != null) {
                //noinspection ResultOfMethodCallIgnored

                ResultSet res = connection.createStatement().executeQuery("SELECT count(*) FROM ordermanager.product WHERE id = " +
                        "(SELECT pictureid FROM ordermanager.picture WHERE name = '" + filename + "');");
                ResultSet res2 = connection.createStatement().executeQuery("SELECT count(*) FROM ordermanager.offer WHERE id = " +
                        "(SELECT pictureid FROM ordermanager.picture WHERE name = '" + filename + "');");
                res.next();
                res2.next();
                if(res.getInt(1)>0 || res2.getInt(1)>0)
                    FacesContext.getCurrentInstance().addMessage("Fehler", new FacesMessage("Bitte löschen oder ändern Sie zuerst die Produkte und Angebote, die dieses Bild verwenden!"));
                else{
                    connection.createStatement().executeUpdate("DELETE FROM ordermanager.picture WHERE name = '" + filename + "';");
                    fileToDelete.delete();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        fileToDelete = null;
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public List<String> getPics() {
        if(pics.isEmpty())
            for (File file : getUploadedPictures())
                pics.add(file.getName().toString());

        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    @PreDestroy
    public void preDestroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
