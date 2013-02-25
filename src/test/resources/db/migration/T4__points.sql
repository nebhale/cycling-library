CREATE TABLE points(
	id IDENTITY PRIMARY KEY,
	itemId BIGINT,
	latitude DOUBLE,
	longitude DOUBLE,
	elevation DOUBLE,
	
	FOREIGN KEY(itemId) REFERENCES items(id) ON DELETE CASCADE
);