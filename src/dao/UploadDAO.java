package dao;

import dto.Picture;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 20.02.14
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class UploadDAO extends JdbcDao {
    private String folder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
    private File fileToDelete;

    public UploadDAO(){
        super();
    }

    public void upload(UploadedFile file){
        try {
            File newFile = new File(folder + "\\" + file.getFileName());
            if (!newFile.createNewFile())
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Achtung! Diese Datei existiert bereits und wird überschrieben"));
            else {
                Connection con = getConnection();
                Statement s = con.createStatement();
                ResultSet res = s.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".picture"
                        + " WHERE name = '" + file.getFileName() + "';");
                res.next();
                if(res.getInt(1)==0){
                    Picture pic = new Picture(file.getFileName().toString());
                    insertObject("picture", pic);

                    s.executeUpdate("UPDATE " + DATABASE_NAME + ".picture SET name = '" + pic.getName() + "' WHERE pictureid = " + pic.getId() + ";");
                    s.executeUpdate("COMMIT;");
                }
                else{
                    FacesContext.getCurrentInstance().addMessage("Anzahl", new FacesMessage("Achtung, ein Bild mit diesem Namen existiert bereits!"));
                }
                close(res, s, con);
            }

            FileOutputStream out = new FileOutputStream(newFile);
            InputStream in = file.getInputstream();

            byte[] buffer = new byte[100000];
            for (int len; (len = in.read(buffer)) != -1; )
                out.write(buffer, 0, len);

            out.close();
            in.close();

            FacesContext.getCurrentInstance().addMessage("Success!", new FacesMessage("Bild " + file.getFileName()
                    + " wurde erfolgreich hochgeladen!"));

        } catch (IOException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("File could not be created."));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String filename){
        for (File file : getUploadedPictures())
            if (file.getName().equals(filename))
                fileToDelete = file;

        Connection con = null;
        Statement s = null;
        Statement s2 = null;
        ResultSet res = null;
        ResultSet res2 = null;
        ResultSet resId = null;

        try {
            if (fileToDelete != null) {
                con = getConnection();
                s = con.createStatement();
                s2 = con.createStatement();
                System.out.println("before initialisation of res&res2");
                res = s.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".product " +
                        "WHERE id = (SELECT pictureid FROM " + DATABASE_NAME + ".picture WHERE name = '" + filename + "');");
                res.next();

                res2 = s2.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".offer " +
                        "WHERE id = (SELECT pictureid FROM " + DATABASE_NAME + ".picture WHERE name = '" + filename + "');");
                res2.next();

                if(res.getInt(1)>0 || res2.getInt(1)>0)
                    FacesContext.getCurrentInstance().addMessage("Fehler", new FacesMessage("Bitte löschen oder ändern "
                            + "Sie zuerst die Produkte und Angebote, die dieses Bild verwenden!"));
                else{
                    resId = s.executeQuery("SELECT pictureid FROM " + DATABASE_NAME + ".picture WHERE name = '" + filename + "';");
                    resId.next();
                    deleteObject("picture", resId.getInt(1));
                    fileToDelete.delete();
                }
            }
            FacesContext.getCurrentInstance().addMessage("Success!", new FacesMessage("Bild " + filename + " erfolgreich gelöscht"));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(res, s, con);
            close(res2, s2, null);
            close(resId, null, null);
            fileToDelete = null;
        }
    }

    public File[] getUploadedPictures(){
        File folder = new File(this.folder);
        return folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return hasPictureExtension(name);
            }
        });
    }

    public static boolean hasPictureExtension(String filename) {
        return filename.toLowerCase().endsWith(".png") || filename.toLowerCase().endsWith(".jpg") ||
                filename.toLowerCase().endsWith(".jpeg");
    }

    public List<String> getPictures(){
        List<String> pics = new ArrayList();
        for (File file : getUploadedPictures())
            pics.add(file.getName());

        Connection con = null;
        Statement stat = null;
        ResultSet res = null;

        try {
            con = getConnection();
            stat = con.createStatement();
            res = stat.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".picture");
            res.next();
            if(res.getInt(1)!=pics.size())
                FacesContext.getCurrentInstance().addMessage("Picture", new FacesMessage(
                        "Fehler! Die Anzahl der Bilder auf dem Server " +
                                "entspricht nicht der Anzahl der Bilder in der Datenbank!"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, stat, con);
        }

        return pics;
    }
}
