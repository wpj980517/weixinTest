package wpj.WeWork;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    @Test
    void templateYaml() {
        Api api=new Api();
        api.getResponseFromYaml("/api/list.yaml",null).then().statusCode(200);
    }
}