package org.example.spring.boot.spin1.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ShowVariables")
@Slf4j
public class ShowVariables implements JavaDelegate {
    private final static String originalCustomerClassName = "org.example.spring.boot.spin1.model.original.Customer";
    private final static String updatedCustomerClassName = "org.example.spring.boot.spin1.model.updated.Customer";
     private final RepositoryService repositoryService;

    public ShowVariables(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String processKey = repositoryService.getProcessDefinition(delegateExecution.getProcessDefinitionId()).getKey();

        if (log.isDebugEnabled()) log.debug("-----> execute: Enter {} - {}", processKey, delegateExecution.getCurrentActivityId());

        List customers1 = (List) delegateExecution.getVariable("customers1");
        processCustomers(customers1, processKey);
        List customers2 = (List) delegateExecution.getVariable("customers2");
        processCustomers(customers2, processKey);

        if (log.isDebugEnabled()) log.debug("-----> execute: Exit {} - {}", processKey, delegateExecution.getCurrentActivityId());
    }
    private void processCustomers(List customers, String processKey) {
        if (customers.isEmpty()) return;
        Object testCustomer = customers.get(0);
        if (testCustomer != null) {
            if (originalCustomerClassName.equals(testCustomer.getClass().getCanonicalName())) {
                List<org.example.spring.boot.spin1.model.original.Customer> originalCustomers = customers;
                for (org.example.spring.boot.spin1.model.original.Customer customer : originalCustomers) {
                    // Do something with org.example.spring.boot.spin1.model.original.Customer.

                    if (log.isDebugEnabled()) log.debug("-----> {}: Account Name = {} - Customer Name = {} {}",
                            processKey, customer.getAccount(), customer.getFirstName(), customer.getLastName());
                }
            } else if (updatedCustomerClassName.equals(testCustomer.getClass().getCanonicalName())) {
                List<org.example.spring.boot.spin1.model.updated.Customer> originalCustomers = customers;
                for (org.example.spring.boot.spin1.model.updated.Customer customer : originalCustomers) {
                    // Do something with org.example.spring.boot.spin1.model.updated.Customer.

                    if (log.isDebugEnabled()) log.debug("-----> {}: Account Name = {} - Customer Name = {} {}",
                            processKey, customer.getAccount(), customer.getFirstName(), customer.getLastName());
                }
            } else {
                if (log.isDebugEnabled()) log.debug("-----> {}: Unknown Canonical type = {}",
                        processKey, testCustomer.getClass().getCanonicalName());
            }
        }
    }

}
