CREATE DATABASE Library_DB;

CREATE TABLE LIBRARY(
    ----- Attributes -----
    libraryID INTEGER NOT NULL,
    libraryName VARCHAR(50) NOT NULL,
    libraryNumBooks INT NOT NULL,

    ----- Table Constraint -----
    PRIMARY KEY (libraryID)
);

CREATE TABLE BOOK(
    ----- Attributes -----
    bookTitle VARCHAR(50) NOT NULL,
    bookAuthor VARCHAR(50) NOT NULL,
    bookISBN INTEGER UNIQUE,
    bookNumPages INTEGER NOT NULL,
    bookYearRead INTEGER NOT NULL,
    bookCategory VARCHAR(30),
    libraryID INTEGER NOT NULL,

    ----- Table Constraints -----
    PRIMARY KEY (bookISBN, libraryID),
    FOREIGN KEY (libraryID) REFERENCES LIBRARY(libraryID)
);

-- Later on, I may need to create a USER table and any appropriate bridging table(s)