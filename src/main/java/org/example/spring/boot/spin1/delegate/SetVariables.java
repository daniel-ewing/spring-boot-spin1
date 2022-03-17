package org.example.spring.boot.spin1.delegate;

import camundajar.impl.com.google.gson.Gson;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.example.spring.boot.spin1.model.Customer;
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

    String apiResponse =
        "{\"Get_Portfolio_Info_V1_Resp\":{\"Portfolio_Info\":{\"External_Asset_Mgr\":\n"
            + "\n"
            + "{\"Eam_Short_Name\":\"\",\"Eam_Code_Mcb\":\"\"}\n"
            + ",\"Info_Markers\":[\\{\"Sc_Info_Mkr_Mcb\":\"\",\"Sc_Info_Edt_Mcb\":\"\",\"Sc_Info_Std_Mcb\":\"\"}],\"Valuation_Amt\":\"0\",\"Closure_Date\":\"\",\"Cds_Local_Mcb\":\"\",\"Management_Type\":{\"Managed_Account\":\"99\",\"Managed_Account_Desc\":\"No Mandate\"},\"Reference_Currency\":\"XXX\",\"Portfolio_Manager\":\"PORTFOLIO_HOLDERS\",\"Date_Of_Valuation\":\"xxxxxxxxxxxxxxxxxx\",\"Reporting\":{\"Valuation_Currency\":\"XXX\",\"Add_Freq\":[\n"
            + "\n"
            + "{\"Additional_Freq\":\"\"}\n"
            + "],\"Statement_Freq\":\"xxxxxxxxxxxxxxxxxx\"},\"Joint_Holders\":[{\"Relation\":\n"
            + "\n"
            + "{\"Relation_Desc\":\"\",\"Relation_Code\":\"\"}\n"
            + ",\"Short_Name\":\"\",\"Joint_Holder\":\"\"}],\"Signatory_Powers\":[\\{\"Im_Sign_Note\":\"1 SIG: xxxxxxxxxxxxxxxxxx\"}],\"Special_Instructions\":{\"Sc_Spe_Inst_Mcb\":\"IFE(E): xxxxxxxxxxxxxxxxxx IFE(T): \"},\"Investment_Program\":{\"Investment_Program\":\"99\",\"Investment_Program_Desc\":\"xxxxxxxxxxxxxxxxxx\"},\"Portfolio_Relation_Officer\":{\"Account_Officer_Name\":\"xxxxxxxxxxxxxxxxxx\",\"Account_Officer\":\"4205\"},\"Charges_Info\":{\"Safekeep_Chrg_Acc_Ccy\":\"USD\",\"Adv_Chg_Scale\":\"999\",\"Safecustody_Freq\":\"xxxxxxxxxxxxxxxxxx\",\"Adv_Last_Run\":\"\",\"Safekeep_Chrg_Acc\":\"xxxxxxxxxxxxxxxxxx\",\"Safe_Last_Run\":\"\",\"Advisory_Freq\":\"xxxxxxxxxxxxxxxxxx\",\"Pm_Grp_Pr_Mcb\":\"\"},\"Mcb_Desk\":{\"Pb_Desk_Specifics\":{\"Agt_Sign_Dt_Mcb\":\"\",\"Benchmark_Info\":{\"Benchmark_Ref_Ccy\":\"\",\"Benchmark\":\"\",\"Benchmark_Desc\":\"\"}},\"Sc_Pf_Desk_Mcb_Desc\":\"PB EAM Desk\",\"Sc_Pf_Desk_Mcb\":\"02\"},\"Acct_Category\":[{\"Account_Category_Desc\":\"xxxxxxxxxxxxxxxxxx\",\"Account_Category\":\"1100\",\"Acct\":[\n"
            + "\n"
            + "{\"Account_Nos_Ccy\":\"XXX\",\"Account_Nos\":\"xxxxxxxxxxxxxxxxxx\"}\n"
            + "]}],\"Account_Name\":\"Name xxxxxxxxxxxxxxxxxx-1\",\"Block_Portfolio\":{\"Blocking_Narr\":\"\",\"Blocking_Date\":\"\"},\"Emails\":[\\{\"De_Address_Id\":\"\",\"Email_1\":\"\"}],\"Start_Date\":\"xxxxxxxxxxxxxxxxxx\",\"Mailing_Address\":{\"Despatch_Code\":\"OV\",\"De_Address_Id\":\"xxxxxxxxxxxxxxxxxx.C-xxxxxxxxxxxxxxxxxx.PRINT.1\",\"Address_Line2\":\"xxxxxxxxxxxxxxxxxx\",\"Post_Code\":\"POST-CODE\",\"Country_Code\":\"KY\",\"Street_Addr\":\"xxxxxxxxxxxxxxxxxx\",\"Country_Code_Num\":\"136\",\"Address_Line3\":\"xxxxxxxxxxxxxxxxxx\",\"Town_Country\":\"xxxxxxxxxxxxxxxxxx\",\"Country\":\"COUNTRY\",\"Name_2\":\"\",\"Name_1\":\"NAME-1\",\"Contact_Add\":\"\"},\"Main_Holder\":{\"Short_Name\":\"SHORT.xxxxxxxxxxxxxxxxxx\",\"Customer_Number\":\"xxxxxxxxxxxxxxxxxx\"}}},\"Exception\":{\"Msg\":\"\",\"Code\":\"0\"}}";

        ObjectValue typedObjectVariableJsonTransient = Variables.objectValue(apiResponse, true).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        ObjectValue typedObjectVariableJson = Variables.objectValue(apiResponse, false).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();

        delegateExecution.setVariable("apiResponseTransient", typedObjectVariableJsonTransient);
        delegateExecution.setVariable("apiResponse", typedObjectVariableJson);

        ObjectValue apiResponseTransientObject = delegateExecution.getVariableTyped("apiResponseTransient");
        String apiResponseTransient = apiResponseTransientObject.getValue().toString();

        LOGGER.info("-----> {}: apiResponseTransient =\n{}", processKey, apiResponseTransient);

        LOGGER.info("-----> execute: Exit {}", processKey);
    }
}
