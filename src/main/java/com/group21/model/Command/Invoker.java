/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import java.util.Stack;

/**
 *
 * @author matte
 */
public class Invoker {

    private Command command;
    private Stack<Command> stack = new Stack<>();

    public void setCommand(Command command) {
        this.command = command;
    }

    public void startCommand() {
        command.execute();
        stack.add(command);
    }

    public void startUndo() {

        Command lastCommand = stack.pop();
        lastCommand.undo();

    }

}
