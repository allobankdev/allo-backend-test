package com.allobank.allobackendtest.service;

 import com.allobank.allobackendtest.mapper.BaseMapper;
 import jakarta.transaction.Transactional;
 import lombok.extern.slf4j.Slf4j;

 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.lang.NonNull;
 import com.allobank.allobackendtest.common.exception.EntityNotFoundException;

/**
 * Service
 * <p>
 * This class is designed to reduce boilerplate code by providing
 * a common implementation for operations such as create, read, update, delete.
 * </p>
 * <p>
 * To use this class:
 * <ol>
 *   <li>Create an entity (e.g., {@code DapilEntity})</li>
 *   <li>Create a DTO/Model (e.g., {@code Dapil})</li>
 *   <li>Implement the {@link BaseMapper} interface for conversion between entity and model</li>
 *   <li>Create a repository that extends {@link JpaRepository}</li>
 *   <li>Create a child service that extends {@code BaseService}</li>
 * </ol>
 * </p>
 *
 * @param <E>  JPA entity type (e.g., {@code DapilEntity})
 * @param <Req>  DTO type (e.g., {@code DapiDTO})
 * @param <Res> Model type (e.g., {@code Dapil})
 * @param <ID> Primary key type (e.g., {@code UUID}, {@code Long})
 * @param <R>  Repository managing the entity (must extend {@link JpaRepository})
 *
 * @author Asepimam
 * @since 1.0
 */
 @Slf4j
 public abstract class BaseService<E, Req,Res, ID, R extends JpaRepository<E, ID>> {

    /**
     * JPA repository used for persistence operations on the entity.
     * <p>
     * Injected via constructor by Spring. Must be defined as
     * {@link org.springframework.stereotype.Repository}.
     * </p>
     */
     protected final R repository;

    /**
     * Mapper responsible for conversion between:
     * <ul>
     *   <li>Entity ({@code E}) → Model ({@code D})</li>
     *   <li>Model ({@code D}) → Entity ({@code E})</li>
     * </ul>
     * <p>
     * Mapper implementation should be written manually or using
     * libraries such as MapStruct.
     * </p>
     */
     protected final BaseMapper<E, Req,Res> mapper;

    /**
     * Constructor for base service initialization.
     * <p>
     * Used by Spring for dependency injection. Make sure child services
     * also provide a constructor with the same order.
     * </p>
     *
     * @param repository JPA repository managing the entity. Must not be null.
     *                   Example: {@code DapilRepository}
     * @param mapper     Mapper implementation for entity ↔ model conversion.
     *                   Must not be null. Example: {@code DapilMapper.INSTANCE}
     *
     * @throws IllegalArgumentException if {@code repository} or {@code mapper} is null
     */
     public BaseService(@NonNull R repository, @NonNull BaseMapper<E, Req,Res> mapper) {
         if (repository == null) {
             throw new IllegalArgumentException("Repository tidak boleh null");
         }
         if (mapper == null) {
             throw new IllegalArgumentException("Mapper tidak boleh null");
         }
         this.repository = repository;
         this.mapper = mapper;
     }

    /**
     * Retrieves all entities from the database and converts them to DTOs.
     *
     * @return List of DTOs ({@code List<D>}) for all existing entities.
     *         Returns an empty list if no data is found.
     *
     * @example
     * <pre>
     * List<Dapil> allDapil = dapilService.getAll();
     * </pre>
     */
     public Page<Res> getAll(Pageable pageable) {
         return repository.findAll(pageable)
                 .map(mapper::toResponse);
     }

    /**
     * Finds an entity by its ID.
     *
     * @param id Unique ID of the entity to find. Must not be null.
     * @return DTO corresponding to the ID.
     * @throws EntityNotFoundException if the entity with the given ID is not found
     *
     * @example
     * <pre>
     * UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
     * Dapil dapil = dapilService.getById(id);
     * </pre>
     */
     public Res getById(@NonNull ID id) {
         E entity = repository.findById(id)
                 .orElseThrow(() -> new EntityNotFoundException(
                     String.format("Entitas tidak ditemukan dengan ID: %s", id)
                 ));
         return mapper.toResponse(entity);
     }

    /**
     * Creates a new entity from the DTO and saves it to the database.
     *
     * @param model Input DTO containing new data. Must not be null.
     * @return DTO result that has been saved, including the generated ID.
     * @throws IllegalArgumentException if model is null or validation fails
     *
     * @example
     * <pre>
     * Dapil newDapil = new Dapil();
     * newDapil.setNamaDapil("Dapil Jawa");
     * // ... set other fields
     * Dapil saved = dapilService.create(newDapil);
     * </pre>
     */
     @Transactional
     public Res create(@NonNull Req model) {
         E entity = mapper.toEntity(model);
         E saved = repository.save(entity);
         return mapper.toResponse(saved);
     }

    /**
     * Updates an existing entity by its ID.
     * <p>
     * Only fields present in the DTO will be updated.
     * The ID from the DTO will be ignored; the ID from the URL parameter is used.
     * </p>
     *
     * @param id    ID of the entity to update. Must not be null.
     * @param model DTO containing new data. Must not be null.
     * @return DTO result after update.
     * @throws EntityNotFoundException if the entity with the given ID is not found
     *
     * @example
     * <pre>
     * Dapil updateData = new Dapil();
     * updateData.setNamaDapil("New Dapil");
     * Dapil updated = dapilService.update(id, updateData);
     * </pre>
     */
     public Res update(@NonNull ID id, @NonNull Req model) {
         E existing = repository.findById(id)
                 .orElseThrow(() -> new EntityNotFoundException("Entitas tidak ditemukan dengan ID: " + id));

         mapper.updateFromRequest(model, existing);
         E updated = repository.save(existing);
         return mapper.toResponse(updated);
     }


         /**
             * Deletes an entity by its ID.
             *
             * @param id ID of the entity to delete. Must not be null.
             * @throws EntityNotFoundException if the entity is not found
             *
             * @example
             * <pre>
             * dapilService.delete(id);
             * </pre>
             */
     @Transactional
     public void delete(@NonNull ID id) {
         System.out.println("Deleting entity with ID: " + id);

         if (!repository.existsById(id)) {
             System.out.println("ID not found");
             throw new EntityNotFoundException(
                     String.format("Entity not found with ID: %s", id)
             );
         }

         repository.deleteById(id);
         System.out.println("Successfully deleted");
     }

    // --- Helper Methods (Optional) ---

    /**
     * Get the ID from the entity using reflection or convention.
     * Can be overridden if the ID is not accessed via standard getter.
     */
     @SuppressWarnings("unchecked")
     private ID getId(E entity) {
         // Asumsi ada getId() → bisa di-refactor jika butuh kompleks
         try {
             return (ID) entity.getClass().getMethod("getId").invoke(entity);
         } catch (Exception e) {
             throw new RuntimeException("Gagal mendapatkan ID dari entity", e);
         }
     }

    /**
     * Set the ID to the target entity.
     */
     private void setId(E entity, ID id) {
         try {
             entity.getClass().getMethod("setId", id.getClass()).invoke(entity, id);
         } catch (Exception e) {
             throw new RuntimeException("Gagal menetapkan ID ke entity", e);
         }
     }
 }


