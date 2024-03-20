package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

//@Entity
//@Data
//@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class YamlMapping extends Mapping {

    @JsonProperty
    private Map<String, String> prefixes;
    @JsonProperty
    private Map<String, Mappings> mappings;

    @OneToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Mapping extendsOf;

    public void setPrefixes(Map<String, String> prefixes) {
        this.prefixes = prefixes;
    }

    public void setMappings(Map<String, Mappings> mappings) {
        this.mappings = mappings;
    }

    public void setExtendsOf(Mapping extendsOf) {
        this.extendsOf = extendsOf;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Mappings {
        @JsonProperty
        private List<Sources> sources;

        @JsonProperty
        private String s;
        @JsonProperty
        private List<PredicateObject> po;


        public void setSources(List<Sources> sources) {
            this.sources = sources;
        }

        public void setS(String s) {
            this.s = s;
        }

        public void setPo(List<PredicateObject> po) {
            this.po = po;
        }
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PredicateObject {
        @JsonProperty
        private String p;
        @JsonProperty
        private PropertyValue o;

        public void setP(String p) {
            this.p = p;
        }

        public void setO(PropertyValue o) {
            this.o = o;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PropertyValue {
        @JsonProperty
        private String value;
        @JsonProperty
        private String datatype;

        public PropertyValue(String value, String datatype) {
            this.value = value;
            this.datatype = datatype;
        }

        public PropertyValue(String value) {
            this.value = value;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Sources {
        @JsonProperty
        private String access;
        @JsonProperty
        private String referenceFormulation;

        public void setAccess(String access) {
            this.access = access;
        }

        public void setReferenceFormulation(String referenceFormulation) {
            this.referenceFormulation = referenceFormulation;
        }
    }
}