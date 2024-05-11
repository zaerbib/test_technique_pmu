package com.test.pmu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.pmu.entity.Course;
import com.test.pmu.entity.Partant;
import com.test.pmu.event.EventCourse;
import com.test.pmu.exception.PmuException;
import com.test.pmu.repository.CourseRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final KafkaTemplate<String, EventCourse> kafkaTemplate;

    public CourseService(CourseRepository courseRepository, KafkaTemplate<String, EventCourse> kafkaTemplate) {
        this.courseRepository = courseRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${microservice.kafka.topics.course-event-store}")
    private String topicName;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * save course service
     * @param course
     * @return
     * @throws PmuException
     */
    public Course saveCourse(Course course) throws PmuException {
        if(courseRepository.isCourseExist(course.getJour(), course.getNombreUnique())) {
            PmuException exception = PmuException.builder().message("Course existe déjà pour aujourd'hui").build();
            EventCourse eventCourse = EventCourse.builder().id(UUID.randomUUID().toString())
                    .date(LocalDate.now()).course(course).exception(exception).build();
            sendEventcourse(eventCourse);
            throw exception;
        }

        if(!isListPartantOK(course.getPartants())) {
            if(!listHasNexts(course.getPartants())) {
                PmuException exception = PmuException.builder().message("les partants ne sont pas correctement numérotés").build();
                EventCourse eventCourse = EventCourse.builder().id(UUID.randomUUID().toString())
                        .date(LocalDate.now()).course(course).exception(exception).build();
                sendEventcourse(eventCourse);
                throw exception;
            }
            if(!isFirstNumberOne(course.getPartants())) {
                PmuException exception = PmuException.builder().message("Aucun partant n'a le numéro 1").build();
                EventCourse eventCourse = EventCourse.builder().id(UUID.randomUUID().toString())
                        .date(LocalDate.now()).course(course).exception(exception).build();
                sendEventcourse(eventCourse);
                throw exception;
            }
        }
        Course result = courseRepository.save(course);
        EventCourse eventCourse = EventCourse.builder().id(UUID.randomUUID().toString())
                                    .date(LocalDate.now()).course(course).course(result).build();
        sendEventcourse(eventCourse);
        return result;
    }

    private void sendEventcourse(EventCourse eventCourse) {
        ListenableFuture<SendResult<String, EventCourse>> future = kafkaTemplate.send(topicName, eventCourse);

        future.addCallback(new ListenableFutureCallback<SendResult<String, EventCourse>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message due to {}", ex.getMessage());
            }

            @SneakyThrows
            @Override
            public void onSuccess(SendResult<String, EventCourse> result) {
                log.info("Sent message=[ {} ], with offset=[{}] ",
                        objectMapper.writeValueAsString(eventCourse), result.getProducerRecord());
            }
        });
    }

    /**
     * la liste de partant doit respecter trois contraintes :
     * * avoir une taille minum de 3
     * * le premier partant doit avoir le numéro 1;
     * * les éléments sont incrementé de 1 à partir de 1
     * @param partants
     * @return
     */
    private boolean isListPartantOK(Set<Partant> partants) {
       return partants.size() >= 3 && isFirstNumberOne(partants)
               && listHasNexts(partants);
    }

    private boolean listHasNexts(Set<Partant> partants) {
        List<Partant> lists = new ArrayList<>(partants);
        List<Integer> listTmp = lists.stream().map(Partant::getNumber).sorted().toList();

        if(listTmp.isEmpty()){
            return false;
        }

        Integer tmp = listTmp.get(lists.size() - 1);
        for(Integer item: listTmp) {
            if(item == tmp)
                return true;
            if(!listTmp.contains(item + 1))
                return false;
        }

        return true;
    }

    private boolean isFirstNumberOne(Set<Partant> partants) {
        List<Partant> tmp = new ArrayList<>(partants);
        tmp.sort(Comparator.comparing(Partant::getNumber));
        return tmp.get(0).getNumber() == 1;
    }
}
