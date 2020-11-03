package wpj.WeWork;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Wework {

    private volatile static String token=null;
    private static String getTokenString(String secret){
        return given().queryParam("corpid", WeworkConfig.getInstance().cropId)
                .queryParam("corpsecret", secret)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then().log().all().statusCode(200).body("errcode",equalTo(0)).extract().path("access_token");
    }

    public static String getToken(){
        if(token==null){
            synchronized(Wework.class){
                if(token==null){
                    return getTokenString(WeworkConfig.getInstance().contactSecret);
                }
            }
        }
        return token;
    }
}
