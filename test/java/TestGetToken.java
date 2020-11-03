import org.junit.jupiter.api.Test;
import wpj.WeWork.Wework;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestGetToken {

    @Test
    void testToken(){
        Wework wework=new Wework();
        String token= Wework.getToken();
        assertThat(token,not(equalTo(null)));
    }

}
