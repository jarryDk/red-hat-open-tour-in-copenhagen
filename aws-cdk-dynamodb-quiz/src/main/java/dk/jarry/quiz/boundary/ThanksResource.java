package dk.jarry.quiz.boundary;

import dk.jarry.quiz.entity.Quiz;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("thanks")
public class ThanksResource {

    @Inject
    QuizService quizService;

    @Inject
    Template thanks;

    @POST
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance post( //
            @FormParam(AbstractService.QUIZ_NAME_COL) String name,
            @FormParam(AbstractService.QUIZ_QUESTION_NO_ONE_COL) String questionNoOne,
            @FormParam(AbstractService.QUIZ_QUESTION_NO_TWO_COL) String questionNoTwo,
            @FormParam(AbstractService.QUIZ_QUESTION_NO_TREE_COL) String questionNoTree) {

        Quiz quiz = quizService.create( //
                new Quiz(name, questionNoOne, questionNoTwo, questionNoTree));

        return thanks //
                .data(AbstractService.QUIZ_UUID_COL, quiz.getUuid()) //
                .data(AbstractService.QUIZ_TIME_STAMP_COL, quiz.getTimeStamp()) //
                .data(AbstractService.QUIZ_NAME_COL, quiz.getName()) //
                .data(AbstractService.QUIZ_QUESTION_NO_ONE_COL, quiz.getQuestionNoOne()) //
                .data(AbstractService.QUIZ_QUESTION_NO_TWO_COL, quiz.questionNoTwo) //
                .data(AbstractService.QUIZ_QUESTION_NO_TREE_COL, quiz.getQuestionNoTree());
    }

}
