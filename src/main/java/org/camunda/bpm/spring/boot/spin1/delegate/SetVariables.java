package org.camunda.bpm.spring.boot.spin1.delegate;

import camundajar.impl.com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
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
@Slf4j
public class SetVariables implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        if (log.isDebugEnabled()) log.debug("-----> execute: Enter - {}", delegateExecution.getCurrentActivityId());

        List<String> list = new ArrayList<>();
        list.add("Grundregelset");
        list.add("Behoerdenregelset");
        list.add("Beschlussregelset");
        list.add("Ursprungsbeschlussregelset");
        list.add("Berichtsregelset");

        ObjectValue typedObjectVariableJava = Variables.objectValue(list).serializationDataFormat(Variables.SerializationDataFormats.JAVA).create();
        delegateExecution.setVariable("deserializedList", list);
        delegateExecution.setVariable("serializedList", typedObjectVariableJava);

        if (log.isDebugEnabled()) log.debug("-----> execute: Exit - {}", delegateExecution.getCurrentActivityId());
    }
}
