package beans;

import constants.Files;
import dto.Offer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 04.11.13
 * Time: 13:14
 * Represents the backing-bean of the offers.xhtml site.
 */
@ManagedBean
public class OfferBean {
    private final String metapath;
    private List<Offer> offers;

    public OfferBean(String metapath) {
        this.metapath = metapath;
    }

    public OfferBean() {
        metapath = Files.OFFERSMETA.getPath();
    }

    public List<Offer> getOffers() {
        if (offers == null)
            try {
                read();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("File could not be accessed!"));
            }
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void insertOffer(Offer offer, int index) throws IOException {
        if (offers == null) {
            read();
        }
        offers.add(index, offer);
        write();
    }

    public void addOffer(Offer offer) throws IOException {
        if (offers == null) {
            read();
        }
        offers.add(offer);
        write();
    }

    public void init() throws IOException {
        File file = new File(metapath);
        //if(!file.getParentFile().exists())
        //    file.getParentFile().mkdir();
        if (!file.exists())
            file.createNewFile();

        file = file.getParentFile();

        offers = new LinkedList<>();

        String[] picturelist = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png");
            }
        });
        String[] textlist = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });
        for (String picture : picturelist)
            System.out.println(picture);
        for (String text : picturelist)
            System.out.println(text);
        String picturename;
        for (String picture : picturelist) {
            picturename = picture.split("\\.")[0];
            for (String text : textlist) {
                if (text.split("\\.")[0].equals(picturename)) {
                    try {
                        addOffer(new Offer(file.getPath() + "\\" + picture, file.getPath() + "\\" + text));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public void read() throws IOException {
        File inFile = new File(metapath);
        if (inFile.exists()) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(metapath));
                Object readOffers = null;
                try {
                    readOffers = inputStream.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                }
                offers = (LinkedList<Offer>) readOffers;
            } catch (EOFException ex) {
                init();
            }
        } else {
            init();
        }
    }

    public void write() {
        FacesMessage message = new FacesMessage("New information is stored.");
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(metapath));
            stream.writeObject(offers);
            stream.close();
        } catch (FileNotFoundException e) {
            message = new FacesMessage("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            message = new FacesMessage("New information could not be stored.");
            e.printStackTrace();
        } finally {
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void delete() {
        try {
            System.out.println("OfferBean:delete called");
            //System.out.println(fetchParameter("picPath"));
        } catch (IllegalArgumentException illArgEx) {
            illArgEx.printStackTrace();
        }
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public void addNewOffer() {
        System.out.println("OfferBean:delete called");
        //System.out.println(fetchParameter("newOfferText"));
    }
}
