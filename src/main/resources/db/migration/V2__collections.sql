CREATE TABLE collections(
	id SERIAL PRIMARY KEY,
	typeId BIGINT,
	name VARCHAR(255),
	
	FOREIGN KEY(typeId) REFERENCES types(id)
);