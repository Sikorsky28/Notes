package com.notes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Класс представляет методы для управлением заметкой.
 */
public class NoteService {

  private final Map<Integer, Note> notes = new HashMap<>();

  private final AtomicInteger nextId = new AtomicInteger(1);

  /**
   * Пустой конструктор.
   */
  public NoteService() {
    // без параметров
  }

  // --- МЕТОДЫ ДЛЯ РЕАЛИЗАЦИИ ЧЕРЕЗ TDD --- //

  /**
   * Добавляет новую заметку.
   *
   * @param title Заголовок.
   * @param text  Текст.
   * @param tags  Набор тегов (может быть null).
   * @return Созданная заметка с присвоенным ID.
   */
  public Note addNote(String title, String text, Set<String> tags) {
    int id = nextId.getAndIncrement();
    Note note = new Note(id, title, text);
    if (tags == null) {
      tags = Collections.emptySet();
    }
    for (String tag : tags) {
      if (tag == null || tag.isBlank()) {
        throw new IllegalArgumentException("Tag cannot be null or blank");
      }
      note.addTag(tag);
    }
    notes.put(id, note);
    return note;
  }

  /**
   * Получает заметку по ID.
   *
   * @param id ID заметки.
   * @return Optional с заметкой, если найдена, иначе Optional.empty().
   */
  public Optional<Note> getNoteById(int id) {
    return Optional.ofNullable(notes.get(id));
  }

  /**
   * Получает все заметки.
   *
   * @return Неизменяемый список всех заметок.
   */
  public List<Note> getAllNotes() {
    return List.copyOf(notes.values());
  }

  /**
   * Обновляет заголовок и текст существующей заметки.
   *
   * @param id       ID заметки.
   * @param newTitle Новый заголовок.
   * @param newText  Новый текст.
   * @return true, если заметка найдена и обновлена, иначе false.
   */
  public boolean updateNoteText(int id, String newTitle, String newText) {
    Note note = notes.get(id);
    if (note == null) {
      return false;
    }
    if (newTitle == null || newTitle.isBlank() || newText == null || newText.isBlank()) {
      throw new IllegalArgumentException("Title and text cannot be null or blank");
    }
    note.setTitle(newTitle);
    note.setText(newText);
    return true;
  }

  /**
   * Добавляет тег к существующей заметке.
   *
   * @param id  ID заметки.
   * @param tag Тег для добавления.
   * @return true, если заметка найдена и тег добавлен, иначе false.
   */
  public boolean addTagToNote(int id, String tag) {
    Note note = notes.get(id);
    if (note == null || note.getTags().contains(tag)) {
      return false;
    }
    if (tag == null || tag.isBlank()) {
      throw new IllegalArgumentException("Tag cannot be null or blank");
    }
    note.addTag(tag);
    return true;
  }

  /**
   * Удаляет тег у существующей заметки.
   *
   * @param id  ID заметки.
   * @param tag Тег для удаления.
   * @return true, если заметка найдена и тег удален, иначе false.
   */
  public boolean removeTagFromNote(int id, String tag) {
    Note note = notes.get(id);
    if (note == null) {
      return false;
    }
    if (note.getTags().contains(tag)) {
      note.removeTag(tag);
      return true;
    }
    return false;
  }

  /**
   * Удаляет заметку по ID.
   *
   * @param id ID заметки.
   * @return true, если заметка найдена и удалена, иначе false.
   */
  public boolean deleteNote(int id) {
    return notes.remove(id) != null;
  }

  /**
   * Ищет заметки, содержащие текст (без учета регистра).
   *
   * @param query Текст для поиска.
   * @return Список найденных заметок.
   */
  public List<Note> findNotesByText(String query) {
    if (query == null || query.isBlank()) {
      return List.of();
    }
    String lowered = query.toLowerCase();
    return notes.values().stream()
        .filter(note ->
            note.getTitle().toLowerCase().contains(lowered)
                || note.getText().toLowerCase().contains(lowered))
        .collect(Collectors.toList());
  }

  /**
   * Ищет заметки, содержащие ВСЕ указанные теги (без учета регистра).
   *
   * @param searchTags Набор тегов для поиска.
   * @return Список найденных заметок.
   */
  public List<Note> findNotesByTags(Set<String> searchTags) {
    if (searchTags == null || searchTags.isEmpty()) {
      return List.of();
    }
    Set<String> loweredTags = searchTags.stream()
        .filter(Objects::nonNull)
        .map(String::toLowerCase)
        .collect(Collectors.toSet());

    return notes.values().stream()
        .filter(note ->
            note.getTags().stream()
                .map(String::toLowerCase)
                .anyMatch(loweredTags::contains))
        .collect(Collectors.toList());
  }

  /**
   * Получает список всех уникальных тегов из всех заметок.
   *
   * @return Список уникальных тегов (в нижнем регистре).
   */
  public Set<String> getAllTags() {
    return notes.values().stream()
        .flatMap(note -> note.getTags().stream())
        .map(String::toLowerCase)
        .collect(Collectors.toSet());
  }
}