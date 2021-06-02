package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovy.json.JsonOutput
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import org.apache.http.HttpStatus 
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.*
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationGroupDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RemoveCombinationQuestionWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def question
    def response
    def student
    def teacher

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)

        student = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        student.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        userRepository.save(student)

        teacher = new User(USER_2_NAME, USER_2_EMAIL, USER_2_EMAIL,
                User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        teacher.authUser.setPassword(passwordEncoder.encode(USER_2_PASSWORD))
        userRepository.save(teacher)

        given: "create a question"
        question = new Question()
        question.setKey(1)
        question.setCourse(externalCourse)
        question.setTitle(QUESTION_1_TITLE)
        question.setContent(QUESTION_1_CONTENT)
        question.setStatus(Question.Status.AVAILABLE)

        def combinationQuestion = new CombinationQuestion()
        combinationQuestion.setLanguage(CODE_QUESTION_1_LANGUAGE)

        CombinationGroup group1 = new CombinationGroup()
        group1.setTitle(QUESTION_1_TITLE)
        def itemGroup1 = new ArrayList<OptionDto>()

        CombinationGroup group2 = new CombinationGroup()
        group2.setTitle(QUESTION_1_TITLE)
        def itemGroup2 = new ArrayList<OptionDto>()

        OptionDto optionDto1 = new OptionDto()
        optionDto1.setContent(OPTION_1_CONTENT)
        optionDto1.setSequence(0)

        OptionDto optionDto2 = new OptionDto()
        optionDto2.setContent(OPTION_2_CONTENT)
        optionDto2.setSequence(0)

        LIST_2_COMBINATION.add(optionDto2)
        optionDto1.setCombination(LIST_2_COMBINATION)
        LIST_1_COMBINATION.add(optionDto1)
        optionDto2.setCombination(LIST_1_COMBINATION)

        itemGroup1.add(optionDto1);
        itemGroup2.add(optionDto2);
        group1.setItems(itemGroup1)
        group2.setItems(itemGroup2)
        group1.setSequence(0)
        group2.setSequence(1)

        combinationQuestion.getCombinationGroup().add(group1)
        combinationQuestion.getCombinationGroup().add(group2)

        question.setQuestionDetails(combinationQuestion)
    }

    def "remove combination question by teacher"() {
        given: "a demo teacher"
        createdUserLogin(USER_2_EMAIL, USER_2_PASSWORD)

        when: "the webservice is invoked"
        response = restClient.delete(
                path: '/questions/' + question.getId(),
                requestContentType: 'application/json'
        )

        then: "check the response status"
        response != null
        response.status == 200
        and: "if it responds with the correct question removed"
        def question = response.data
        question.id != null
        question.title == questionDto.getTitle()
        question.content == questionDto.getContent()
        question.status == Question.Status.REMOVED.name()
    }

    def "cannot remove combination question by student"() {
        given: "a demo student"
        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)

        when: "the webservice is invoked"
        response = restClient.delete(
                path: '/questions/' + question.getId(),
                requestContentType: 'application/json'
        )

        then: 'the request fails'
        def error = thrown(HttpResponseException)
        error.response.status == HttpStatus.SC_BAD_REQUEST
    }

    def cleanup() {
        persistentCourseCleanup()

        userRepository.deleteById(student.getId())
        userRepository.deleteById(teacher.getId())
    }
}
