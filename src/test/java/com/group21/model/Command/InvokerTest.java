/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import com.group21.model.Shape.*;

/**
 *
 * @author matte
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvokerTest {

    private Invoker invoker;
    private FakeCommand fakeCommand;

    static class FakeCommand implements Command {
        boolean executed = false;

        @Override
        public void execute() {
            executed = true;
        }

        public boolean wasExecuted() {
            return executed;
        }
    }

    @BeforeEach
    void setUp() {
        invoker = new Invoker();
        fakeCommand = new FakeCommand();
    }

    @Test
    void testStartCommandExecutesCommand() {
        invoker.setCommand(fakeCommand);
        invoker.startCommand();
        assertTrue(fakeCommand.wasExecuted(), "Il comando dovrebbe essere stato eseguito");
    }

    @Test
    void testStartCommandWithoutSetCommandDoesNothing() {
        assertDoesNotThrow(() -> invoker.startCommand(), "startCommand() senza comando non deve lanciare eccezioni");
    }
}

