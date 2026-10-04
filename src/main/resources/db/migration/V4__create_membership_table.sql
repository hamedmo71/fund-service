CREATE TABLE membership (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            member_id BIGINT NOT NULL,
                            start_date DATE NOT NULL,
                            end_date DATE NULL,
                            status VARCHAR(20) NOT NULL,

                            CONSTRAINT pk_membership
                                PRIMARY KEY (id),

                            CONSTRAINT fk_membership_member
                                FOREIGN KEY (member_id)
                                    REFERENCES member (id),

                            CONSTRAINT ck_membership_date_range
                                CHECK (
                                    end_date IS NULL
                                        OR end_date >= start_date
                                    )
);

CREATE INDEX idx_membership_member
    ON membership (member_id);

CREATE INDEX idx_membership_status
    ON membership (status);