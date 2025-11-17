package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Record;
import cat.udl.eps.softarch.demo.repository.RecordRepository;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class RecordEventHandler {
    final RecordRepository recordRepository;

    public RecordEventHandler(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @HandleBeforeCreate
    public void handleRecordPreCreate(Record record) {
        ZonedDateTime timeStamp = ZonedDateTime.now();
        record.setCreated(timeStamp);
        record.setModified(timeStamp);
    }

    @HandleBeforeSave
    public void handleRecordPreSave(Record record) {
        ZonedDateTime timeStamp = ZonedDateTime.now();
        record.setModified(timeStamp);
    }
}
