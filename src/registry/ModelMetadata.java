package registry;

import java.time.LocalDate;

//Records : Advanced feature of Java
// This one line replaces a whole class!
// It automatically creates a constructor, accessors (getters), toString, equals, and hashCode.
public record ModelMetadata(String creatorName, LocalDate dateCreated) {
}