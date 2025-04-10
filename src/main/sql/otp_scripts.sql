DROP TABLE IF EXISTS config;

CREATE TABLE config
(
	id INT NOT NULL PRIMARY KEY,
	code_length INT NOT NULL,
	lifetime INT NOT NULL
);

INSERT INTO config VALUES (1, 6, 60);

SELECT * FROM config;

DROP TABLE IF EXISTS users;

CREATE TABLE users
(
	user_login VARCHAR(512) NOT NULL PRIMARY KEY,
	user_role VARCHAR(32) NOT NULL,
	user_password_hash_code INT NOT NULL
);

SELECT * FROM users;


DROP VIEW IF EXISTS otp_statuses;
DROP TABLE IF EXISTS otp;

CREATE TABLE otp
(
	code VARCHAR(32) NOT NULL PRIMARY KEY,
	created_date TIMESTAMP NOT NULL DEFAULT (now() at time zone 'utc'),
	lifetime INT NOT NULL,
	was_used BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE VIEW otp_statuses AS
SELECT 	*
		,CASE 	WHEN was_used																		THEN 'USED'
				WHEN (now() AT time ZONE 'utc') - created_date <= INTERVAL '1 second' * lifetime 	THEN 'ACTIVE'
																									ELSE 'EXPIRED'
		END AS code_status
FROM otp;

select * from otp_statuses;




