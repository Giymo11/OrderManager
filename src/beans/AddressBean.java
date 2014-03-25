package beans;

import dao.AddressDAO;
import dto.Address;

import javax.faces.bean.ManagedBean;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 19.03.14
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
public class AddressBean {
    private AddressDAO addressDAO;
    public AddressBean(){
        addressDAO = new AddressDAO();
    }

    public Address getAddressWithID(int id){
        return addressDAO.getAddressWithID(id);
    }
}
