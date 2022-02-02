package org.camunda.bpm.spring.boot.spin1.delegate;

import camundajar.impl.com.google.gson.Gson;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.spring.boot.spin1.model.Customer;
import org.camunda.spin.Spin;
import org.camunda.spin.json.SpinJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SetVariables implements JavaDelegate {
    private final static Logger LOGGER = LoggerFactory.getLogger(SetVariables.class);

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String processKey = repositoryService.getProcessDefinition(delegateExecution.getProcessDefinitionId()).getKey();

        LOGGER.info("-----> execute: Enter {}", processKey);

        String taskNumbers = "{ \"taskNumber\" : [ \"A\", \"B\", \"C\" ] }";
        SpinJsonNode spinJson = Spin.JSON(taskNumbers);
        SpinJsonNode spinList = spinJson.prop("taskNumber");
        delegateExecution.setVariable("taskNumbers", spinList);

        Map<String, List<Customer>> accountCustomers = new HashMap<String, List<Customer>>();
        List<Customer> customersOfAccount1 = new ArrayList<Customer>();
        customersOfAccount1.add(new Customer("Account1", "First1", "Last1"));
        customersOfAccount1.add(new Customer("Account1", "First2", "Last2"));
        List<Customer> customersOfAccount2 = new ArrayList<Customer>();
        customersOfAccount2.add(new Customer("Account2", "First3", "Last3"));
        customersOfAccount2.add(new Customer("Account2", "First4", "Last4"));
        accountCustomers.put("Account1", customersOfAccount1);
        accountCustomers.put("Account2", customersOfAccount2);

        ObjectValue typedObjectVariableJava = Variables.objectValue(accountCustomers).serializationDataFormat(Variables.SerializationDataFormats.JAVA).create();
        delegateExecution.setVariable("accountCustomersJava", typedObjectVariableJava);

        ObjectValue typedObjectVariableJson = Variables.objectValue(accountCustomers).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        delegateExecution.setVariable("accountCustomersJson", typedObjectVariableJson);

        delegateExecution.setVariable("accountCustomersGsonToJson", new Gson().toJson(accountCustomers));

        Map<String, String> mapWithNullValue = new HashMap<String, String>();

        // The behavior seems a bit unpredictable as to which Map entry will be used to determine the type.
        // Sometimes you get a NPE and sometimes you don't.

        // This usually generates a NPE (a map with only two entries):
        mapWithNullValue.put("key0", "value0");
        mapWithNullValue.put("key1", null);

        // This usually works (another map, again with only two entries):
//        mapWithNullValue.put("key1", "value1");
//        mapWithNullValue.put("key2", null);

        // This always generates a NPE (a map with only one entry):
//        mapWithNullValue.put("key1", null);

        typedObjectVariableJava = Variables.objectValue(mapWithNullValue).serializationDataFormat(Variables.SerializationDataFormats.JAVA).create();
        delegateExecution.setVariable("mapWithNullValueJava", typedObjectVariableJava);

        typedObjectVariableJson = Variables.objectValue(mapWithNullValue).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        delegateExecution.setVariable("mapWithNullValueJson", typedObjectVariableJson);

        LOGGER.info("-----> execute: Exit {}", processKey);
    }
}
