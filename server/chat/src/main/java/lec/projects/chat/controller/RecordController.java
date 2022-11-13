package lec.projects.chat.controller;

import lec.projects.chat.entity.Record;
import lec.projects.chat.form.GetRecordsForm;
import lec.projects.chat.result.Result;
import lec.projects.chat.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/getRecords")
    public Result<List<Record>> getRecords(@RequestBody GetRecordsForm getRecordsForm){
        return recordService.getRecords(getRecordsForm);
    }

    @PostMapping("/insertRecord")
    public Result<?> insertRecord(@RequestBody Record record){
        return recordService.insertRecord(record);
    }
}

