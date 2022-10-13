package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Request;
import cat.udl.eps.softarch.demo.exception.ForbiddenException;
import cat.udl.eps.softarch.demo.repository.RequestRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class RequestEventHandler {

    final RequestRepository requestRepository;

    public RequestEventHandler(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /*
    @HandleBeforeCreate
    public void handleTransactionPreCreate(Request request) {
        assert request.getId() != null;
        if(requestRepository.existsById(request.getId())){
            throw new ForbiddenException();
        }
    }
*/
}
