package cat.udl.eps.softarch.demo.controller;

import cat.udl.eps.softarch.demo.domain.Column;
import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.domain.Supplier;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.exception.NotAuthorizedException;
import cat.udl.eps.softarch.demo.exception.NotFoundException;
import cat.udl.eps.softarch.demo.repository.ColumnRepository;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;
import java.util.Optional;

@RepositoryRestController
public class ColumnController {
    private final ColumnRepository columnRepository;
    private final MappingRepository mappingRepository;
    private final SupplierRepository supplierRepository;

    public ColumnController(ColumnRepository columnRepository, MappingRepository mappingRepository, SupplierRepository supplierRepository) {
        this.columnRepository = columnRepository;
        this.mappingRepository = mappingRepository;
        this.supplierRepository = supplierRepository;
    }

    @RequestMapping(value = "/columns/{id}", method = RequestMethod.GET)
    public @ResponseBody PersistentEntityResource getColumn(PersistentEntityResourceAssembler resourceAssembler,
                                                             @PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new NotAuthorizedException();
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Column> column = columnRepository.findById(id);

        if (column.isEmpty()) {
            throw new NotFoundException();
        }

        Column col = column.get();
        assert col.getColumnBelongsTo().getId() != null;
        Optional<Mapping> mappingBelongs = mappingRepository.findById(col.getColumnBelongsTo().getId());

        if (mappingBelongs.isPresent() && Objects.equals(mappingBelongs.get().getProvidedBy().getId(), user.getId())) {
            return resourceAssembler.toFullResource(col);
        } else {
            throw new NotAuthorizedException();
        }
    }

    @RequestMapping(value = "/columns/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteColumns(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentSupplierUsername = authentication.getName();
        Supplier currentSupplier = supplierRepository.findByUsernameContaining(currentSupplierUsername).get(0);

        Optional<Column> column = columnRepository.findById(id);

        if (column.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Column not found.");
        }

        Column col = column.get();
        assert col.getColumnBelongsTo().getId() != null;
        Optional<Mapping> mappingBelongs = mappingRepository.findById(col.getColumnBelongsTo().getId());

        if (mappingBelongs.isPresent() && Objects.equals(mappingBelongs.get().getProvidedBy().getId(), currentSupplier.getId())) {
            columnRepository.delete(col);
            return ResponseEntity.ok("Column deleted successfully.");
        } else {
            throw new NotAuthorizedException();
        }
    }
}
