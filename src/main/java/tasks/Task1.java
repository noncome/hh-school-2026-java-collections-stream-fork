package tasks;

import common.Person;
import common.PersonService;

import java.util.*;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    Map<Integer, List<Person>> personsById = persons.stream().collect(Collectors.groupingBy(Person::id));
    return personIds.stream().map(personId -> personsById.get(personId).get(0)).toList();
  }
}
//  Переписал решение, теперь асимптотика должна быть O(n + m)
//  Процессы выполняются последовательно, друг от друга не зависят.
//  Мапа соберётся за O(n), вывод произойдёт за O(m)
