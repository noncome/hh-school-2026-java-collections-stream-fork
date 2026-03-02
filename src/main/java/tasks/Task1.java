package tasks;

import common.Person;
import common.PersonService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
      return persons.stream()
        .flatMap(Person -> Collections.nCopies(Collections.frequency(personIds, Person.id()), Person).stream())
        .sorted(Comparator.comparing(Person -> personIds.indexOf(Person.id())))
        .toList();
  }
}
//  Асимптотика моего решения будет O(n^2 log n)
//  flatMap применяет функцию к каждому элементу за O(n)
//  Нугуглил, что sorted сортирует за O(n log n)
//  Не совсем понял как на общую сложность повлияет frequency, так как он зависит от другой коллекции