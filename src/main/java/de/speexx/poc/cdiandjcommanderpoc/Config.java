/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.speexx.poc.cdiandjcommanderpoc;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** @author Sascha Kohlmann */
@Qualifier
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface Config {}
