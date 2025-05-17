Project Software Architecture Design UNISA 24/25 - GEOMETRIC DRAWING APP

This project involves the development of a geometric drawing application that allows users to create, edit, and manage multiple shapes on a drawing canvas. The application enables users to draw, modify, and organize geometric shapes (lines, rectangles, ellipses, polygons, and text) on a workspace.
Each shape is automatically placed above the previous ones through an implicit layering mechanism based on a shape stack, without requiring the user to manage layers manually.

Shapes can be selected by clicking on the canvas, giving priority to the visually topmost shape. Once selected, shapes can be moved, resized, rotated, mirrored, and stretched. The application allows users to copy, cut, and paste shapes, as well as group or ungroup them to manage multiple shapes as a single unit.

It is possible to assign text labels to shapes, which can be displayed and edited through the toolbar. Every action performed by the user can be undone or redone (Undo/Redo).

Users can save and load drawings in a proprietary format, preserving shapes, their properties, and layer order. Additionally, it is possible to import and export shape libraries, allowing custom shapes to be reused in other projects.

The canvas view supports an optional grid, with adjustable size, along with zoom and scrolling functionalities to facilitate precision in complex drawings.

The application architecture follows the Model-View-Controller (MVC) pattern.

User interactions such as drawing, selecting, moving, or deleting shapes are handled by the Controller, which translates user events into actions on the Model.

Shapes are structured through a Shape interface with concrete implementations.
Actions like Undo and Redo are managed using the Command pattern.

USEFUL LINKS:

- Trello: https://trello.com/b/D0SHQlkv/geometric-drawing
- Project Documentation: urly.it/319r4m
