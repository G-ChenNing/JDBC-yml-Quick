package tech.xiangcheng.orangebus.generalcrud;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.leaf.util.sql.QuerySystem;
import tech.xiangcheng.orangebus.leaf.util.sql.QuerySystem.QuerySystemBuilder;
import tech.xiangcheng.orangebus.leaf.util.sql.SQLGroupResult;

@Service
public class GeneralService {
@Autowired
EntityManagerFactory emf;
@Autowired
DataSource dataSource;
private static QuerySystem querySystem;
static {
	try {
		querySystem = QuerySystemBuilder.createQuerySystem();
		System.out.println("general service start successfully");
	} catch (IOException e1) {
		System.err.println("general service start fail");
		e1.printStackTrace();
		querySystem = null;
	}
}
/**
 * @param id
 * @param args
 * @return
 * @throws SQLException
 * @throws JsonProcessingException
 */
public String select(String id, JsonObj args) throws SQLException, JsonProcessingException {
	EntityManager em = emf.createEntityManager();
	return querySystem.getQuery(id).select(em, args);
//	return querySystem.getQuery(id).select(dataSource, args).getJSON();
	//	Map<String, Stream<Stream<Entry<String, Object>>>> selectRes = querySystem.getQuery(id).select(dataSource, args);
//	JsonObj jsonObj = JsonObj.instance();
//	selectRes.forEach((k, v) -> {
//		jsonObj.putProperty(k, v.map(s -> {
//			JsonObj temp = JsonObj.instance();
//			s.forEach(e -> {
//				temp.putProperty(e.getKey(), e.getValue());
//			});
//			return temp.toJson();
//		}).collect(Collectors.toList()));
//	});
//	return jsonObj.toJson();
}
public String selectJson(String id, JsonObj args) throws SQLException {
	return querySystem.getQuery(id).select(dataSource, args).getJSON();
}
public String create (String id, JsonObj args) throws SQLException {
	return querySystem.getQuery(id).create(dataSource, args);
}
public void update(String id, JsonObj args) throws SQLException {
	querySystem.getQuery(id).update(dataSource, args);
}
public void delete(String id, JsonObj args) throws SQLException {
	querySystem.getQuery(id).delete(dataSource, args);
}

/**
 * 相比selectJson，该方法更适合在内部适用
 * @param id
 * @param args
 * @return
 * @throws SQLException
 */
@Deprecated
public SQLGroupResult selectResult(String id, JsonObj args) throws SQLException {
	return querySystem.getQuery(id).select(dataSource, args);
}

public void batchInsert(String id, JsonObj args) throws SQLException {
	querySystem.getQuery(id).batchInsert(dataSource, args);
}
public void batchUpdate(String id, JsonObj args) throws SQLException {
	querySystem.getQuery(id).batchUpdate(dataSource, args);
}
}
