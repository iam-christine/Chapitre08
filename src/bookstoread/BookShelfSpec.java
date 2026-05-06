package bookstoread;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests de la bibliothèque BookShelf")
public class BookShelfSpec {
    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;

    @BeforeEach
    void init() {
        shelf = new BookShelf();
        effectiveJava = new Book("Effective Java", "Joshua Bloch", LocalDate.of(2008, Month.MAY, 8));
        codeComplete = new Book("Code Complete", "Steve McConnel", LocalDate.of(2004, Month.JUNE, 9));
        mythicalManMonth = new Book("The Mythical Man-Month", "Frederick Phillips Brooks", LocalDate.of(1975, Month.JANUARY, 1));
        cleanCode = new Book("Clean Code", "Robert C. Martin", LocalDate.of(2008, Month.AUGUST, 1));
    }

    @Nested
    @DisplayName("Tests sur le contenu de base")
    class BasicTests {
        @Test
        @DisplayName("est vide quand aucun livre n'est ajouté")
        void shelfEmptyWhenNoBookAdded() {
            assertTrue(shelf.books().isEmpty(), () -> "La bibliothèque devrait être vide.");
        }

        @Test
        @DisplayName("contient deux livres quand deux livres sont ajoutés")
        void bookshelfContainsTwoBooksWhenTwoBooksAdded() {
            shelf.add(effectiveJava, codeComplete);
            assertEquals(2, shelf.books().size());
        }

        @Test
        @DisplayName("reste immuable pour le client")
        void booksReturnedFromBookShelfIsImmutableForClient() {
            List<Book> books = shelf.books();
            assertThrows(UnsupportedOperationException.class, () -> books.add(mythicalManMonth));
        }
    }

    @Nested
    @DisplayName("Tests sur le tri (Arrange)")
    class ArrangementTests {
        @Test
        @DisplayName("est triée par titre par défaut")
        void bookshelfArrangedByBookTitle() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange();
            assertEquals(asList(codeComplete, effectiveJava, mythicalManMonth), books);
        }

        @Test
        @DisplayName("est triée selon un critère utilisateur (ordre inversé)")
        void bookshelfArrangedByUserProvidedCriteria() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange(Comparator.<Book>naturalOrder().reversed());
            assertEquals(asList(mythicalManMonth, effectiveJava, codeComplete), books);
        }
    }

    @Nested
    @DisplayName("Tests sur le regroupement (Group)")
    class GroupingTests {
        @Test
        @DisplayName("les livres sont groupés par année de publication")
        void groupBooksInsideBookShelfByPublicationYear() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
            Map<Year, List<Book>> booksByPublicationYear = shelf.groupByPublicationYear();
            assertThat(booksByPublicationYear).containsKey(Year.of(2008)).containsEntry(Year.of(2008), asList(effectiveJava, cleanCode));
            assertThat(booksByPublicationYear).containsKey(Year.of(2004)).containsEntry(Year.of(2004), Collections.singletonList(codeComplete));
        }
    }
}