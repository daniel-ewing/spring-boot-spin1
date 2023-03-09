package org.example.spring.boot.spin1.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("SetVariables")
@Slf4j
public class SetVariables implements JavaDelegate {
    private final RepositoryService repositoryService;

    public SetVariables(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String processKey = repositoryService.getProcessDefinition(delegateExecution.getProcessDefinitionId()).getKey();

        if (log.isDebugEnabled()) log.debug("-----> execute: Enter {} - {}", processKey, delegateExecution.getCurrentActivityId());

        List customers1 = new ArrayList<>();
        customers1.add(new org.example.spring.boot.spin1.model.original.Customer("Account1", "First1", "Last1"));
        customers1.add(new org.example.spring.boot.spin1.model.original.Customer("Account1", "First2", "Last2"));

        List customers2 = new ArrayList<>();
        customers2.add(new org.example.spring.boot.spin1.model.updated.Customer("Account2", "First1", "Last1"));
        customers2.add(new org.example.spring.boot.spin1.model.updated.Customer("Account2", "First2", "Last2"));

        delegateExecution.setVariable("customers1", customers1);
        delegateExecution.setVariable("customers2", customers2);

        if (log.isDebugEnabled()) log.debug("-----> execute: Exit {} - {}", processKey, delegateExecution.getCurrentActivityId());
    }
}
