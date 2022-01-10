package pulson.hibernate.hibernate_basic_annotations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

@NamedQueries({
        @NamedQuery(name = "customer.getAll", query = "select c from customers c"),
        @NamedQuery(name = "customer.getOldestCustomer", query = "SELECT c FROM customers c WHERE c.age = " +
                "(SELECT MAX(x.age) FROM customers x)"),
        @NamedQuery(name = "customer.getCustomerWithBiggestOrder", query = "SELECT c FROM customers c JOIN c.orders o WHERE o.money = " +
                "(SELECT MAX(o.money) FROM o)"),
        @NamedQuery(name = "customer.getCustomersIDsWithSumOfOrders", query = "SELECT o.customer.id, SUM(o.money) as total FROM customers c JOIN c.orders o " +
                "GROUP BY o.customer.id ORDER BY total desc")
        })


@NamedNativeQueries({
        @NamedNativeQuery(name = "customer.getCustomersNameAndAge",
                query = "select c.last_name as name, c.age as age from customers c",
                resultSetMapping = "nameAgeMapping")
        })
@SqlResultSetMapping(name = "nameAgeMapping", classes = {
        @ConstructorResult(targetClass = NameAgeMapping.class, columns = {
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "age", type = Integer.class)
        })
})
        //Mapper pozwala na zmapowanie kolumn, które są wynikami zapytania SQL do obiektu klasy
        //Nazwy pól w klasie nie muszą być takie same jak nazwy kolumn, liczy się ich kolejność i typ
        //Kolejność typów @ColumnResult musi być taka jak kolejność pól w konstruktorze target klasy
        //Zmieniona kolejność poskutkuje błędem - "Could not locate appropriate constructor on class : NameAgeMapping"




@Entity(name = "customers")
//@Entity - oznacza klasę jako persistent entity - będzie ona istnieć w bazie danych
//wymaga primary key @Id oraz bezargumentowego konstruktora
//name jest używane podczas pisania Query w JPQL i HQL


//Wszystkie nazwy tabel i kolumn generowane są przy użyciu Hibernate NamingStrategy
//Stąd podana nazwa BasicAnnotations zostanie zmieniona na basic_annotations
//idąc dalej nazwa kolumny firstName zostanie zamieniona na first_name
//a nazwa tabeli to customer. NamingStrategy zmienia słowa na lowercase i oddziela je '_'
@Table(name = "customers",
//name - nazwa widoczna w bazie danych
schema = "BasicAnnotations",
//schema - oznacza miejsce, w którym stworzy się tabela (defaultowo jest to public)
//Schema to osobna przestrzeń nazw w obrębie tej samej bazy danych. Przypomina folder na dysku
    uniqueConstraints = {@UniqueConstraint(columnNames = "phone_number")},
//uniqueConstraints - jest równoznaczne z użyciem @Column(unique = true), PostgreSQL stworzy automatycznie index na tej kolumnie
    indexes = {
        @Index(name = "first_name_index", columnList = "first_name", unique = false)
            //Index to struktura danych, która zwiększa prędkość działania zapytań SQL wyciągających dane w zamian za dodatkowe zapisy i pamięć
    }
)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {

    @Id//Oznacza pole jako klucz główny encji
    @SequenceGenerator(name = "customer_id_generator", sequenceName = "customer_sequence", allocationSize = 50, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_generator")
    //Pozwala na ustalenie strategii generowania kolejnych kluczy głównych
    //SEQUENCE - (najlepsza, zaawansowana dzięki kustomizacji) nowa wartość id jest generowana przez database sequence (odpala się create sequence hibernate_sequence). Każdy insert wywoła select na tej sekwencji i dzięki temu uzyska następną wartość. Nie ma to wpływu na wydajność
    //sequenceName to nazwa sekwencji, która stworzy się w bazie danych, initialValue to początkowa wartość klucza głównego (indexy zaczną się od 10). MySQL nie wspiera SEQUENCE
    //AUTO - persistence provider (Hibernate) wybierze strategię sam (najczęściej defaultowe SEQUENCE o nazwie hibernate_sequence)
    //IDENTITY - łatwa w życiu, ale słaba pod względem wydajności. Opiera się na auto-inkrementującej kolumnie i pozwala bazie generować nową wartość przy każdym insercie
    //TABLE - tworzy tabelę o nazwie hibernate_sequences, w której są 2 kolumny - sequence_name i next_val, która przechowuje liczbową wartość (przy kolejnym insercie jest ona pobierana z tabeli i używana jako klucz główny)
    @Column(name = "id", unique = true, updatable = false)
    private Long id;



    @Column(
            name = "some_number",
            unique = false,//Podobnie jak @UniqueConstraint pozwala na oznaczenie kolumny jako przechowującą unikalne wartości
            nullable = true,//Opisane niżej przy polu lastName
            insertable = true,//Ustala, czy kolumna jest brana pod uwagę w zapytaniach SQL INSERT. Ustawienie false sprawi, że NIE uda nam się wstawć żadnych danych do bazy INSERTEM.
            updatable = false,//Ustala, czy kolumna jest brana pod uwagę w zapytaniach SQL UPDATE. Ustawienie false pozwoli jednorazowo wprowadzić dane, ale nie da się ich później zmienić. Zapytanie customer.setSomeNumber(new BigDecimal("999.999") po prostu nie zostanie wywołane. Nie wyskoczy nawet żaden błąd.
            table = "customers", //Nazwa tabeli, w której ma znaleźć się kolumna, defaultowo to nasza @Table(name = "customers")
            length = 3,//Ogranicza długość ciągu znaków. Jeśli wstawimy 5-literowe imię wyrzuci - ERROR: value too long for type character varying(3)
            columnDefinition = "numeric(10,3) default 55.555",//Pozwala ustawić customowe wartości, które zostaną wykorzystane podczas generowania DDL(Data Definition Language) dla kolumny. Default value do poprawnego działania wymaga ustawienia insertable = false!
            //create table basic_annotations.customers (
            //.... inne kolumny
            //some_number numeric(10,3) default 55.555,
            //To, co wpiszemy w columnDefinition, znajdzie się w DDL podczas tworzenia tabeli po nazwie kolumny!
            precision = 10,//Ustala ile cyfr łącznie(przed i po przecinku) może mieć dana liczba. Działa tylko z kolumną typu numeric
            scale = 3//Ustala ilość cyfr po przecinku
            //Konstrukcja numeric(10,3) to to samo co columnDefinition = numeric(precision,scale).
    )
    //@ColumnDefault(value = "55.555")
    private BigDecimal someNumber;



    //@Transient
    //Pole nie zostanie zapisane w bazie danych, ale nadal będzie serializowane
    //Dodanie słowa kluczowego transient da ten sam efekt, ale pole nie będzie serializowane
    @Column(name = "first_name")
    private String firstName;



    @NotNull
    //Uniemożliwia dodanie wartości null. Dzięki pre-persist sprwadzenie, czy dodawana wartość jest nullem odbywa się PRZED wykonaniem zapytania SQL, które w przypadki obecności nulla się nie wykona
    //Przy próbie wprowadzenia wartości null dostajemy javax.validation.ConstraintViolationException: Validation failed for classes [learning.pulson.inheritance.Customer]/// interpolatedMessage='must not be null', propertyPath=lastName///  messageTemplate='{javax.validation.constraints.NotNull.message}
    //Z kolei '@Column nullable = false' przerzuca odpowiedzialność na bazę danych. Dopiero ona weryfikuje to, co próbujemy dodać. Dlatego zawsze przy próbie insertu uruchamiane jest zapytanie SQL a w przypadku wystąpienia wartości null dostaniemy - org.postgresql.util.PSQLException: ERROR: null value in column "last_name" of relation "customers" violates not-null constraint
    @Column(name = "last_name")
    private String lastName;



    @Column(name = "phone_number", unique = false)
    private String phoneNumber;



    @Temporal(TemporalType.DATE)
    //Pozwala ustalić precyzję, z jaką data zostanie zapisana w bazie. java.sql.Date/Time/Timestamp nie wymagają stosowania tej adnotacji w przeciwieństwie do java.util.Date/Calendar
    //java.util.Date format = yyyy-MM-dd///format.parse("1998-07-14") - wpisanie tej daty do bazy bez @Temporal poskutkuje zapisaniem w bazie kolumny typu timestamp z wartością 1998-07-14 00:00:00.000000
    //TemporalType.DATE - zapisze naszą datę w kolumnie o typie date z wartością 1998-07-14, bez godzin/minut/sekund
    //TemporalType.TIME - zapisze kolumnę o typie time z wartością 00:00:00
    //TemporalType.TIMESTAMP - zapisze kolumnę o typie timestamp z wartością 1998-07-14 00:00:00.000000
    //Najlepszym rozwiązaniem jest używanie klas z java.time - LocalDate/LocalTime/LocalDateTime/ZonedDateTime
    @Column(name = "birth_date")
    private Date birthDate;



    //@Enumerated(EnumType.STRING)
    //Enumerated służy do zapisania pola typu Enum.
    //EnumType.STRING - zapisze w bazie Rating.name(), czyli String nazwy enuma. W razie update nazwy enuma - istniejące rekordy nadal będą wskazywać na starą nazwę. Do tego ta forma zapisy zajmuje sporo miejsca
    //EnumType.ORDINAL - zapisze w bazie Rating.ordinal(), czyli integer odpowiadający indexowi użytego przez nas enuma. Indexy liczone są od 0 jak w liście. Jeśli w klasie enum mamy 5 enumów, a użyjemy czwartego od góry - w bazie będzie figurował jako "3".
    //Problem z ORDINAL polega na tym, że jeśli dodamy(lub usuniemy) nowy enum na początku lub w środku - istniejące rekordy się nadpiszą i nie będą wskazywać już na poprawne wartości

    @Convert(converter = RatingConverter.class)
    //Converter pozwala na konwersję pola do innej postaci, którą chcemy przechować w bazie danych
    //public class RatingConverter implements AttributeConverter<Rating, String>
    //Rating to obiekt, który przyjmuje do konwersji a String to obiekt, który jest efektem konwersji i zostaje zapisany w bazie danych
    private Rating rating;




    @Basic(optional = true, fetch = FetchType.EAGER)
    //Służy do oznaczania prymitywów i ich wrapper class oraz innych klas implementujących Serializable. Mówi Hibernate, że powinien użyć standardowego mapowania. Jest to opcjonalna adnotacja
    //optional (default true) - oznacza czy pole może przyjmować wartość null. false działa tak samo jak @Column nullable = true. Po ustawieniu false i próbie dodania nulla przy uruchomieniu programu wyskoczy błąd - not-null property references a null or transient value
    //FetchType jest defaultowo ustawiony na EAGER
    private Integer age;




    @Embedded
    //Zagnieżdża klasę (w tym wypadku Address) wewnątrz naszej (Customer). Niejako przenosi pola z Address do Customer.
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "address_country")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "building", column = @Column(name = "address_building"))
            //AttributeOverride pozwala na mapowanie pól klasy zagnieżdżonej do kolumn, które pojawią się w tabeli customers i nadanie im własnych nazw
    })
    private Address address;




    @Lob
    //Informuje że pole powinno zostać zapisane jako Large Object Type(LOB). Może być to BLOB lub CLOB
    //CLOB = java.sql.Clob, Character[], char[], java.lang.String - Character Large Object stosowany jest z dużymi ilościami danych tekstowych
    //BLOB = java.sql.Blob, Byte[], byte[] i typy serializable - Binary Large Object używany jest do przechowywania plików binarnych jak obrazy, wideo czy audio
    //Tak może wyglądać zapisanie obrazu w bazie
    //InputStream inputStream = Initializer.class.getClassLoader().getResourceAsStream("photo.jpg"); //folder resources/photo.jpg
    //byte[] photoByte = IOUtils.toByteArray(inputStream);
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "photo", columnDefinition = "bytea")
    @ToString.Exclude
    private byte[] photo;


    //defaultowe adnotacje dodawane
    //jeśli pole jest typu prymitywnego - ma adnotację @Basic
    //jeśli pole jest obiektem Serializable - ma adnotację @Basic i przechowuje obiekt w jego serializowanej wersji
    //jeśli pole jest obiektem oznaczonym @Embeddable - ma adnotację @Embedded
    //jeśli pole jest typu java.sql.Clob lub java.sql.Blob - ma adnotację @Lob z odpowiednim LobType


    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)//CascadeTypes mają związek z tym co dzieje się między obiektami w relacji parent(Customer) - child(Order). Nasze ustawienia są kaskadowane na child entities (Orders przypisane Customerowi)
    //orphanRemoval = true i CascadeType.REMOVE działają tak samo, gdy usuwamy referenced/parent side czyli Customera. Wtedy mogą one być stosowane zamiennie i oznaczają to samo - obiekty Order powiązane z Customerem zostaną również usunięte z bazy danych
    //Różnica pojawia się, gdy odłączamy relację np. customer.getOrders().set(0, null) lub .set(0, new Order()). Mając ustawione CascadeZType.REMOVE nie dzieje się nic. Z orphanRemoval = true po odłączeniu obiektu usuwa się też powiązany obiekt. Ustawienie któregoś Orderu Customera na null, spowoduje całkowite usunięcie obiektu Order. Przypisanie do starego Order nowego obiektu Order spowoduje usunięcie starego i dodanie nowego z następnym id
    //bez orphanRemoval lub Cascade REMOVE (lub ręcznego usunięcia Orders) przy próbie usunięcia Customera, jego pozostałe w bazie zamówienia będą powodować błędy - update or delete on table "customers" violates foreign key constraint "fkpxtb8awmi0dk6smoh2vp1litg" on table "orders"///Detail: Key (id)=(11) is still referenced from table "orders"
    @OrderBy("money ASC")//Przy wyciąganiu Customera, jego lista orders będzie posortowana rosnąco po "money". Porządek ten NIE jest zachowywany w bazie danych
    @OrderColumn(name = "orders_index")//Dodaje do tabeli orders kolumnę przechowującą indexy ArrayListy, pod którymi są poszczególne zamówienia. I customer z 4 zamówieniami{0,1,2,3,4} / II customer z 1 zamówieniem{0} / III customer z 2 zamówieniami{0,1}. Razem ze zmianami w Listach zamówień customerów - zmiany w id ArrayListy są updatowane w bazie danych.
    //@OrderColumn może być używane przy @OneToMany, @ManyToMany i @ElementCollection
    //OrderBy i @OrderColumn się wykluczają. Jeśli włączyliśmy sortowanie po indexie ArrayListy, to OrderBy money nie zadziała przy zwracaniu obiektów. Będą posortowane po indexie Listy
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }
    public void removeOrder(Order order) {
        order.setCustomer(null);
        orders.remove(order);
    }




    //Field-based access vs property-based access! https://thorben-janssen.com/access-strategies-in-jpa-and-hibernate/
    //Access strategy decyduje o tym jak implementacja JPA np. Hibernate czy EclipseLink będzie uzyskiwał dostęp do atrybutów encji (pól klasy).
    //Field based oznacza umieszczanie adnotacji JPA nad polami klasy. Hibernate używa wtedy refleksji do odczytu/zapisu atrybutów encji.
    //Property based oznacza umieszczanie adnotacji JPA nad getterami. Hibernate do odczytu/zapisu wywołuje gettery i settery pól klasy.
    //Access type dla klasy definiuje pozycja adnotacji @Id lub @EmbeddedId. Jeśli te adnotacje są nad polem - niejawnie ustawiamy field-based access type.
    //Jeśli damy adnotację @Id nad getterem pola id - Hibernate użyje property-based access type do odczytu i zapisu atrybutów tej encji.
    //Jeden rabin powie field a inny rabin powie property. Pan Janssen radzi, aby stosować field based access.
    //1. Lepsza czytelność kodu. Wszystkie adnotacje mapujące znajdują się na górze w jednym miejscu. Nie są rozrzucone optycznie w kodzie między getterami i setterami.
    //2. Można pominąć settery, które nie powinny być wykorzystywane przez nasz kod np. setter do Id. Możemy też dostarczyć utility methods do dodawania i usuwania elementów przy relacjach ...ToMany aby uniemożliwić bezpośredni dostęp do Listy czy Setu. Jest to najlepsza praktyka np. przy bidirectional ManyToMany
    //3. Umożliwia bardziej elastyczną implementację getterów i setterów. Dzięki temu, że nasz persistence provider nie wywołuje getterów i setterów - nie muszą spełniać one żadnych wymagań. Możemy je zaimplementować tak jak chcemy. Umożliwia to zaimplementowanie w nich dodatkowej logiki biznesowej.
    //4. Nie trzeba oznaczać utility methods jako @Transient.
    //5. Pozwala uniknąć bugów przy pracy z proxy używanymi przy lazy-fetching ...ToOne associations.

    //W obrębie klasy najlepiej nie mieszać typów dostępu - należy trzymać się jednej ustalonej strategii.
    //Można to jednak osiągnąć stosując @Access(AccessType.PROPERTY/FIELD).
    //W tym przypadku @Id jest nad polem więc mamy ustawiony field access, ale używając @Access(AccessType.PROPERTY) nad getterem nicknames - możemy zmienić access dla tego jednego pola z defaultowaego field na property. Działa to też w drugą stronę.
    private Set<String> nicknames = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    //Powstanie tabela nicknames z kolumnami nickname i user_id mapująca te obie wartości
    //@ElementCollection jest niezbędna - oznacza ona kolekcję typów prostych lub obiektów Embeddable. Defaultowo kolekcja jest LAZY loaded tak jak w relacjach ...ToMany.
    //@CollectionTable jest opcjonalna - bez ustawienia nazwy tabeli na nicknames defaultowa to customers_nicknames (połączenie table_name i field_name)
    @CollectionTable(name = "nicknames", joinColumns = @JoinColumn(name = "customer_id"), schema = "basic_annotations")
    @Column(name = "nickname")
    @Access(AccessType.PROPERTY)
    public Set<String> getNicknames() {
        return this.nicknames;
    }
    public void setNicknames(Set<String> nicknames) {
        this.nicknames = nicknames;
    }




    @Formula(value = "100 - date_part('year', age(birth_date))")
    //Formula pozwala Hibernate na wykonywanie obliczeń w czasie wyciągania encji z bazy danych (dołącza formułę do zapytania SELECT). Powstaje wartość read-only, która nie jest zapisywana do bazy danych
    private Integer howManyYearsTo100YearsOld;



    private String fullName;
    @PostPersist
    //Przy pracy z JPA możemy wykorzystywać cykl życia encji. Służą do tego adnotacje oznaczające różne eventy podczas entity lifecycle
    //@PrePersist - before persist is called for a new entity
    //@PostPersist - after persist is called for a new entity
    //@PreRemove - before an entity is removed
    //@PostRemove - after an entity has been deleted
    //@PreUpdate - before the update operation
    //@PostUpdate - after an entity is updated
    //@PostLoad - after an entity has been loaded
    //metody oznaczane tymi adnotacjami muszą mieć typ zwrotny void
    //Tutaj po zapisaniu Customera do bazy danych inicjalizujemy jego pole fullName.
    //Innym use case mogłaby być klasa Order i zapisywanie creation_time dzięki @PostPersist
    //Kolejny przykład to pole last_update_time i obsługiwanie je dzięki @PostUpdate
    public void setFullNameAfterCustomerIsCreated() {
        this.fullName = firstName + " " + lastName;
    }




    public Customer(String firstName, String lastName, String phoneNumber, Date birthDate, Integer age, Rating rating, byte[] photo, Address address, List<Order> orders, Set<String> nicknames) {
        this.fullName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.age = age;
        this.rating = rating;
        this.photo = photo;
        this.address = address;
        this.orders = orders;
        this.nicknames = nicknames;
    }

    public Customer(BigDecimal someNumber, String firstName, String lastName, String phoneNumber, Date birthDate, Integer age, Rating rating, byte[] photo, Address address, List<Order> orders, Set<String> nicknames) {
        this(firstName, lastName, phoneNumber, birthDate, age, rating, photo, address, orders, nicknames);
        this.someNumber = someNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(someNumber, customer.someNumber) && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(phoneNumber, customer.phoneNumber) && Objects.equals(age, customer.age) && Objects.equals(birthDate, customer.birthDate) && rating == customer.rating && Arrays.equals(photo, customer.photo) && Objects.equals(address, customer.address) && Objects.equals(nicknames, customer.nicknames) && Objects.equals(howManyYearsTo100YearsOld, customer.howManyYearsTo100YearsOld) && Objects.equals(fullName, customer.fullName);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, someNumber, firstName, lastName, phoneNumber, age, birthDate, rating, address, nicknames, howManyYearsTo100YearsOld, fullName);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
