package %packageName%.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import %packageName%.dao.%entityName%Dao;
import %packageName%.entity.%entityName%;
import %packageName%.service.base.BaseService;

@Transactional(readOnly = true)
@Service
public class %className% extends BaseService<%entityName%, %pkDataType%, %entityName%Dao> {

}
