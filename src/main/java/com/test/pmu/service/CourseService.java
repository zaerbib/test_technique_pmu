package com.test.pmu.service;

import com.test.pmu.entity.Course;
import com.test.pmu.entity.Partant;
import com.test.pmu.exception.PmuException;
import com.test.pmu.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course saveCourse(Course course) throws PmuException {
        if(courseRepository.isCourseExist(course.getJour(), course.getNombreUnique()))
            throw PmuException.builder().message("Course existe déjà pour aujourd'hui").build();

        if(!isListPartantOK(course.getPartants())) {
            if(!listHasNexts(course.getPartants()))
                throw PmuException.builder().message("les partants ne sont pas correctement numérotés").build();
            if(!isFirstNumberOne(course.getPartants()))
                throw PmuException.builder().message("Aucun partant n'a le numéro 1").build();
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
       return partants.size() >= 3 && isFirstNumberOne(partants)
               && listHasNexts(partants);
    }

    private boolean listHasNexts(Set<Partant> partants) {
        List<Partant> lists = new ArrayList<>(partants);
        List<Integer> listTmp = lists.stream().map(item -> item.getNumber()).sorted().collect(Collectors.toList());
        Integer tmp = listTmp.get(listTmp.size() - 1);

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
