CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,

                        member_code VARCHAR(50) NOT NULL,
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,

                        national_code VARCHAR(10) NULL,

                        description VARCHAR(500) NULL,

                        created_at DATETIME NOT NULL,
                        active BOOLEAN NOT NULL DEFAULT TRUE,

                        CONSTRAINT pk_member
                            PRIMARY KEY (id),

                        CONSTRAINT uk_member_member_code
                            UNIQUE (member_code)
);


CREATE TABLE member_phone (
                              id BIGINT NOT NULL AUTO_INCREMENT,

                              member_id BIGINT NOT NULL,

                              phone_number VARCHAR(11) NOT NULL,

                              phone_type VARCHAR(20) NOT NULL,

                              is_primary BOOLEAN NOT NULL DEFAULT FALSE,

                              description VARCHAR(500) NULL,

                              CONSTRAINT pk_member_phone
                                  PRIMARY KEY (id),

                              CONSTRAINT fk_member_phone_member
                                  FOREIGN KEY (member_id)
                                      REFERENCES member (id)
                                      ON DELETE CASCADE,

                              CONSTRAINT uk_member_phone_number
                                  UNIQUE (member_id, phone_number)
);


CREATE INDEX idx_member_phone_number
    ON member_phone (phone_number);


CREATE INDEX idx_member_last_name
    ON member (last_name);