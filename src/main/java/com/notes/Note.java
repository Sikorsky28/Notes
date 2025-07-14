package com.notes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Класс представляет заметку с заголовком,id, текстом и тегами.
 */
public class Note {
  private final int id; // Уникальный ID
  private String title; // Заголовок
  private String text; // Текст заметки
  private final LocalDate creationDate; // Дата создания
  private Set<String> tags; // Набор тегов (уникальные строки)

  /**
   * Конструкутор класса.
   */
  public Note(int id, String title, String text) {

    this.title = Objects.requireNonNullElse(title, "title");
    this.text = Objects.requireNonNullElse(text, "text");
    this.id = id;
    this.creationDate = LocalDate.now();
    this.tags = new HashSet<>();
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public Set<String> getTags() {
    return Collections.unmodifiableSet(tags);
  }

  /**
   * Устанавливает название.
   *
   * @param title название заметки.
   */
  public void setTitle(String title) {
    if (title == null) {
      throw new IllegalArgumentException("Title cannot be null");
    }
    this.title = title;
  }

  /**
   * Устанавливает название.
   *
   * @param text текст заметки.
   */
  public void setText(String text) {
    if (text == null) {
      throw new IllegalArgumentException("Text cannot be null");
    }
    this.text = text;
  }

  /**
   * Добавляет тег во множество tags.
   *
   * @param tag Тег для добавления.
   */
  public void addTag(String tag) {
    if (tag == null || tag.isBlank()) {
      throw new IllegalArgumentException("Tag cannot be null or blank");
    }
    tags.add(tag.toLowerCase());
  }

  /**
   * Удаляет тег из множества tags.
   *
   * @param tag Тег для удаления.
   */
  public boolean removeTag(String tag) {
    if (tag == null || tag.isBlank()) {
      throw new IllegalArgumentException("Tag cannot be null or blank");
    }
    return tags.remove(tag.toLowerCase());
  }


  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Note note = (Note) o;
    return id == note.id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}