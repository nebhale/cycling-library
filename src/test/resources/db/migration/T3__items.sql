CREATE TABLE items(
	id IDENTITY PRIMARY KEY,
	collectionId BIGINT,
	name VARCHAR(255),
	
	FOREIGN KEY(collectionId) REFERENCES collections(id) ON DELETE CASCADE
);