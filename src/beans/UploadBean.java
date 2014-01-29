package beans;

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
    private File fileToDelete;
    private String folder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");

    public File getFileToDelete() {
        return fileToDelete;
    }

    public void setFileToDelete(File fileToDelete) {
        this.fileToDelete = fileToDelete;
    }

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
                File newFile = new File(folder + "\\" + file.getFileName());
                System.out.println(newFile + " is the path to new File.");
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

        file = null;
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
        if (fileToDelete != null)
            //noinspection ResultOfMethodCallIgnored
            fileToDelete.delete();
        fileToDelete = null;
    }
}
