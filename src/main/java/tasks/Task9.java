package tasks;

import common.Person;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  //    toList() вернёт emptyList если будет передан пустой набор элементов
  //    Избавляемся от первого элемента через метод sublist
  public List<String> getNames(List<Person> persons) {
    return persons.subList(1, persons.size()).stream().map(Person::firstName).toList();
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    return getNames(persons).stream().distinct().collect(Collectors.toSet());
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  //    Логика реализуется в одну строку через стрим и его метод joining
  //    Не совсем понял, является ли ошибкой то, что secondName используется два раза, но на всякий случай оставил так
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName(), person.secondName()).collect(Collectors.joining(" ", "", ""));
  }

  // словарь id персоны -> ее имя
  //    Собираем мапу через toMap, чтобы избежать исключения при одинаковых ключах, добавил туда merge-функцию
  //    Как я понял, если id повторяются, то мы просто не принимаем новое значение, merge-функция в таком случае оставит уже добавленное имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return new HashMap<>(persons.stream().collect(Collectors.toMap(Person::id, this::convertPersonToString, (containedName, newName) -> containedName)));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  //  Вместо вложенных циклов используем метод стрима anyMatch
  //    - вернёт true и завершит работу как только найдётся хоть один совпадающий элемент
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    return persons1.stream().anyMatch(persons2::contains);
  }

  // Посчитать число четных чисел
  //  Можно избавиться от переменной использовав метод count после filter
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
/*
  Как я понял, у Integer hashCode всегда будет возвращать их числовое значение.
  При вставке в HashSet индекс будет рассчитываться по отдельной формуле index = (n - 1) & hash, то есть положение чисел в нём
  не будет зависеть от индексов в исходном List, даже если они перемешаны. А в нашем конкретном случае чисел в мапе будет меньше
  чем выделено бакетов, поэтому каждое число будет лежать в отдельном бакете. Поэтому при выводе элементов будет по очереди выведено
  содержимое бакетов.
 */