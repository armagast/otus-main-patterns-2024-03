package me.blokhin.homework005;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Value
public class CommandIOCInitialize implements Command {
    private static boolean initialized = false;
    private static final Map<String, DependencySupplier> rootScope = new HashMap<>();
    private static final Map<String, Map<String, DependencySupplier>> scopes = new HashMap<>();
    private static final ThreadLocal<String> currentScopeId = new ThreadLocal<>();

    @Override
    public void execute() {
        if (initialized) return;
        synchronized (rootScope) {
            if (initialized) return;

            //region Commands
            rootScope.put(
                    "IOC.register",
                    (args) -> (Command) () -> this.dependenciesRegister((String) args[0], (DependencySupplier) args[1])
            );

            rootScope.put(
                    "IOC.scopes.new",
                    (args) -> (Command) () -> this.scopesNew((String) args[0])
            );

            rootScope.put(
                    "IOC.scopes.useRootScope",
                    (args) -> (Command) this::scopesUseRootScope
            );

            rootScope.put(
                    "IOC.scopes.useScope",
                    (args) -> (Command) () -> this.scopesUseScope((String) args[0])
            );
            //endregion

            IOC.<Command>resolve("IOC.updateResolver", (DependencyResolver) this::dependenciesResolve).execute();

            initialized = true;
        }
    }

    //region Dependencies related stuff.
    private void dependenciesRegister(final String name, final DependencySupplier supplier) {
        final String scopeId = currentScopeId.get();
        if (Objects.isNull(scopeId)) throw new IllegalStateException("Dependency registration in root scope is not allowed");

        final Map<String, DependencySupplier> scope = getScope(scopeId);
        if (scope.containsKey(name)) throw new IllegalStateException(String.format("Dependency {%s} already registered", name));

        scope.put(name, supplier);
    }

    private Optional<DependencySupplier> dependenciesResolve(final String name) {
        Map<String, DependencySupplier> scope = getCurrentScope();

        while (Objects.nonNull(scope)) {
            if (scope.containsKey(name)) return Optional.of(scope.get(name));
            scope = getParentScope(scope);
        }

        return Optional.empty();
    }
    //endregion

    //region Scopes related stuff.
    private void scopesNew(final String scopeId) {
        synchronized (scopes) {
            if (scopes.containsKey(scopeId)) throw new IllegalStateException(String.format("Scope {%s} already exists", scopeId));

            final Map<String, DependencySupplier> scope = new HashMap<>();

            final String parentScopeId = currentScopeId.get();
            scope.put("IOC.scopes.parentScopeId", (args) -> parentScopeId);

            scopes.put(scopeId, scope);
        }
    }

    private void scopesUseRootScope() {
        currentScopeId.remove();
    }

    private void scopesUseScope(final String scopeId) {
        currentScopeId.set(scopeId);
    }
    //endregion

    private Map<String, DependencySupplier> getScope(final String scopeId) {
        if (scopes.containsKey(scopeId)) return scopes.get(scopeId);
        throw new IllegalStateException(String.format("Scope {%s} not found", scopeId));
    }

    private Map<String, DependencySupplier> getCurrentScope() {
        final String scopeId = currentScopeId.get();
        return Objects.nonNull(scopeId) ? getScope(scopeId) : rootScope;
    }

    private Map<String, DependencySupplier> getParentScope(final Map<String, DependencySupplier> scope) {
        if (scope == rootScope) return null;

        final String parentScopeId = (String) scope.get("IOC.scopes.parentScopeId").get();
        return Objects.nonNull(parentScopeId) ? getScope(parentScopeId) : rootScope;
    }
}
