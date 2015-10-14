package org.activiti;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyUnitTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@Deployment(resources = {"org/activiti/test/CollectionTestCase.bpmn"})
	public void testProcessVars() {
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("CollectionTestCase");
		assertNotNull(processInstance);
		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		assertEquals("Switch/Inspect", task.getName());
		Map<String,Object> processVariablesFromRuntime  = activitiRule.getRuntimeService().getVariablesLocal(processInstance.getId());
		Map<String,Object> processVariablesFromInstance = processInstance.getProcessVariables();
		Map<String,Object> processVariablesFromTask = task.getProcessVariables();
		
		assertEquals(3,processVariablesFromRuntime.size());
		assertEquals(3,processVariablesFromInstance.size());
		assertEquals(3,processVariablesFromTask.size());
		
	}
	@SuppressWarnings("unchecked")
	@Test
	@Deployment(resources = {"org/activiti/test/CollectionTestCase.bpmn"})
	public void test() {
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("CollectionTestCase");
		assertNotNull(processInstance);
		
		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		assertEquals("Switch/Inspect", task.getName());

		Map<String,Object> processVariables  = activitiRule.getRuntimeService().getVariablesLocal(processInstance.getId());
		
		ArrayList<String> collection = (ArrayList<String>) processVariables.get("collection");
		ArrayList<String> collectionSample = new ArrayList<String>();
		collectionSample.add("test1");
		collectionSample.add("test2");
		collectionSample.add("test3");
		assertEquals(collectionSample , collection);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("flow", "again");
		activitiRule.getFormService().submitTaskFormData(task.getId(), paramMap);
		
		processVariables = activitiRule.getRuntimeService().getVariablesLocal(processInstance.getId());
		
		
		ArrayList<String> laterCollection = (ArrayList<String>) processVariables.get("collection");
		assertEquals(collectionSample , laterCollection);
	}

}
