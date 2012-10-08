# Intro

Trial task for the Java Developer position at Complitex.

By Andy Belsky, andy@abelsky.com.


# Overview

Simple, but nice and AJAX-ish web application for trivial task tracking.
Built with Wicket and MyBatis, uses Maven for building and Glassfish v3 
as a container.


# Structure

There are a lot of comments in the code, so it seems no need to duplicate
any details here.

To deploy on another machine, you should definitely:

 1. Check `src\main\resources\database.properties` for DB access details

 2. Create DB using init scripts from `src\sql`

 3. Check Maven's `pom.xml` for any missing options.

