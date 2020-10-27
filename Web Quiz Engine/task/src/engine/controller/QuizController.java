package engine.controller;

import engine.dto.ResponseDTO;
import engine.entity.CompletionEntity;
import engine.entity.Quiz;
import engine.entity.User;
import engine.repository.CompletionRepository;
import engine.service.QuizService;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RestController()
public class QuizController {
    private final QuizService service;
    private final CompletionRepository completionRepository;


    @Autowired
    public QuizController(QuizService service, CompletionRepository completionRepository) {
        this.service = service;
        this.completionRepository = completionRepository;
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuizById(@PathVariable int id) {
        return service.getQuizById(id);
    }

    @GetMapping(path = "/api/quizzes")
    public Page<Quiz> getQuizzes(@RequestParam(required = false, defaultValue = "0", name = "page") int page) {
        int pageSize = 10;
        Pageable paging = PageRequest.of(page, pageSize, Sort.by("id").descending());

        return service.getAllQuizzesFromStorage(paging);
    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    public Quiz addQuiz(@Valid @NotNull @RequestBody Quiz quiz) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        quiz.setOwnerEmail(user.getEmail());

        return service.addQuizToStorage(quiz);
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public ResponseDTO solveQuiz(@PathVariable int id, @RequestBody Quiz answer) {
        if (service.solveQuizById(id, answer.getAnswer())) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            completionRepository.save(new CompletionEntity(user.getEmail(), id, new Date()));

            return new ResponseDTO(true);
        } else {
            return new ResponseDTO(false);
        }
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable int id) {
        Quiz quiz = service.getQuizById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!quiz.getOwnerEmail().equals(user.getEmail())) {
            return new ResponseEntity<>("You cannot delete another user's quiz", HttpStatus.FORBIDDEN);
        }

        service.deleteById(id);

        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/quizzes/completed")
    public Page<CompletionEntity> getCompleted(@RequestParam(required = false, defaultValue = "0", name = "page") int page) {
        int pageSize = 10;
        Pageable paging = PageRequest.of(page, pageSize, Sort.by("completedAt").descending());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return completionRepository.findAllByUserEmail(user.getEmail(), paging);
    }
}
