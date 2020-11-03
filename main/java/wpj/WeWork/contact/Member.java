package wpj.WeWork.contact;


import io.restassured.response.Response;

import java.util.HashMap;

public class Member extends Contact{


    public Response create(HashMap<String,Object> map){
        String json=template("/data/member.json",map);
        return getDefaultRequestSpecification().body(json).when().post("https://qyapi.weixin.qq.com/cgi-bin/user/create").then().statusCode(200).extract().response();
    }

    public Response delete(String userid){

        return getDefaultRequestSpecification().queryParam("userid",userid)
        .when().get("https://qyapi.weixin.qq.com/cgi-bin/user/delete")
        .then().extract().response();
    }

    public Response update(HashMap<String,Object> map){
        String json=template("/Data/updateMember.json",map);
        return
                getDefaultRequestSpecification().body(json)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/update")
                .then().extract().response();
    }

    public Response getMember(String userid){
        return
                getDefaultRequestSpecification().queryParam("userid",userid)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .then().extract().response();
    }
}
