package com.test.pmu.service;

import com.test.pmu.entity.Course;
import com.test.pmu.entity.Partant;
import com.test.pmu.exception.PmuException;
import com.test.pmu.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course saveCourse(Course course) throws PmuException {
        if(!isListPartantOK(course.getPartants())) {
            if(!listHasNexts(course.getPartants()))
                throw new PmuException("les partants ne sont pas correctement numérotés");
            if(!isFirstNumberOne(course.getPartants()))
                throw new PmuException("Aucun partant n'a le numéro 1");
        }

        return courseRepository.save(course);
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
       return partants.size() >= 3 && isFirstNumberOne(partants) && listHasNexts(partants);
    }

    private boolean listHasNexts(Set<Partant> partants) {
        List<Partant> lists = new ArrayList<>(partants);
        lists.sort(Comparator.comparing(Partant::getNumber));
        Partant lastPartant = lists.get(lists.size() - 1);

        for(Partant partant: lists) {
            if(partant.equals(lastPartant) && !lists.contains(partant.getNumber() + 1))
                return false;
        }

        return true;
    }

    private boolean isFirstNumberOne(Set<Partant> partants) {
        List<Partant> tmp = new ArrayList<>(partants);
        return tmp.get(0).getNumber() == 1;
    }
}
