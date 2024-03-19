//package cat.udl.eps.softarch.demo.handler;
//
//import cat.udl.eps.softarch.demo.domain.Mapping;
//import cat.udl.eps.softarch.demo.domain.Supplier;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.event.EventListener;
//import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
//import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
//import org.springframework.data.rest.core.event.BeforeCreateEvent;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.TransactionSystemException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.http.HttpStatus;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@RepositoryEventHandler
//public class MappingEventHandler {
//    final Logger logger = LoggerFactory.getLogger(Supplier.class);
//    /*
//    * When a validation error occurs during Mapping creation, the handleValidationExceptions catches the exception.
//    * It extracts the field names and corresponding errors messages and creates a structured response.
//    * This response it returned with a status code of 400
//    * */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @EventListener(BeforeCreateEvent.class)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        logger.info("Validation error: {}", ex.getMessage());
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }
//}
