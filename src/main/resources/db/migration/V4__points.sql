CREATE TABLE points(
	id SERIAL PRIMARY KEY,
	itemId BIGINT,
	latitude DOUBLE PRECISION,
	longitude DOUBLE PRECISION,
	elevation DOUBLE PRECISION,
	
	FOREIGN KEY(itemId) REFERENCES items(id) ON DELETE CASCADE
);