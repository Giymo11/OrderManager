package dto;

import interfaces.Identifiable;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 26.02.14
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public class Order implements Identifiable {
    private int id;
    private int tourid;
    private int addressid;
    private String memoForPock;
    private String memoForCustomer;

    public Order(int tourid, int addressid, String memoForCustomer, String memoForPock){
        setTourid(tourid);
        setAddressid(addressid);
        setMemoForCustomer(memoForCustomer);
        setMemoForPock(memoForPock);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTourid() {
        return tourid;
    }

    public void setTourid(int tourid) {
        this.tourid = tourid;
    }

    public int getAddressid() {
        return addressid;
    }

    public void setAddressid(int addressid) {
        this.addressid = addressid;
    }

    public String getMemoForPock() {
        return memoForPock;
    }

    public void setMemoForPock(String memoForPock) {
        this.memoForPock = memoForPock;
    }

    public String getMemoForCustomer() {
        return memoForCustomer;
    }

    public void setMemoForCustomer(String memoForCustomer) {
        this.memoForCustomer = memoForCustomer;
    }
}
