package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.AnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OpenEndedAnswerDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OpenEndedStatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.*;

import javax.persistence.*;

@Entity
@DiscriminatorValue(Question.QuestionTypes.OPEN_ENDED_QUESTION)
public class OpenEndedAnswer extends AnswerDetails {
    private String answer;
    private String defaultAnswer;
    
    public OpenEndedAnswer() {
        super();
    }

    public OpenEndedAnswer(QuestionAnswer answer) {
        super(answer);
    }

    public OpenEndedAnswer(QuestionAnswer answer, String text) {
        super(answer);
        this.setAnswer(text);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnswer(OpenEndedQuestion question, OpenEndedStatementAnswerDetailsDto answerDetailsDto) {
        answer = answerDetailsDto.getAnswer();
        defaultAnswer = question.getDefaultAnswer();
    }

    @Override
    public void remove() {
        this.setAnswer(null);
    }

    @Override
    public AnswerDetailsDto getAnswerDetailsDto() {
        return new OpenEndedAnswerDto(this);
    }

    @Override
    public boolean isCorrect() {
        return answer.equals(defaultAnswer);
    }

    @Override
    public boolean isAnswered() {
        /* 
            It is considered NOT answered if:
            - The answer is null;
            - The answer is a zero-length string;
            - The answer is just whitespace;
        */

        return this.answer != null && !this.answer.strip().equals("");
    }

    @Override
    public String getAnswerRepresentation() {
        return this.isAnswered() ? this.answer : "";
    }

    @Override
    public StatementAnswerDetailsDto getStatementAnswerDetailsDto() {
        return new OpenEndedStatementAnswerDetailsDto(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitAnswerDetails(this);
    }
}
