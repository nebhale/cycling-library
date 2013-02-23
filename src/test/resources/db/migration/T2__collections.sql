CREATE TABLE collections(
	id IDENTITY PRIMARY KEY,
	typeId BIGINT,
	name VARCHAR(255),
	
	FOREIGN KEY(typeId) REFERENCES types(id)
);