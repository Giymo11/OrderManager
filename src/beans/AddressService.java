package beans;

import dao.AddressDao;
import dto.Address;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 19.03.14
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@RequestScoped
public class AddressService {
    private AddressDao addressDao;
    public AddressService(){
        addressDao = new AddressDao();
    }

    public Address getAddressWithID(int id){
        return addressDao.getAddressWithID(id);
    }
}
