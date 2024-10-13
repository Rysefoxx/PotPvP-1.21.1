package io.potpvp.minecraft.interceptor;

import io.potpvp.minecraft.annotation.AutoSave;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This class represents an entity interceptor.
 *
 * @author Rysefoxx
 * @since 13.10.2024
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class EntityInterceptor<T> implements MethodInterceptor {

  private final T entity;
  private final JpaRepository<T, ?> repository;
  private final Queue<T> queue = new ArrayDeque<>();
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    if (method.isAnnotationPresent(AutoSave.class)) {
      return saveAndReturn(args, proxy);
    }

    return proxy.invoke(this.entity, args);
  }

  private Object saveAndReturn(Object[] args, MethodProxy proxy) throws Throwable {
    Object result = proxy.invoke(this.entity, args);

    if (!this.queue.contains(this.entity)) {
      this.queue.add(this.entity);

      this.scheduler.schedule(() -> {
        CompletableFuture.runAsync(() -> this.repository.save(this.entity));
        this.queue.remove(this.entity);
      }, 1, TimeUnit.SECONDS);
    }

    return result;
  }
}