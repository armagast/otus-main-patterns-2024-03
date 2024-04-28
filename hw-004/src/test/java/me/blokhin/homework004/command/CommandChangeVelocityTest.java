package me.blokhin.homework004.command;

import me.blokhin.homework004.Angle;
import me.blokhin.homework004.Vector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandChangeVelocityTest {
    @Test
    @DisplayName("Throw IllegalArgumentException when {target} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandChangeVelocity(null));
    }

    @Test
    @DisplayName("Changes velocity")
    void changesVelocity() {
        final Vector velocity = mock(Vector.class);
        final Angle angularVelocity = mock(Angle.class);
        final Vector velocityNext = mock(Vector.class);
        final VelocityChangeable target = mock(VelocityChangeable.class);
        doReturn(angularVelocity).when(target).getAngularVelocity();
        doReturn(velocity).when(target).getVelocity();
        doReturn(velocityNext).when(velocity).rotate(angularVelocity);

        final Command command = new CommandChangeVelocity(target);
        command.execute();

        ArgumentCaptor<Vector> velocityCaptor = ArgumentCaptor.forClass(Vector.class);
        verify(target).setVelocity(velocityCaptor.capture());
        assertEquals(velocityNext, velocityCaptor.getValue());
    }

    @Test
    @DisplayName("Throws when angularVelocity is null")
    void throwsWhenAngularVelocityIsNull() {
        final Vector velocity = mock(Vector.class);
        final Angle angularVelocity = mock(Angle.class);
        final Vector velocityNext = mock(Vector.class);
        final VelocityChangeable target = mock(VelocityChangeable.class);
        doReturn(null).when(target).getAngularVelocity();
        doReturn(velocity).when(target).getVelocity();
        doReturn(velocityNext).when(velocity).rotate(angularVelocity);

        final Command command = new CommandChangeVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by MacroVelocityChangeable::getAngularVelocity")
    void passesThroughThrownByMacroVelocityChangeableGetAngularVelocity() {
        final Vector velocity = mock(Vector.class);
        final Angle angularVelocity = mock(Angle.class);
        final Vector velocityNext = mock(Vector.class);
        final VelocityChangeable target = mock(VelocityChangeable.class);
        doThrow(new IllegalStateException()).when(target).getAngularVelocity();
        doReturn(velocity).when(target).getVelocity();
        doReturn(velocityNext).when(velocity).rotate(angularVelocity);

        final Command command = new CommandChangeVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Throws when velocity is null")
    void throwsWhenVelocityIsNull() {
        final Vector velocity = mock(Vector.class);
        final Angle angularVelocity = mock(Angle.class);
        final Vector velocityNext = mock(Vector.class);
        final VelocityChangeable target = mock(VelocityChangeable.class);
        doReturn(angularVelocity).when(target).getAngularVelocity();
        doReturn(null).when(target).getVelocity();
        doReturn(velocityNext).when(velocity).rotate(angularVelocity);

        final Command command = new CommandChangeVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by MacroVelocityChangeable::getVelocity")
    void passesThroughThrownByMacroVelocityChangeableGetVelocity() {
        final Vector velocity = mock(Vector.class);
        final Angle angularVelocity = mock(Angle.class);
        final Vector velocityNext = mock(Vector.class);
        final VelocityChangeable target = mock(VelocityChangeable.class);
        doReturn(angularVelocity).when(target).getAngularVelocity();
        doThrow(new IllegalStateException()).when(target).getVelocity();
        doReturn(velocityNext).when(velocity).rotate(angularVelocity);

        final Command command = new CommandChangeVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by MacroVelocityChangeable::setVelocity")
    void passesThroughThrownByMacroVelocityChangeableSetVelocity() {
        final Vector velocity = mock(Vector.class);
        final Angle angularVelocity = mock(Angle.class);
        final Vector velocityNext = mock(Vector.class);
        final VelocityChangeable target = mock(VelocityChangeable.class);
        doReturn(angularVelocity).when(target).getAngularVelocity();
        doReturn(velocity).when(target).getVelocity();
        doReturn(velocityNext).when(velocity).rotate(angularVelocity);
        doThrow(new IllegalStateException()).when(target).setVelocity(any());

        final Command command = new CommandChangeVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Vector::rotate")
    void passesThroughThrownByVectorRotate() {
        final Vector velocity = mock(Vector.class);
        final Angle angularVelocity = mock(Angle.class);
        // final Vector velocityNext = mock(Vector.class);
        final VelocityChangeable target = mock(VelocityChangeable.class);
        doReturn(angularVelocity).when(target).getAngularVelocity();
        doReturn(velocity).when(target).getVelocity();
        doThrow(new IllegalStateException()).when(velocity).rotate(angularVelocity);

        final Command command = new CommandChangeVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
