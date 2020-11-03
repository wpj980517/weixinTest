package wpj.WeWork.contact;

import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartmentTest {

    private static Department department;
    String random=String.valueOf(System.currentTimeMillis());
    @BeforeAll
    static void SetUp() {
        department=new Department();
    }

    @Order(1)
    @Test
    void list(){
        //不传入参数代表全部都查询，测试是否包含java组
        department.list("").then().log().all().statusCode(200).body("department.name[0]",equalTo("小杰企业"));
    }
    @Order(2)
    @Test
    void create(){
        department.create("wpjDepartment","1").then().log().all().statusCode(200).body("errcode",equalTo(0));
    }

    @Order(3)
    @Test
    void update(){
        department.update("4","wpj_"+random).then().log().all().statusCode(200);
    }

    @Order(4)
    @Test
    void delete(){
        department.delete("4").then().log().all().statusCode(200);
    }




}