Feature: Testing External Command Executor

  Scenario: Execute yarrrml-parser command
    #Given A rules file name "rules.yml"
    When I execute the command with the file name "rules.yml" and the output file name "rules.rml.ttl"
    #Then The file "rules.rml.ttl" should be generated
