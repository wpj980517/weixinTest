package wpj.WeWork.contact;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import sun.security.ec.point.ProjectivePoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    private  static Member member;
    private String random=String.valueOf(Math.random());
    @BeforeAll
    static void setUp() {
        member=new Member();
    }

    @ParameterizedTest
    @CsvSource({
            "xiaojie, 13129325109",
            "jiege, 18218192833"
    })
    void create(String name, String phoneNum) {
        HashMap<String,Object> map=new HashMap<String, Object>();
                map.put("userid",name+String.valueOf(123));
                map.put("name",name);
                map.put("mobile",phoneNum);
        member.create(map).then().log().all().statusCode(200).body("errcode",equalTo(0));
    }

    @Test
    void delete(){
        member.delete("xiaojie123").then().log().all().body("errcode",equalTo(0));
    }

    @Test
    void updata(){
        HashMap<String,Object> map=new HashMap<String, Object>();
        map.put("userid","jiege123");
        map.put("name","xiaojie");
        member.update(map).then().log().all().body("errcode",equalTo(0));
    }

    @Test
    void getMember(){
        member.getMember("jiege123").then().log().all().body("errcode" ,equalTo(0));
    }
}