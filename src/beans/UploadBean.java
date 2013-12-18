package beans;

import constants.Files;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.*;


/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 30.10.13
 * Time: 14:04
 */
@ManagedBean
public class UploadBean {

    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() {
        if (file == null) {
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("No file uploaded!"));
            return;
        }
        if (!(hasPictureExtension(file.getFileName()))) {
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("File is no picture!"));
        } else if (!file.getFileName().equals("")) {
            System.out.println("File uploaded: " + file.getFileName());
            try {
                File newFile = new File(Files.getFolder() + "\\" + file.getFileName());
                if (!newFile.createNewFile())
                    FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("File already exists and will be overwritten!"));

                FileOutputStream out = new FileOutputStream(newFile);
                InputStream in = file.getInputstream();

                byte[] buffer = new byte[100000];
                for (int len; (len = in.read(buffer)) != -1; )
                    out.write(buffer, 0, len);

                out.close();
                in.close();

                FacesContext.getCurrentInstance().addMessage("Success!", new FacesMessage("File " + file.getFileName() + " was uploaded successfully."));

            } catch (IOException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("File could not be created."));
            }
        } else
            System.out.println("shit went full retard..");

        setFile(null);
    }

    public static boolean hasPictureExtension(String filename) {
        return filename.toLowerCase().endsWith(".png") || filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg");
    }

    public String[] getUploadedPictureNames() {
        File folder = new File(Files.getFolder());
        return folder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return hasPictureExtension(name);
            }
        });
    }
}
