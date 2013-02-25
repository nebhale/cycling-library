CREATE TABLE items(
	id SERIAL PRIMARY KEY,
	collectionId BIGINT,
	name VARCHAR(64),
	
	FOREIGN KEY(collectionId) REFERENCES collections(id) ON DELETE CASCADE
);