package wpj.WeWork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.*;

public class Api {

    private HashMap<String,Object> map=new HashMap<String, Object>();

    public RequestSpecification getDefaultRequestSpecification(){
        return given().log().all();
    }

    //获取json数据
    public String template(String path, HashMap<String,Object> map){
        DocumentContext documentContext= JsonPath.parse(Api.class.getResourceAsStream(path));
        for(Map.Entry<String,Object> entry: map.entrySet()){
            documentContext.set(entry.getKey(),entry.getValue());
        }
        return documentContext.jsonString();
    }

    //将map的数据放入到restful参数中中
    public Restful updateApiFromMap(Restful api ,HashMap<String,Object> map){
        if(map==null)
            return api;
        //这里需要将map的参数传入到query里面去
        if(api.method.toLowerCase().equals("get")){
            map.entrySet().forEach(entry->{
                api.query.put(entry.getKey(),entry.getValue().toString());
            });
        }

        //这里用_file标识json，然后判断是否是_file，然后使用template将其转换成json
        if(map.containsKey("_file")){
            String filepath= (String) map.remove("_file");
            api.body=template(filepath,map);
        }
        return api;
    }

    //通过restful获取出response
    public Response getResponseFromRestful(Restful api){
            RequestSpecification requestSpecification =getDefaultRequestSpecification();
            if(!api.query.isEmpty()){
                api.query.entrySet().forEach(entry ->{
                    requestSpecification.queryParam(entry.getKey(),entry.getValue());
                });
            }
            if(api.body!=null){
                requestSpecification.body(api.body);
            }
        return requestSpecification.when().request(api.method.toLowerCase(),api.url).then().extract().response();
    }


    //根据har读取数据并存放到restful中
    public Restful getApiFromHar(String path,String pattern){
        //从har文件读取接口定义并发送
        HarReader harReader=new HarReader();

        //读取文件
        try {
            Har har=harReader.readFromFile(new File(URLDecoder.decode(getClass().getResource(path).getPath(),"utf-8")));
            HarRequest request=new HarRequest();//这个是请求头

            //提取出最底层的log的map然后进行遍历
            for (HarEntry entry : har.getLog().getEntries()) {
                request = entry.getRequest();//提取出request标签
                if (request.getUrl().matches(pattern)) {
                    break; //匹配到需要的链接便退出,得到request
                }
            }
            Restful api=new Restful();
            api.url=request.getUrl();
            api.method=request.getMethod().name();
            request.getQueryString().forEach(entry->{
                api.query.put(entry.getName(),entry.getValue());
            });
            api.body=request.getPostData().getText();
            return api;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //利用yaml发送请求
    public Response getResponseFromYaml(String path,HashMap<String,Object> map)  {
        //利用yaml文件拼接访问api
        ObjectMapper mapper=new ObjectMapper(new YAMLFactory());//将文件public数据以YAML形式序列化保存
        Restful api= null;
        try {
            api = mapper.readValue(Restful.class.getResourceAsStream(path),Restful.class);
            //这里就相当于重置了，不需要reset方法
            api=updateApiFromMap(api,map);
            return getResponseFromRestful(api);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    public Response getResponseFromHar(String path,String pattern ,HashMap<String,Object> map){
        //获取restful
        Restful api=getApiFromHar(path,pattern);
        api=updateApiFromMap(api,map);
        return getResponseFromRestful(api);
    }

    private String[] updateUrl(String url){
        //多环境的支持，替换host和ip
            HashMap<String,String> hosts=WeworkConfig.getInstance().env.get(WeworkConfig.getInstance().current);
            String host="";
            String urlNew="";
            for(Map.Entry<String,String> entry: hosts.entrySet()){
                if(url.contains(entry.getKey())){
                    host=  entry.getKey();
                    urlNew=url.replace(entry.getKey(),entry.getValue());
                }
            }
            return new String[]{host,urlNew};
    }

}
