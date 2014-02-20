package beans;

import dao.UploadDAO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
public class UploadBean{

    private UploadedFile file;

    private List<String> pics;
    private UploadDAO uploadDAO;

    public UploadBean() {
        uploadDAO = new UploadDAO();
        pics = new ArrayList<>();
    }

    public void upload(FileUploadEvent event) {
        file = event.getFile();
        if (file == null) {
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Bild konnte nicht hochgeladen werden!"));
            return;
        }
        if (!file.getFileName().equals("")) {
            uploadDAO.upload(file);
        } else
            System.out.println("shit went full retard..");

        file = null;
    }

    public void delete() {
        String filename = fetchParameter("name");
        uploadDAO.delete(filename);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public List<String> getPics() {
        pics = uploadDAO.getPictures();
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
