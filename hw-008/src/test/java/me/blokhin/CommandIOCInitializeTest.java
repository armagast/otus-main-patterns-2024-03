package me.blokhin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;

class CommandIOCInitializeTest {
    @BeforeAll
    static void setup() {
        new CommandIOCInitialize().execute();
    }

    @DisplayName("Throws IllegalStateException when trying to resolve unregistered dependency")
    @Test
    void throwsOnUnregisteredDependency() {
        assertThrows(IllegalStateException.class, () -> IOC.resolve("CommandIOCInitializeTest.throwsOnUnregisteredDependency"));
    }

    @DisplayName("Throws IllegalStateException when trying to register dependency in root scope")
    @Test
    void throwsOnRootScopeExtension() {
        final String dependencyName = "CommandIOCInitializeTest.throwsOnRootScopeExtension.dependencyName";

        IOC.<Command>resolve("IOC.scopes.use", "root").execute();

        final Command command = IOC.resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> new Object());
        assertThrows(IllegalStateException.class, command::execute);
    }

    @DisplayName("Throws IllegalStateException when trying to use non-unique scope identifier")
    @Test
    void throwsOnNonUniqueScopeIdentifier() {
        final String scopeId = "CommandIOCInitializeTest.throwsOnNonUniqueScopeIdentifier.scopeId";

        IOC.<Command>resolve("IOC.scopes.use", "root").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();

        final Command command = IOC.resolve("IOC.scopes.new", scopeId);
        assertThrows(IllegalStateException.class, command::execute);
    }

    @DisplayName("Throws IllegalStateException when trying to update dependency supplier")
    @Test
    void throwsOnUpdateDependencySupplier() {
        final String scopeId = "CommandIOCInitializeTest.throwsOnUpdateDependencySupplier.scopeId";
        final String dependencyName = "CommandIOCInitializeTest.throwsOnUpdateDependencySupplier.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.use", "root").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.scopes.use", scopeId).execute();
        IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();

        final Command command = IOC.resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @DisplayName("Resolves dependency in current scope")
    @Test
    void resolvesCurrentScope() {
        final String scopeId = "CommandIOCInitializeTest.resolvesCurrentScope.scopeId";
        final String dependencyName = "CommandIOCInitializeTest.resolvesCurrentScope.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.use", "root").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.scopes.use", scopeId).execute();
        IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();

        assertSame(dependency, IOC.resolve(dependencyName));
    }

    @DisplayName("Resolves dependency in parent scope")
    @Test
    void resolvesParentScope() {
        final String parentScopeId = "CommandIOCInitializeTest.resolvesParentScope.parentScopeId";
        final String scopeId = "CommandIOCInitializeTest.resolvesParentScope.scopeId";
        final String dependencyName = "CommandIOCInitializeTest.resolvesParentScope.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.use", "root").execute();
        IOC.<Command>resolve("IOC.scopes.new", parentScopeId).execute();
        IOC.<Command>resolve("IOC.scopes.use", parentScopeId).execute();
        IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.scopes.use", scopeId).execute();

        assertSame(dependency, IOC.resolve(dependencyName));
    }

    @DisplayName("Uses thread local current scope")
    @Test
    @Timeout(1)
    void usesThreadLocalCurrentScope() throws InterruptedException {
        final String scopeId = "CommandIOCInitializeTest.usesThreadLocalCurrentScope.scopeId";
        final String dependencyName = "CommandIOCInitializeTest.usesThreadLocalCurrentScope.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.use", "root").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        new Thread(() -> {
            IOC.<Command>resolve("IOC.scopes.use", scopeId).execute();
            IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();

            semaphore.release();
        }).start();

        semaphore.acquire();

        assertThrows(IllegalStateException.class, () -> IOC.resolve(dependencyName));
    }

    @DisplayName("Uses shared scopes storage")
    @Test
    @Timeout(1)
    void usesSharedScopesStorage() throws InterruptedException {
        final String scopeId = "CommandIOCInitializeTest.usesSharedScopesStorage.scopeId";
        final String dependencyName = "CommandIOCInitializeTest.usesSharedScopesStorage.dependencyName";
        final Object dependency = new Object();

        IOC.<Command>resolve("IOC.scopes.use", "root").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        new Thread(() -> {
            IOC.<Command>resolve("IOC.scopes.use", scopeId).execute();
            IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> dependency).execute();

            semaphore.release();
        }).start();

        semaphore.acquire();

        IOC.<Command>resolve("IOC.scopes.use", scopeId).execute();

        assertSame(dependency, IOC.resolve(dependencyName));
    }

    @DisplayName("Allows null values as dependency")
    @Test
    void allowsNullValues() {
        final String scopeId = "CommandIOCInitializeTest.allowsNullValues.scopeId";
        final String dependencyName = "CommandIOCInitializeTest.allowsNullValues.dependencyName";

        IOC.<Command>resolve("IOC.scopes.use", "root").execute();
        IOC.<Command>resolve("IOC.scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.scopes.use", scopeId).execute();
        IOC.<Command>resolve("IOC.register", dependencyName, (DependencySupplier) (args) -> null).execute();

        assertNull(IOC.resolve(dependencyName));
    }
}