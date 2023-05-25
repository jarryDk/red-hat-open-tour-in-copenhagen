package dk.jarry.quiz.entity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import dk.jarry.quiz.boundary.AbstractService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RegisterForReflection
public class Quiz {

	@Schema(readOnly = true)
	public String uuid;
	@Schema(readOnly = true)
	public String timeStamp;
	public String name;
	public String questionNoOne;
	public String questionNoTwo;
	public String questionNoTree;

	public Quiz() {
		this.uuid = UUID.randomUUID().toString();
		this.timeStamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
	}

	public Quiz(
			String name,
			String questionNoOne,
			String questionNoTwo,
			String questionNoTree) {
		this.uuid = UUID.randomUUID().toString();
		this.timeStamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
		this.name = name;
		this.questionNoOne = questionNoOne;
		this.questionNoTwo = questionNoTwo;
		this.questionNoTree = questionNoTree;
	}

	public static Quiz from(Map<String, AttributeValue> item) {
		Quiz quiz = new Quiz();
		if (item != null && !item.isEmpty()) {
			quiz.setUuid(item.get(AbstractService.QUIZ_UUID_COL).s());
			quiz.setTimeStamp(item.get(AbstractService.QUIZ_TIME_STAMP_COL).s());
			quiz.setName(item.get(AbstractService.QUIZ_NAME_COL).s());
			quiz.setQuestionNoOne(item.get(AbstractService.QUIZ_QUESTION_NO_ONE_COL).s());
			quiz.setQuestionNoTwo(item.get(AbstractService.QUIZ_QUESTION_NO_TWO_COL).s());
			quiz.setQuestionNoTree(item.get(AbstractService.QUIZ_QUESTION_NO_TREE_COL).s());
		}
		return quiz;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuestionNoOne() {
		return questionNoOne;
	}

	public void setQuestionNoOne(String questionNoOne) {
		this.questionNoOne = questionNoOne;
	}

	public String getQuestionNoTwo() {
		return questionNoTwo;
	}

	public void setQuestionNoTwo(String questionNoTwo) {
		this.questionNoTwo = questionNoTwo;
	}

	public String getQuestionNoTree() {
		return questionNoTree;
	}

	public void setQuestionNoTree(String questionNoTree) {
		this.questionNoTree = questionNoTree;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((questionNoOne == null) ? 0 : questionNoOne.hashCode());
		result = prime * result + ((questionNoTwo == null) ? 0 : questionNoTwo.hashCode());
		result = prime * result + ((questionNoTree == null) ? 0 : questionNoTree.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quiz other = (Quiz) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (questionNoOne == null) {
			if (other.questionNoOne != null)
				return false;
		} else if (!questionNoOne.equals(other.questionNoOne))
			return false;
		if (questionNoTwo == null) {
			if (other.questionNoTwo != null)
				return false;
		} else if (!questionNoTwo.equals(other.questionNoTwo))
			return false;
		if (questionNoTree == null) {
			if (other.questionNoTree != null)
				return false;
		} else if (!questionNoTree.equals(other.questionNoTree))
			return false;
		return true;
	}

}
