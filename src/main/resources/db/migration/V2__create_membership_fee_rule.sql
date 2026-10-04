CREATE TABLE membership_fee_rule
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    effective_from DATE   NOT NULL,
    effective_to   DATE NULL,
    amount         BIGINT NOT NULL,
    period VARCHAR (20) NOT NULL,

    CONSTRAINT pk_membership_fee_rule
        PRIMARY KEY (id),

    CONSTRAINT ck_membership_fee_rule_amount
        CHECK (amount > 0),

    CONSTRAINT ck_membership_fee_rule_date_range
        CHECK (effective_to IS NULL OR effective_to >= effective_from)
);