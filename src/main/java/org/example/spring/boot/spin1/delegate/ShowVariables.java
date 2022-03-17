package org.example.spring.boot.spin1.delegate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.example.spring.boot.spin1.model.Customer;
import org.camunda.spin.json.SpinJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ShowVariables implements JavaDelegate {
     private final static Logger LOGGER = LoggerFactory.getLogger(ShowVariables.class);

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String processKey = repositoryService.getProcessDefinition(delegateExecution.getProcessDefinitionId()).getKey();

        LOGGER.info("-----> execute: Enter {}", processKey);

        ObjectValue apiResponseObject = delegateExecution.getVariableTyped("apiResponse");
        String apiResponse = apiResponseObject.getValue().toString();

        LOGGER.info("-----> {}: apiResponse =\n{}", processKey, apiResponse);

        LOGGER.info("-----> execute: Exit {}", processKey);
    }

}
