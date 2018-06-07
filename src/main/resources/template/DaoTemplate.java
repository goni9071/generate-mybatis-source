package %packageName%.dao;

import org.apache.ibatis.annotations.Mapper;

import %packageName%.dao.base.BaseDao;
import %packageName%.entity.%entityName%;

@Mapper
public interface %className% extends BaseDao<%entityName%, %pkDataType%> {
}
