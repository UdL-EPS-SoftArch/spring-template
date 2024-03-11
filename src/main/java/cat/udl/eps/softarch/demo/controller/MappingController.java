package cat.udl.eps.softarch.demo.controller;
import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.exception.NotAuthorizedException;
import cat.udl.eps.softarch.demo.exception.NotFoundException;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@RepositoryRestController
public class MappingController {
    private final MappingRepository mappingRepository;

    public MappingController(MappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }

    @RequestMapping(value = "/mappings/{id}", method = RequestMethod.GET)
    public @ResponseBody PersistentEntityResource getMapping(PersistentEntityResourceAssembler resourceAssembler,
                                                             @PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new NotAuthorizedException();
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Mapping> mapping = mappingRepository.findById(id);

        if (mapping.isEmpty()) {
            throw new NotFoundException();
        }

        Mapping m = mapping.get();

        if (m.getProvidedBy().getId().equals(user.getId())) {
            return resourceAssembler.toFullResource(m);
        } else {
            throw new NotAuthorizedException();
        }
    }

}
