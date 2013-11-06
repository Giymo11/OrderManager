package dto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 04.11.13
 * Time: 13:14
 * The DTO representing an offer.
 */
public class Offer implements Serializable {
    private String picturePath;
    private String textPath;
    private String text;

    public Offer(String picturePath, String textPath) {
        setPicturePath(picturePath);
        setTextPath(textPath);
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getTextPath() {
        return textPath;
    }

    public void setTextPath(String textPath) {
        this.textPath = textPath;
    }

    public String getText() {
        if (getTextPath() != null)
            try {
                text = new Scanner(new File(getTextPath())).nextLine();
                return text;
            } catch (FileNotFoundException e) {
                System.out.println("File not found - " + getTextPath());
            } catch (NoSuchElementException ex) {
                text = "";
            }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        return !(picturePath != null ? !picturePath.toLowerCase().equals(offer.picturePath.toLowerCase()) : offer.picturePath != null) && !(textPath != null ? !textPath.toLowerCase().equals(offer.textPath.toLowerCase()) : offer.textPath != null);

    }

    @Override
    public int hashCode() {
        int result = picturePath != null ? picturePath.hashCode() : 0;
        result = 31 * result + (textPath != null ? textPath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "picturePath='" + picturePath + '\'' +
                ", textPath='" + textPath + '\'' +
                ", text='" + getText() + '\'' +
                '}';
    }
}
