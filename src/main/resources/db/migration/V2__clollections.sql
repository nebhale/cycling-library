CREATE TABLE types(
	id SERIAL PRIMARY KEY,
	typeId BIGINT,
	name VARCHAR(255),
	
	FOREIGN KEY(typeId) REFERENCES collections(id)
);