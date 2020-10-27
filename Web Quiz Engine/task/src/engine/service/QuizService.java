package engine.service;

import engine.dto.ResponseDTO;
import engine.entity.Quiz;
import engine.exception.InvalidAnswerException;
import engine.exception.QuizNotFoundException;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz addQuizToStorage(Quiz quiz) {
        checkAnswerOptions(quiz);
        quizRepository.save(quiz).getId();

        return quiz;
    }

    public Page<Quiz> getAllQuizzesFromStorage(Pageable paging) {
        return quizRepository.findAll(paging);
    }

    public Quiz getQuizById(int id) {
        Quiz quiz = findById(id);

        return quiz;
    }

    private Quiz findById(int id) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);

        if (optionalQuiz.isEmpty()) {
            throw new QuizNotFoundException();
        } else {
            return optionalQuiz.get();
        }
    }

    public boolean solveQuizById(int id, Set<Integer> answer) {
        Quiz quiz = findById(id);

        return Arrays.equals(quiz.getAnswer().toArray(), answer.toArray()) ? true : false;
    }

    private void checkAnswerOptions(Quiz quiz) {
        if(quiz.getOptions() == null) {
            throw new InvalidAnswerException();
        } else {
            int numberOfOptionsInQuiz = quiz.getOptions().size();

            for (Integer eachAnswer : quiz.getAnswer()) {
                if (eachAnswer < 0 || eachAnswer > numberOfOptionsInQuiz) {
                    throw new InvalidAnswerException();
                }
            }
        }
    }

    public void deleteById(int id) {
        quizRepository.deleteById(id);
    }
}
