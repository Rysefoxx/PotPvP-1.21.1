package io.potpvp.minecraft.factory;

import io.potpvp.minecraft.exception.EntityCreationException;
import io.potpvp.minecraft.interceptor.EntityInterceptor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This class represents an entity proxy factory.
 *
 * @author Rysefoxx
 * @since 13.10.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityProxyFactory {

  /**
   * This method creates a proxy for the given entity.
   *
   * @param entity     the entity
   * @param repository the repository
   * @param <T>        the entity type
   * @return the proxy instance
   */
  @SuppressWarnings("unchecked")
  public static <T> T createProxy(T entity, JpaRepository<T, ?> repository) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(entity.getClass());
    enhancer.setCallback(new EntityInterceptor<>(entity, repository));
    return (T) enhancer.create();
  }

  /**
   * This method creates a proxy for the given entity class.
   *
   * @param clazz      the entity class
   * @param repository the repository
   * @param <T>        the entity type
   * @return the proxy instance
   */
  public static <T> T createProxy(Class<T> clazz, JpaRepository<T, ?> repository) {
    T entity;
    try {
      entity = clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new EntityCreationException("Failed to create entity instance of " + clazz.getName(), e);
    }

    return createProxy(entity, repository);
  }
}