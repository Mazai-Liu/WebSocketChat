package lec.projects.chat.service;


import lec.projects.chat.entity.Record;
import lec.projects.chat.form.GetRecordsForm;
import lec.projects.chat.result.Result;

import java.util.List;

public interface RecordService {
    Result<List<Record>> getRecords(GetRecordsForm getRecordsForm);

    Result<?> insertRecord(Record record);

    Result<?> insertRecords(List<Record> records);
}
