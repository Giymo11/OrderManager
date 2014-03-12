package dto;

import interfaces.Identifiable;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 26.02.14
 * Time: 09:11
 * To change this template use File | Settings | File Templates.
 */
public class OrderItem implements Identifiable {
    private int id;
    private int orderid;
    private int productid;
    private int ordered;
    private int delivered;

    public OrderItem(int orderid, int productid, int ordered, int delivered){
        setOrderid(orderid);
        setProductid(productid);
        setOrdered(ordered);
        setDelivered(delivered);
    }

    public OrderItem(int productid, int ordered, int delivered){
        setProductid(productid);
        setOrdered(ordered);
        setDelivered(delivered);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }
}
