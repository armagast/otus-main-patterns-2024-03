package me.blokhin;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Value
public class CommandIOCInitialize implements Command {
    private static final String ROOT_SCOPE_ID = "root";

    private static boolean initialized = false;
    private static final Map<String, Map<String, DependencySupplier>> scopes = new HashMap<>();
    private static final ThreadLocal<String> currentScopeId = ThreadLocal.withInitial(() -> ROOT_SCOPE_ID);

    @Override
    public void execute() {
        if (initialized) return;
        synchronized (scopes) {
            if (initialized) return;

            final Map<String, DependencySupplier> scope = new HashMap<>();
            scopes.put(ROOT_SCOPE_ID, scope);

            scope.put("IOC.scopes.parentScopeId", (args) -> null);

            //region Commands
            scope.put(
                    "IOC.register",
                    (args) -> (Command) () -> this.dependenciesRegister((String) args[0], (DependencySupplier) args[1])
            );

            scope.put(
                    "IOC.scopes.new",
                    (args) -> (Command) () -> this.scopesNew((String) args[0])
            );

            scope.put(
                    "IOC.scopes.useRootScope",
                    (args) -> (Command) this::scopesUseRootScope
            );

            scope.put(
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
        if (Objects.equals(scopeId, ROOT_SCOPE_ID)) throw new IllegalStateException("Dependency registration in root scope is not allowed");

        final Map<String, DependencySupplier> scope = getScope(scopeId);
        if (scope.containsKey(name)) throw new IllegalStateException(String.format("Dependency {%s} already registered", name));

        scope.put(name, supplier);
    }

    private Optional<DependencySupplier> dependenciesResolve(final String name) {
        Map<String, DependencySupplier> scope = getCurrentScope();

        while (Objects.nonNull(scope)) {
            if (scope.containsKey(name)) return Optional.of(scope.get(name));
            scope = getParentScope(scope).orElse(null);
        }

        return Optional.empty();
    }
    //endregion

    //region Scopes related stuff.
    private void scopesNew(final String scopeId) {
        Assert.notNull(scopeId, "{scopeId} must not be null");

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
        Assert.notNull(scopeId, "{scopeId} must not be null");

        if (scopes.containsKey(scopeId)) return scopes.get(scopeId);

        throw new IllegalStateException(String.format("Scope {%s} not found", scopeId));
    }

    private Map<String, DependencySupplier> getCurrentScope() {
        return getScope(currentScopeId.get());
    }

    private Optional<Map<String, DependencySupplier>> getParentScope(final Map<String, DependencySupplier> scope) {
        final String parentScopeId = (String) scope.get("IOC.scopes.parentScopeId").get();

        return Objects.nonNull(parentScopeId) ? Optional.of(getScope(parentScopeId)) : Optional.empty();
    }
}
