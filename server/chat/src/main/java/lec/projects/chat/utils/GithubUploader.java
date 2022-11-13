package lec.projects.chat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Component
@Slf4j
public class GithubUploader {

    public static final String URI_SEPARATOR = "/";

    public static final Set<String> ALLOW_FILE_SUFFIX = new HashSet<>(Arrays.asList("jpg", "png", "jpeg", "gif"));

    public static final String AVATAR_PREFIX = "img/chat/avatar/";

    @Value("${github.bucket.url}")
    private String url;

    @Value("${github.bucket.api}")
    private String api;

    @Value("${github.bucket.access-token}")
    private String accessToken;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 上传文件到Github
     * @param multipartFile
     * @return 文件的访问地址
     * @throws IOException
     */
    public String upload (MultipartFile multipartFile) throws IOException {
        // 重命名文件
        String fileName = getNewFileName(multipartFile);

        // 目录按照日期打散
        String[] folders = this.getDateFolder();

        // 最终的文件路径
        String filePath = new StringBuilder(String.join(URI_SEPARATOR, folders)).append("/").append(fileName).toString();

        log.info("上传文件到Github：{}", filePath);

        HashMap<String, String> map = new HashMap<>();
        map.put("message","file upload");
        map.put("content",Base64.getEncoder().encodeToString(multipartFile.getBytes()));

        String json = JSON.toJSONString(map);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "token " + this.accessToken);

        ResponseEntity<String> responseEntity =
                this.restTemplate.exchange(this.api + filePath, HttpMethod.PUT,
                new HttpEntity<String>(json, httpHeaders), String.class);

        if (responseEntity.getStatusCode().isError()) {
            // TODO 上传失败
        }

        log.info("上传完毕: {}", JSON.parse(responseEntity.getBody()));

        // TODO 序列化到磁盘备份
        return this.url + filePath;
    }

    public JSONObject uploadAvatar (MultipartFile multipartFile){
        String fileName = getNewFileName(multipartFile);
        // 最终的文件路径
        String filePath = AVATAR_PREFIX + fileName;

        HashMap<String, String> map = new HashMap<>();
        map.put("message","file upload");
        try {
            map.put("content",Base64.getEncoder().encodeToString(multipartFile.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("成功上传头像到Github：{}", filePath);
        return process(filePath,HttpMethod.PUT,map);
    }

    public JSONObject deleteAvatar(String filePath){
//        String filePath = AVATAR_PREFIX + fileName;
        String sha = process(filePath,HttpMethod.GET,null).get("sha").toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("message","file delete");
        map.put("sha",sha);
        log.info("成功删除头像{}", filePath);
        return process(filePath,HttpMethod.DELETE,map);
    }

    public String getNewFileName(MultipartFile multipartFile){
        String suffix = this.getSuffix(multipartFile.getOriginalFilename()).toLowerCase();
//        if (!ALLOW_FILE_SUFFIX.contains(suffix)) {
//            throw new IllegalArgumentException("不支持的文件后缀：" + suffix);
//        }

        // 重命名文件
        return UUID.randomUUID().toString().replace("-", "") + "." + suffix;
    }

    public JSONObject process(String filePath, HttpMethod method,HashMap<String,String> requestbody){
        String body = JSON.toJSONString(requestbody);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "token " + this.accessToken);

        ResponseEntity<JSONObject> responseEntity =
                this.restTemplate.exchange(this.api + filePath, method,
                        new HttpEntity(body, httpHeaders), JSONObject.class);

        if (responseEntity.getStatusCode().isError()) {
            // TODO process失败
        }

        log.info("执行完毕: {}", responseEntity.getBody());
          return responseEntity.getBody();
    }

    /**
     * 获取文件的后缀
     * @param fileName
     * @return
     */
    protected String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            String suffix = fileName.substring(index + 1);
            if (!suffix.isEmpty()) {
                return suffix;
            }
        }
        return null;
//        throw new IllegalArgumentException("非法的文件名称：" + fileName);
    }

    /**
     * 按照年月日获取打散的打散目录
     * yyyy/mmd/dd
     * @return
     */
    protected String[] getDateFolder() {
        String[] retVal = new String[3];

        LocalDate localDate = LocalDate.now();
        retVal[0] = localDate.getYear() + "";

        int month = localDate.getMonthValue();
        retVal[1] = month < 10 ? "0" + month : month + "";

        int day = localDate.getDayOfMonth();
        retVal[2] = day < 10 ? "0" + day : day + "";

        return retVal;
    }
}
