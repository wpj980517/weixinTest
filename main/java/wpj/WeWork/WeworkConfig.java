package wpj.WeWork;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.HashMap;

public class WeworkConfig {
    public  String AgentId;
    public String secret;
    public String cropId;
    public  String contactSecret;
    public String current;
    public HashMap<String,HashMap<String,String>> env;


    private volatile   static  WeworkConfig weworkConfig=null;

    private WeworkConfig(){}
    public static  WeworkConfig getInstance(){

        if(weworkConfig==null){
            synchronized (WeworkConfig.class){
                if(weworkConfig==null)
                    return load("/config/WeworkConfig.yaml");

            }

        }
        return weworkConfig;
    }

    //这里其实做到了将yaml和类中的文段相通起来
    public static WeworkConfig load(String path){
        ObjectMapper mapper=new ObjectMapper(new YAMLFactory());//将文件public数据以YAML形式序列化保存

        try {
            //这里读了以后虽然public的数据还是可看，但能够通过path传进来的yaml来进行修改
            return mapper.readValue(WeworkConfig.class.getResourceAsStream(path),WeworkConfig.class);
//            String json=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(WeworkConfig.getInstance());//读取出数据，这里只从类中读取
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }

}
