/**
 * This package contains the class {@link gameobjects.GameObject} and some subclasses defined for
 * convenience and ease of use. These classes describe dynamic game objects appearing in a game
 * level described by a {@link playground.Playground} subclass. {@link gameobjects.GameObject} is
 * not usually subclassed except for simplicity to define objects with new properties. Instead, it
 * aggregates instances of {@link rendering.Artist}, {@link controller.ObjectController} and
 * {@link collider.Collider} which you can subclass separately to modularly change and adapt certain
 * aspects of a game object. See more in the documentation of {@link gameobjects.GameObject}.
 */
package gameobjects;
