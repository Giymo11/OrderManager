package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 18.12.13
 * Time: 17:25
 */
@FacesConverter("fileConverter")
public class FileConverter implements Converter {
    private String folder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return new File(folder + "\\" + s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof File)
            return ((File) o).getName();
        else
            return (String) o;
    }
}