package me.blokhin.homework005;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class IOCTest {
    @BeforeAll
    static void setup() {
        new CommandIOCInitialize().execute();
    }

    @Test
    @DisplayName("Throws IllegalStateException when trying to resolve unregistered dependency")
    void throwsOnUnregisteredDependency() {
        assertThrows(IllegalStateException.class, () -> IOC.resolve("IOCTest.throwsOnUnregisteredDependency"));
    }

    @Test
    @DisplayName("Throws IllegalStateException when trying to register dependency in root scope")
    void throwsOnRootScopeExtension() {
        final String dependencyName = "IOCTest.throwsOnRootScopeExtension.dependencyName";

        IOC.<Command>resolve("IOC.scopes.useRootScope").execute();

        final Command command = IOC.resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> new Object());

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Throws IllegalStateException when trying to use non-unique scope identifier")
    void throwsOnNonUniqueScopeIdentifier() {
        final String scopeId = "IOCTest.throwsOnNonUniqueScopeIdentifier.scopeId";

        IOC.<Command>resolve("IOC.scopes.useRootScope").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();

        final Command command = IOC.resolve("IOC.scopes.new", scopeId);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Throws IllegalStateException when trying to update dependency supplier")
    void throwsOnUpdateDependencySupplier() {
        final String scopeId = "IOCTest.throwsOnUpdateDependencySupplier.scopeId";
        final String dependencyName = "IOCTest.throwsOnUpdateDependencySupplier.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.useRootScope").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.scopes.useScope", scopeId).execute();
        IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();

        final Command command = IOC.resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Resolves dependency in current scope")
    void resolvesCurrentScope() {
        final String scopeId = "IOCTest.resolvesCurrentScope.scopeId";
        final String dependencyName = "IOCTest.resolvesCurrentScope.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.useRootScope").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.scopes.useScope", scopeId).execute();
        IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();

        assertSame(dependency, IOC.resolve(dependencyName));
    }

    @Test
    @DisplayName("Resolves dependency in parent scope")
    void resolvesParentScope() {
        final String parentScopeId = "IOCTest.resolvesParentScope.parentScopeId";
        final String scopeId = "IOCTest.resolvesParentScope.scopeId";
        final String dependencyName = "IOCTest.resolvesParentScope.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.useRootScope").execute();
        IOC.<Command>resolve("IOC.scopes.new", parentScopeId).execute();
        IOC.<Command>resolve("IOC.scopes.useScope", parentScopeId).execute();
        IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.scopes.useScope", scopeId).execute();

        assertSame(dependency, IOC.resolve(dependencyName));
    }

    @Test
    @DisplayName("Uses thread local current scope")
    void usesThreadLocalCurrentScope() throws InterruptedException {
        final String scopeId = "IOCTest.usesThreadLocalCurrentScope.scopeId";
        final String dependencyName = "IOCTest.usesThreadLocalCurrentScope.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.useRootScope").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            IOC.<Command>resolve("IOC.scopes.useScope", scopeId).execute();
            IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();
        });

        executor.shutdown();
        if (!executor.awaitTermination(50, TimeUnit.MILLISECONDS)) throw new IllegalStateException("Executor timed out");

        assertThrows(IllegalStateException.class, () -> IOC.resolve(dependencyName));
    }

    @Test
    @DisplayName("Uses shared scopes storage")
    void usesSharedScopesStorage() throws InterruptedException {
        final String scopeId = "IOCTest.usesSharedScopesStorage.scopeId";
        final String dependencyName = "IOCTest.usesSharedScopesStorage.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.useRootScope").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            IOC.<Command>resolve("IOC.scopes.useScope", scopeId).execute();
            IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();
        });

        executor.shutdown();
        if (!executor.awaitTermination(50, TimeUnit.MILLISECONDS)) throw new IllegalStateException("Executor timed out");

        IOC.<Command>resolve("IOC.scopes.useScope", scopeId).execute();

        assertSame(dependency, IOC.resolve(dependencyName));
    }
}