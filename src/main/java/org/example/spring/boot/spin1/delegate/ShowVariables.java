package org.example.spring.boot.spin1.delegate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.spin.json.SpinJsonNode;
import org.example.spring.boot.spin1.model.Customer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component("ShowVariables")
@Slf4j
public class ShowVariables implements JavaDelegate {
    private RepositoryService repositoryService;
    private HistoryService historyService;

    public ShowVariables(RepositoryService repositoryService, HistoryService historyService) {
        this.repositoryService = repositoryService;
        this.historyService = historyService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String processKey = repositoryService.getProcessDefinition(delegateExecution.getProcessDefinitionId()).getKey();

        if (log.isDebugEnabled()) log.debug("-----> execute: Enter {} - {}", processKey, delegateExecution.getCurrentActivityId());

        SpinJsonNode taskNumbers = (SpinJsonNode) delegateExecution.getVariable("taskNumbers");
        if (log.isDebugEnabled()) log.debug("-----> {}: taskNumbers = {}", processKey, taskNumbers.toString());

        ObjectValue accountCustomersObject = delegateExecution.getVariableTyped("accountCustomersJava");
        processAccountCustomersObject(accountCustomersObject, processKey, "Java");

        accountCustomersObject = delegateExecution.getVariableTyped("accountCustomersJson");
        processAccountCustomersObject(accountCustomersObject, processKey, "Json");

        try {
            accountCustomersObject = delegateExecution.getVariableTyped("accountCustomersGsonToJson");
            processAccountCustomersObject(accountCustomersObject, processKey, "GsonToJson");
        }
        catch (ClassCastException e) {
            if (e.getMessage().contains("PrimitiveTypeValueImpl$StringValueImpl cannot be cast to class")) {
                String accountCustomersString = delegateExecution.getVariable("accountCustomersGsonToJson").toString();
                TypeReference<HashMap<String, List<Customer>>> typeReference = new TypeReference<HashMap<String, List<Customer>>>() {};
                Map<String, List<Customer>> accountCustomers = new ObjectMapper().readValue(accountCustomersString, typeReference);
                for (Map.Entry<String, List<Customer>> arrayEntry : accountCustomers.entrySet()) {
                    for (Customer customer : arrayEntry.getValue()) {
                        if (log.isDebugEnabled()) log.debug("-----> {}: Deserialized {}: Account Name = {} - Customer Name = {} {}",
                                processKey, "GsonToJson String", customer.getAccount(), customer.getFirstName(), customer.getLastName());
                        // Do something with Customer.
                    }
                }
            }
        }


        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().variableName("accountCustomersGsonToJson").list().get(0);
        String getName = historicVariableInstance.getTypedValue().getType().getName();
        String getTypeName = historicVariableInstance.getTypeName();

        if (log.isDebugEnabled()) log.debug("-----> execute: accountCustomersGsonToJson - getName = {}", getName);
        if (log.isDebugEnabled()) log.debug("-----> execute: accountCustomersGsonToJson - getTypeName = {}", getTypeName);

        historicVariableInstance = historyService.createHistoricVariableInstanceQuery().variableName("accountCustomersJson").list().get(0);
        getName = historicVariableInstance.getTypedValue().getType().getName();
        getTypeName = historicVariableInstance.getTypeName();

        if (log.isDebugEnabled()) log.debug("-----> execute: accountCustomersJson - getName = {}", getName);
        if (log.isDebugEnabled()) log.debug("-----> execute: accountCustomersJson - getTypeName = {}", getTypeName);

         historicVariableInstance = historyService.createHistoricVariableInstanceQuery().variableName("customersOfAccount1").list().get(0);
         getName = historicVariableInstance.getTypedValue().getType().getName();
         getTypeName = historicVariableInstance.getTypeName();

        if (log.isDebugEnabled()) log.debug("-----> execute: customersOfAccount1 - getName = {}", getName);
        if (log.isDebugEnabled()) log.debug("-----> execute: customersOfAccount1 - getTypeName = {}", getTypeName);

        if (log.isDebugEnabled()) log.debug("-----> execute: Exit {} - {}", processKey, delegateExecution.getCurrentActivityId());
    }

    private void processAccountCustomersObject(ObjectValue accountCustomersObject, String processKey, String type) {
        Map<String, List<Customer>> accountCustomers = (HashMap<String, List<Customer>>) accountCustomersObject.getValue();
        Iterator<Map.Entry<String, List<Customer>>> mapIterator = accountCustomers.entrySet().iterator();
        for (Map.Entry<String, List<Customer>> arrayEntry : accountCustomers.entrySet()) {
            for (Customer customer : arrayEntry.getValue()) {
                if (log.isDebugEnabled()) log.debug("-----> {}: Deserialized {}: Account Name = {} - Customer Name = {} {}",
                        processKey, type, customer.getAccount(), customer.getFirstName(), customer.getLastName());
                // Do something with Customer.
            }
        }
    }
}
