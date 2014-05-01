package beans;

import dao.PictureDao;
import dto.Picture;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
@RequestScoped
public class PictureService {
    private UploadedFile file;

    private PictureDao pictureDao;

    public PictureService() {
        pictureDao = new PictureDao();
    }

    public void upload(FileUploadEvent event) {
        file = event.getFile();
        if (file == null) {
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Bild konnte nicht hochgeladen werden!"));
            return;
        }
        if (!file.getFileName().equals("")) {
            pictureDao.upload(file);
        } else
            System.out.println("shit went full retard..");

        file = null;
    }

    public void delete() {
        String filename = fetchParameter("name");
        pictureDao.delete(filename);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public List<String> getPics() {
        List<String> pics = new ArrayList();
        for(Picture picture : pictureDao.getPictureList())
            pics.add(picture.getName());
        return pics;
    }

    public String getNameForID(int id){
        return pictureDao.getNameForID(id);
    }
}
