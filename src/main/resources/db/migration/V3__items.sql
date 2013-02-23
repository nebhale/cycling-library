CREATE TABLE items(
	id SERIAL PRIMARY KEY,
	collectionId BIGINT,
	name VARCHAR(255),
	
	FOREIGN KEY(collectionId) REFERENCES collections(id)
);