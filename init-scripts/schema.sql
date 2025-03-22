CREATE TABLE library_user
(
	id		 SERIAL PRIMARY KEY,
	username   VARCHAR(50)  NOT NULL UNIQUE,
	email	  VARCHAR(100) NOT NULL UNIQUE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE book
(
	id			   SERIAL PRIMARY KEY,
	title			VARCHAR(255) NOT NULL,
	author		   VARCHAR(255) NOT NULL,
	isbn			 VARCHAR(20)  NOT NULL UNIQUE,
	available_copies INT		  NOT NULL,
	total_copies	 INT		  NOT NULL, -- This can be useful to know the inventory size.
	created_at	   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create an index on title for faster search by title.
CREATE INDEX idx_books_title ON book (title);

CREATE TABLE reservation
(
	id		 SERIAL PRIMARY KEY,
	library_user_id	INT		 NOT NULL,
	book_id	INT		 NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	expires_at TIMESTAMP, -- This will be set to created_at + 7 days if reservationEntity isn’t picked up.
	status	 VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'CANCELED', 'EXPIRED')),
	CONSTRAINT fk_library_user FOREIGN KEY (library_user_id) REFERENCES library_user (id),
	CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES book (id)
);

-- Indexes to support efficient queries:
-- For retrieving reservation by libraryUserEntity
CREATE INDEX idx_reservations_library_user ON reservation (id);
-- For retrieving reservation by bookEntity
CREATE INDEX idx_reservations_book ON reservation (id);
-- For efficient expiration checks
CREATE INDEX idx_reservations_expires ON reservation (expires_at);


insert into library_user (id,username,email) values ('1','carloscarvalho','carval.carlos@outlook.com');
insert into book (id,title,author,isbn,available_copies,total_copies) values ('1','Book Title','Carlos Carvalho','ISBN','10','15');
insert into reservation (id,library_user_id,book_id,expires_at,status) values ('1','1','1','2025-03-21 22:19:05.371949','ACTIVE');