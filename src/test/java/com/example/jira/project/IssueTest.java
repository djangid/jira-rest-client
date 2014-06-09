package com.example.jira.project;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jira.issue.Attachment;
import com.example.jira.issue.Component;
import com.example.jira.issue.Issue;
import com.example.jira.issue.IssueFields;
import com.example.jira.issue.IssueService;
import com.example.jira.issue.IssueType;
import com.example.jira.issue.Priority;

public class IssueTest {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void getIssue() throws IOException, ConfigurationException {
		String issueKey = "TEST-857";

		IssueService issueService = new IssueService();
		Issue issue =  issueService.getIssue(issueKey);

		logger.info(issue.toPrettyJsonString());

		// attachment info
		List<Attachment> attachs = issue.getFields().getAttachment();
		for ( Attachment a : attachs) 
			logger.info(a.toPrettyJsonString());
		
		IssueFields fields = issue.getFields();
		// 프로젝트키
		String prjKey =fields.getProject().getKey();
		//이슈 타입
		IssueType issueType = fields.getIssuetype();
		// 이슈 상세내역
		String desc = fields.getDescription();
	}
	
	@Test
	public void createIssue() throws IOException, ConfigurationException {

		Issue issue = new Issue();
		
		IssueFields fields = new IssueFields();
		
		fields.setProjectKey("TEST");
		fields.setSummary("something's wrong");
		fields.setIssueTypeName(IssueType.ISSUE_TYPE_TASK);
		fields.setDescription("Full description for issue");
		fields.setAssigneeName("lesstif");
		
		// Change Reporter need admin role
		fields.setReporterName("rest-api");
		fields.setPriorityName(Priority.PRIORITY_CRITICAL);
		fields.setLabels(new String[]{"bugfix","blitz_test"});
			
		fields.setComponents(Arrays.asList(new Component[]{new Component("Component-1"), new Component("Component-2")}));
		
		fields.addAttachment("c:\\Users\\lesstif\\test.pdf");
		fields.addAttachment("c:\\Users\\lesstif\\attachment.png");
		
		issue.setFields(fields);
		
		logger.info(issue.toPrettyJsonString());
		
		IssueService issueService = new IssueService();
		
		Issue genIssue = issueService.createIssue(issue);		
		
		//Print Generated issue
		logger.info(genIssue.toPrettyJsonString());
	}
	
	@Test
	public void uploadAttachments() throws IOException, ConfigurationException {
		Issue issue = new Issue();
		
		issue.setKey("TEST-834");
				
		issue.addAttachment(new File("c:\\Users\\lesstif\\attachment.png"));
		issue.addAttachment("c:\\Users\\lesstif\\test.pdf");
		
		IssueService issueService = new IssueService();
		issueService.postAttachment(issue);
	}
	
	@Test
	public void getAllIssueType() throws IOException, ConfigurationException {

		IssueService issueService = new IssueService();
		List<IssueType> issueTypes =  issueService.getAllIssueTypes();

		logger.info(issueTypes.toString());
	}
	
	@Test
	public void getAllPriorities() throws IOException, ConfigurationException {

		IssueService issueService = new IssueService();
		List<Priority> priority =  issueService.getAllPriorities();

		logger.info(priority.toString());
	}
	
	@Test
	public void getCustomeFields() throws IOException, ConfigurationException {
		IssueService issueService = new IssueService();
		Issue issue =  issueService.getIssue("TEST-834");

		logger.info(issue.getFields().getCustomfield().toString());		
	}
}
