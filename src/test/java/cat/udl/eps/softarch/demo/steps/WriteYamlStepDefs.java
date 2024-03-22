package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.domain.YamlMapping;
import cat.udl.eps.softarch.demo.repository.ColumnRepository;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class WriteYamlStepDefs {
    final StepDefs stepDefs;

    protected MockMvc mockMvc;

    protected ResultActions result;

    final MappingRepository mappingRepository;
    final WebApplicationContext wac;

    final SupplierRepository supplierRepository;

    final ColumnRepository columnRepository;

    public static Mapping mappingFile;

    public WriteYamlStepDefs(StepDefs stepDefs, MappingRepository mappingRepository, WebApplicationContext wac, SupplierRepository supplierRepository, ColumnRepository columnRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.wac = wac;
        this.supplierRepository = supplierRepository;
        this.columnRepository = columnRepository;
    }

    @Given("Generate the Yaml file")
    public void generateTheYamlFile() throws IOException {

        YamlMapping.Mappings mapping = new YamlMapping.Mappings();

        Map<String, String> prefixes = new HashMap<>();
        prefixes.put("ex", "http://www.example.com/");
        prefixes.put("e", "http://myontology.com/");

        //mapping.setSources(List.of("CEP-2021-S1-WEIGHT.csv~csv"));

        YamlMapping.Sources sources = new YamlMapping.Sources();
        sources.setAccess("CEP-2021-S1-WEIGHT.csv");
        sources.setReferenceFormulation("csv");
        mapping.setSources(List.of(sources));

        mapping.setS("ex:$(Animal ID)");

        YamlMapping.PredicateObject po1 = new YamlMapping.PredicateObject();
        po1.setP("a");
        po1.setO(new YamlMapping.PropertyValue("schema:Pork"));

        YamlMapping.PredicateObject po2 = new YamlMapping.PredicateObject();
        po2.setP("schema:date");
        po2.setO(new YamlMapping.PropertyValue("$(Date)"));

        YamlMapping.PredicateObject po3 = new YamlMapping.PredicateObject();
        po3.setP("ex:weight");
        po3.setO(new YamlMapping.PropertyValue("$(Weight)", "xsd:integer"));

        mapping.setPo(Arrays.asList(po1, po2, po3));

        Map<String, YamlMapping.Mappings> mappings = new HashMap<>();
        mappings.put("animalWeight", mapping);

        YamlMapping yamlMapping = new YamlMapping();
        yamlMapping.setPrefixes(prefixes);

        yamlMapping.setMappings(mappings);

        YAMLFactory yamlFactory = new YAMLFactory();

        // Remove default quotes
        yamlFactory.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        // Remove --- from the start of the file
        yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);

        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.writeValue(new File("src/main/resources/pork.yaml"), yamlMapping);
    }

    @Then("Write the yaml file")
    public void writeTheYamlFile() throws IOException {
        Mapping mapping = mappingRepository.findByTitle("CEP-2021-S1-WEIGHT.csv").get(0);

        YamlMapping.Mappings yamlMappingsMap = new YamlMapping.Mappings();

        Map<String, String> prefixes = new HashMap<>();

        Map<String, String> prefixes4Factory = new HashMap<>();

        ArrayList<String> prefixList = new ArrayList<>(Arrays.asList(mapping.getPrefixesURIS().split(",")));
        AtomicInteger count = new AtomicInteger(1);

        prefixList.forEach(prefix -> {
            prefixes.put(prefix, "pre" + count);
            prefixes4Factory.put("pre" + count, prefix);
            count.getAndIncrement();
        });

        YamlMapping.Sources sources = new YamlMapping.Sources();
        sources.setAccess(mapping.getFileName());
        sources.setReferenceFormulation(mapping.getFileFormat());
        yamlMappingsMap.setSources(List.of(sources));

        ArrayList<YamlMapping.PredicateObject> poList = new ArrayList<>();

        columnRepository.findByColumnBelongsTo(mapping).forEach(column -> {
            if (column.getId() == 1) {
                yamlMappingsMap.setS(prefixes.get(column.getOntologyURI()) + ":$(" + column.getTitle() + ")");
                YamlMapping.PredicateObject po = new YamlMapping.PredicateObject();
                po.setP("a");
                po.setO(new YamlMapping.PropertyValue(column.getColumnBelongsTo().getMainOntology()));
                poList.add(po);

            } else {
                YamlMapping.PredicateObject po = new YamlMapping.PredicateObject();
                po.setP(prefixes.get(column.getOntologyURI()) + ":" + column.getOntologyType());
                po.setO(new YamlMapping.PropertyValue("$(" + column.getTitle() + ")", column.getDataType()));
                poList.add(po);

            }
        });
        yamlMappingsMap.setPo(poList);

        Map<String, YamlMapping.Mappings> mappings = new HashMap<>();
        mappings.put(mapping.getTitle(), yamlMappingsMap);

        YamlMapping yamlMapping = new YamlMapping();

        yamlMapping.setPrefixes(prefixes4Factory);
        yamlMapping.setMappings(mappings);

        YAMLFactory yamlFactory = new YAMLFactory();

        // Remove default quotes
        yamlFactory.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        // Remove --- from the start of the file
        yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);

        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.writeValue(new File("src/main/resources/porkDef.yaml"), yamlMapping);


    }
}
