package io.chekarev.taskManagementSystem.repositories.specifications;

import io.chekarev.taskManagementSystem.domain.entities.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Спецификация для фильтрации задач. Содержит статический метод, который создает фильтры для
 * поиска задач по различным критериям, таким как статус, приоритет, автор и исполнитель.
 */
public class TaskSpecification {

    /**
     * Создает спецификацию для фильтрации задач по заданным параметрам.
     *
     * @param status Статус задачи (может быть null или пустым).
     * @param priority Приоритет задачи (может быть null или пустым).
     * @param author ID автора задачи в виде строки (может быть null или пустым).
     * @param assignee ID исполнителя задачи в виде строки (может быть null или пустым).
     * @return Спецификация для фильтрации задач с учетом заданных критериев.
     * @throws IllegalArgumentException Если ID автора или исполнителя имеет неверный формат.
     */
    public static Specification<Task> withFilters(String status, String priority, String author, String assignee) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (priority != null && !priority.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            if (author != null && !author.isEmpty()) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("author").get("id"), Long.parseLong(author)));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid author ID format: " + author);
                }
            }

            if (assignee != null && !assignee.isEmpty()) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("assignee").get("id"), Long.parseLong(assignee)));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid assignee ID format: " + assignee);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
