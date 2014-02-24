package dto;

import interfaces.Identifiable;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 20.02.14
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class Picture implements Identifiable{
    private int id;
    private String name;

    public Picture(String name){
        setName(name);
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
