package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.OpenEndedAnswer;

public class OpenEndedAnswerDto extends AnswerDetailsDto {
    private String answer;

    public OpenEndedAnswerDto() {
    }

    public OpenEndedAnswerDto(OpenEndedAnswer answer) {
        String answerRep = answer.getAnswerRepresentation();
        this.setAnswer(answerRep);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer.strip() == "" ? null : answer;
    }
}
