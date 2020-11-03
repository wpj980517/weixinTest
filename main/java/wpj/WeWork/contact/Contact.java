package wpj.WeWork.contact;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import wpj.WeWork.Api;
import wpj.WeWork.Wework;

import static io.restassured.RestAssured.given;

public class Contact extends Api {

    //利用java的重写,来清空上一次请求的参数、请求体等
    @Override
    public RequestSpecification getDefaultRequestSpecification() {
        RequestSpecification requestSpecification= super.getDefaultRequestSpecification();
        return requestSpecification.queryParam("access_token",Wework.getToken())
                .contentType(ContentType.JSON);
    }
}
