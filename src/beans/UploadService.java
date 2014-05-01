package beans;

import dao.UploadDao;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 30.10.13
 * Time: 14:04
 */
@ManagedBean
@RequestScoped
public class UploadService {
    private UploadedFile file;

    private List<String> pics;
    private UploadDao uploadDao;

    public UploadService() {
        uploadDao = new UploadDao();
    }

    public void upload(FileUploadEvent event) {
        file = event.getFile();
        if (file == null) {
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Bild konnte nicht hochgeladen werden!"));
            return;
        }
        if (!file.getFileName().equals("")) {
            uploadDao.upload(file);
            pics.add(file.getFileName());
        } else
            System.out.println("shit went full retard..");

        file = null;
    }

    public String delete() {
        String filename = fetchParameter("name");
        uploadDao.delete(filename);
        for(int i = 0; i<pics.size(); i++)
            if(pics.get(i).equals(filename))
                pics.remove(i);
        return "#";
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public List<String> getPics() {
        if(pics == null)
            pics = uploadDao.getPictures();
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
