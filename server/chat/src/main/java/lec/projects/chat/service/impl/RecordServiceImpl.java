package lec.projects.chat.service.impl;

import lec.projects.chat.entity.Record;
import lec.projects.chat.enums.MessageAndCode;
import lec.projects.chat.form.GetRecordsForm;
import lec.projects.chat.result.Result;
import lec.projects.chat.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 获取两人间的聊天记录
     * @param getRecordsForm 消息发送者，接受者
     * @return 二者的聊天记录
     */
    @Override
    public Result<List<Record>> getRecords(GetRecordsForm getRecordsForm) {
        String fromString = getRecordsForm.getFromName(), toString = getRecordsForm.getToName();

        // 这是聊天室的聊天内容
        if(toString.equals("Lec")){
            Criteria oneTo = Criteria.where("toName").is(toString);
            Query query = new Query(oneTo);
            List<Record> records = mongoTemplate.find(query, Record.class, "record");

            return Result.success(MessageAndCode.OK,records);
        }

        // select (fromName = a and toName = b) || (fromName = b and toName = a)

        // one part
        Criteria oneFrom = Criteria.where("fromName").is(fromString);
        Criteria oneTo = Criteria.where("toName").is(toString);

        Criteria oneAndCriteria = new Criteria();
        oneAndCriteria.andOperator(oneFrom, oneTo);

        // two part
        Criteria twoFrom = Criteria.where("fromName").is(toString);
        Criteria twoTo = Criteria.where("toName").is(fromString);

        Criteria twoAndCriteria = new Criteria();
        twoAndCriteria.andOperator(twoFrom, twoTo);

        // or Operator
        Criteria threeAndCriteria = new Criteria();
        threeAndCriteria.orOperator(oneAndCriteria,twoAndCriteria);

        Query query = new Query(threeAndCriteria);
        List<Record> records = mongoTemplate.find(query, Record.class, "record");

        return Result.success(MessageAndCode.OK,records);
    }


    /**
     * 在用户修改完头像后，更新历史聊天记录中的用户头像
     * @param username 修改头像的用户
     * @param newAvatarPath 新头像的图床路径
     */
    public void setAvatarAfterChanged(String username, String newAvatarPath){
        Criteria criteria = Criteria.where("fromName").is(username);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("fromAvatar",newAvatarPath);
        mongoTemplate.updateMulti(query,update,Record.class,"record");
    }

    /**
     *  插入聊天记录
     * @param record 一条聊天记录
     * @return
     */
    @Override
    public Result<?> insertRecord(Record record) {
        Record record1 = mongoTemplate.insert(record, "record");
        System.out.println(record1);
        return Result.success(MessageAndCode.OK, null);
    }

    public Result<?> insertRecords(List<Record> records) {
        mongoTemplate.insert(records, "record");
        return Result.success(MessageAndCode.OK, null);
    }
}
