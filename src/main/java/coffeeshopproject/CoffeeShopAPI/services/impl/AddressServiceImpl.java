package coffeeshopproject.CoffeeShopAPI.services.impl;

import coffeeshopproject.CoffeeShopAPI.entity.Address;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.address.CreateAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.address.AddressResponse;
import coffeeshopproject.CoffeeShopAPI.model.address.UpdateAddressModel;
import coffeeshopproject.CoffeeShopAPI.repository.AddressRepository;
import coffeeshopproject.CoffeeShopAPI.services.AddressService;
import coffeeshopproject.CoffeeShopAPI.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ValidationUtil validationUtil;

    private AddressResponse convertToResponse(Address requset){
        return AddressResponse
                .builder()
                .name(requset.getName())
                .handphone(requset.getHandphone())
                .country(requset.getCountry())
                .province(requset.getProvince())
                .city(requset.getCity())
                .streetname(requset.getStreetname())
                .postalcode(requset.getPostalcode())
                .otherdetails(requset.getOtherdetails())
                .labelas(requset.getLabelas())
                .created(requset.getCreated())
                .updated(requset.getUpdated())
                .build();
    }

    @Override
    public AddressResponse createShpAddress(User user, CreateAddressModel request) {
        validationUtil.validate(request);

        var address = Address.builder()
                .name(request.getName())
                .handphone(request.getHandphone())
                .country(request.getCountry())
                .province(request.getProvince())
                .city(request.getCity())
                .streetname(request.getStreetname())
                .postalcode(request.getPostalcode())
                .otherdetails(request.getOtherdetails())
                .labelas(request.getLabelas())
                .created(new Date())
                .updated(null)
                .user(user)
                .build();
        addressRepository.save(address);

        return convertToResponse(address);
    }

    @Override
    public AddressResponse getShpAddress(User user, int id) {
        var address = addressRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Address not found"));
        return convertToResponse(address);
    }

    @Override
    public AddressResponse updateShpAddress(User user, UpdateAddressModel request) {
        validationUtil.validate(request);
        Address req = addressRepository.findFirstByUserAndId(user,request.getId_address())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Address not found"));
        req.setName(request.getName());
        req.setHandphone(request.getHandphone());
        req.setCountry(request.getCountry());
        req.setProvince(request.getProvince());
        req.setCity(request.getCity());
        req.setStreetname(request.getStreetname());
        req.setPostalcode(request.getPostalcode());
        req.setOtherdetails(request.getOtherdetails());
        req.setLabelas(request.getLabelas());
        req.setUpdated(new Date());
        addressRepository.save(req);
        return convertToResponse(req);
    }

    @Override
    public void deleteaddress(User user, int id) {
        var address = addressRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Address not found"));
        addressRepository.delete(address);
    }
}
