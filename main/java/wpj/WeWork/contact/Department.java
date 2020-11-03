package wpj.WeWork.contact;

import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import javafx.beans.binding.ObjectExpression;
import wpj.WeWork.Wework;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Department extends Contact {
    public Response list(String id) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return getResponseFromYaml("/api/list.yaml", map);


//        return requestSpecification
//        .param("id",id)
//        .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
//        .then().statusCode(200).extract().response();
    }

    //创建部门
    public Response create(String name, String pid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("_file", "/Data/create.json");
        map.put("$.name", name);
        map.put("$.parentid", pid);

        return getResponseFromYaml("/api/create.yaml", map);
    }

    //删除部门
    public Response delete(String id) {
        HashMap<String ,Object> map=new HashMap<String,Object>();
        map.put("id",id);
        return getResponseFromYaml("/api/delete.yaml",map);

//        return
//                getDefaultRequestSpecification()
//                        .param("id", id)
//                        .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
//                        .then().statusCode(200).extract().response();

    }

    //更新部门
    public Response update(String id, String name) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("$.id",id);
        map.put("$.name",name);
        map.put("_file","/Data/update.json");

        return getResponseFromYaml("/api/update.yaml",map);



//        String json = JsonPath.parse(this.getClass().getResourceAsStream("/Data/update.json"))
//                .set("$.id", id).set("$.name", name)
//                .jsonString();
//        return getDefaultRequestSpecification().body(json)
//                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
//                .then().statusCode(200).extract().response();

    }

    //删除全部部门
    public Response deleteAll() {
        ArrayList<Integer> idlist = list("").then().log().all().statusCode(200).extract().path("department.id");
        //然后遍历删除
        for (Integer i : idlist) {
            delete(String.valueOf(i));
        }
        return null;
    }
}
