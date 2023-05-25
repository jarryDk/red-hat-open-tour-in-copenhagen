package dk.jarry.quiz.boundary;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dk.jarry.quiz.entity.Quiz;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public abstract class AbstractService {

	@Inject
	@ConfigProperty(defaultValue = "quizs", name = "dynamoDbTableName")
	String dynamoDbTableName;

	public final static String QUIZ_UUID_COL = "uuid";
	public final static String QUIZ_TIME_STAMP_COL = "timeStamp";
	public final static String QUIZ_NAME_COL = "name";
	public final static String QUIZ_QUESTION_NO_ONE_COL = "questionNoOne";
	public final static String QUIZ_QUESTION_NO_TWO_COL = "questionNoTwo";
	public final static String QUIZ_QUESTION_NO_TREE_COL = "questionNoTree";

	public String getTableName() {
		return dynamoDbTableName;
	}

	protected ScanRequest getScanRequest() {
		return ScanRequest.builder() //
				.tableName(getTableName()) //
				.attributesToGet( //
						QUIZ_UUID_COL, //
						QUIZ_TIME_STAMP_COL, //
						QUIZ_NAME_COL, //
						QUIZ_QUESTION_NO_ONE_COL, //
						QUIZ_QUESTION_NO_TWO_COL, //
						QUIZ_QUESTION_NO_TREE_COL //
				).build();
	}

	/**
	 * Create
	 *
	 * @param quiz
	 * @return
	 */
	protected PutItemRequest getPutItemRequest(Quiz quiz) {

		Map<String, AttributeValue> item = new HashMap<>();
		item.put(QUIZ_UUID_COL, AttributeValue.builder().s(quiz.getUuid()).build());
		item.put(QUIZ_TIME_STAMP_COL, AttributeValue.builder().s(quiz.getTimeStamp()).build());
		item.put(QUIZ_NAME_COL, AttributeValue.builder().s(quiz.getName()).build());
		item.put(QUIZ_QUESTION_NO_ONE_COL, AttributeValue.builder().s(quiz.getQuestionNoOne()).build());
		item.put(QUIZ_QUESTION_NO_TWO_COL, AttributeValue.builder().s(quiz.getQuestionNoTwo()).build());
		item.put(QUIZ_QUESTION_NO_TREE_COL, AttributeValue.builder().s(quiz.getQuestionNoTree()).build());

		return PutItemRequest.builder() //
				.tableName(getTableName()) //
				.item(item) //
				.build();
	}

	/**
	 * Read
	 *
	 * @param uuid
	 * @return
	 */
	protected GetItemRequest getGetItemRequest(String uuid) {

		Map<String, AttributeValue> key = new HashMap<>();
		key.put(QUIZ_UUID_COL, AttributeValue.builder().s(uuid).build());

		return GetItemRequest.builder() //
				.tableName(getTableName()) //
				.key(key) //
				.attributesToGet( //
						QUIZ_UUID_COL, //
						QUIZ_TIME_STAMP_COL, //
						QUIZ_NAME_COL, //
						QUIZ_QUESTION_NO_ONE_COL, //
						QUIZ_QUESTION_NO_TWO_COL, //
						QUIZ_QUESTION_NO_TREE_COL //
				).build();
	}

	protected UpdateItemRequest getUpdateItemRequest(String uuid, Quiz quiz) {

		Map<String, AttributeValue> key = new HashMap<>();
		key.put(QUIZ_UUID_COL, AttributeValue.builder().s(uuid).build());

		Map<String, AttributeValue> items = new HashMap<>();
		items.put(":" + QUIZ_NAME_COL, AttributeValue.builder().s(quiz.getName()).build());
		items.put(":" + QUIZ_TIME_STAMP_COL, AttributeValue.builder().s(quiz.getTimeStamp()).build());
		items.put(":" + QUIZ_QUESTION_NO_ONE_COL, AttributeValue.builder().s(quiz.getQuestionNoOne()).build());
		items.put(":" + QUIZ_QUESTION_NO_TWO_COL, AttributeValue.builder().s(quiz.getQuestionNoTwo()).build());
		items.put(":" + QUIZ_QUESTION_NO_TREE_COL, AttributeValue.builder().s(quiz.getQuestionNoTree()).build());

		String updateExpression = "SET " + //
				QUIZ_NAME_COL + " = :" + QUIZ_NAME_COL + ", " + //
				QUIZ_TIME_STAMP_COL + " = :" + QUIZ_TIME_STAMP_COL + ", " + //
				QUIZ_QUESTION_NO_ONE_COL + " = :" + QUIZ_QUESTION_NO_ONE_COL + ", " + //
				QUIZ_QUESTION_NO_TWO_COL + " = :" + QUIZ_QUESTION_NO_TWO_COL + ", " + //
				QUIZ_QUESTION_NO_TREE_COL + " = :" + QUIZ_QUESTION_NO_TREE_COL;

		return UpdateItemRequest.builder() //
				.tableName(getTableName()) //
				.key(key) //
				.returnValues(ReturnValue.ALL_NEW) //
				.updateExpression(updateExpression) //
				.expressionAttributeValues(items).build();
	}

	protected DeleteItemRequest getDeleteItemRequest(String uuid) {

		Map<String, AttributeValue> key = new HashMap<>();
		key.put(QUIZ_UUID_COL, AttributeValue.builder().s(uuid).build());

		return DeleteItemRequest.builder() //
				.tableName(getTableName()) //
				.key(key) //
				.build();
	}

}
