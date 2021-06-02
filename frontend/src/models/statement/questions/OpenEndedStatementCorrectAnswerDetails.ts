import StatementCorrectAnswerDetails from '@/models/statement/questions/StatementCorrectAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class OpenEndedStatementCorrectAnswerDetails extends StatementCorrectAnswerDetails {
  public defaultAnswer: string = '';

  constructor(jsonObj?: OpenEndedStatementCorrectAnswerDetails) {
    super(QuestionTypes.OpenEnded);
    if (jsonObj) {
      this.defaultAnswer = jsonObj.defaultAnswer;
    }
  }
}
